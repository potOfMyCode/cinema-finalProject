package controller.command;

import model.dao.exceptions.DaoException;
import model.entity.Ticket;
import model.entity.User;
import model.service.TicketService;
import model.util.Languages;
import model.util.LogGen;
import model.util.Role;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.util.LogMsg.*;
import static model.util.LogMsg.TICKET_PURCHASED_SUCCESSFULLY;

public class PurchaseTicket implements Command{
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";
    private final String LOCAL_RB_BASE_NAME = "lang";
    private final String MESSAGE = "msg";
    private final String MESSAGE2 = "msg2";
    private final String SESSION_ID_PARAM = "sessionId";
    private final String PLACE = "place";
    private final String CURDATE = "curDate";

    private static Logger log = LogGen.getInstance();
    private TicketService ticketService;

    public PurchaseTicket(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        User sessionUser = (User) request.getSession().getAttribute(SESSION_USER);
        Optional<Role> role = Optional.ofNullable((sessionUser).getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));
        ResourceBundle rsBundle = ResourceBundle.getBundle(LOCAL_RB_BASE_NAME, locale);

        ticketService.setDaoLocale(locale);
        String outUrlOK = "forward:/WEB-INF/util/operation_success.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        String outUrlInvalid = "forward:/WEB-INF/util/operation_fail.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());

        String placeParam = request.getParameter(PLACE);
        String sessionIdParam = request.getParameter(SESSION_ID_PARAM);
        String curDay = request.getParameter("curDay");
        String curMonth = request.getParameter("curMonth");
        String curYear = request.getParameter("curYear");

        LocalDate date = LocalDate.of(Integer.parseInt(curYear), Integer.parseInt(curMonth), Integer.parseInt(curDay));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        System.out.println(date);

        System.out.println("CURDATE:::::::::: " + curDay + "-" + curMonth + "-" + curYear);

        if (invalidInput(placeParam, sessionIdParam)) {
            //set attribute Bean with fail message
            request.setAttribute(MESSAGE, rsBundle.getString("wrong.data"));
            request.setAttribute(MESSAGE2, rsBundle.getString("back.room"));
            log.info(PURCH_TICKET_BAD_INPUT_DATA + " place = " + Optional.ofNullable(placeParam).toString() +
                    " sessionID = " + Optional.ofNullable(sessionIdParam).toString());
            return outUrlInvalid;
        }

        Ticket ticketToSave = generateRawTicket(placeParam, sessionIdParam, sessionUser, date);

        try {
            ticketService.createTicket(ticketToSave);
            log.info(TICKET_CREATED_SUCCESSFULLY + " By " + sessionUser.toString());
        } catch (DaoException e) {
            request.setAttribute(MESSAGE, rsBundle.getString("ticket.purch.already.bought"));
            request.setAttribute(MESSAGE2, rsBundle.getString("back.room"));
            log.error(CANT_CREATE_TICKET2, e);
            return outUrlInvalid;
        }
        request.setAttribute(MESSAGE, rsBundle.getString("success.bought"));
        request.setAttribute(MESSAGE2, rsBundle.getString("back.room"));
        log.error(TICKET_PURCHASED_SUCCESSFULLY);
        return outUrlOK;
    }

    private boolean invalidInput(String place, String session) {
        return !Optional.ofNullable(place).isPresent() ||
                !Optional.ofNullable(session).isPresent() ||
                !place.matches("[0-9]+") ||
                !session.matches("[0-9]+");

    }

    private Ticket generateRawTicket(String place, String sessionID, User user, LocalDate date) {
        Ticket ticket = new Ticket();

        int userID = user.getId();
        int sessionIDNumber = Integer.parseInt(sessionID);
        int placeNumber = Integer.parseInt(place);

        ticket.setUserID(userID);
        ticket.setSessionID(sessionIDNumber);
        ticket.setPlace(placeNumber);
        ticket.setDate(date);

        return ticket;
    }
}
