package controller.command;

import model.entity.Ticket;
import model.entity.User;
import model.service.TicketService;
import model.util.Languages;
import model.util.LogGen;
import model.util.Role;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class RemoveTicket implements Command{
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";
    private final String LOCAL_RB_BASE_NAME = "lang";
    private final String MESSAGE = "msg";
    private final String TICKET_ID = "ticketId";
    private final String SESSION_ID_PARAM = "sessionId";

    private static Logger log = LogGen.getInstance();
    private TicketService ticketService;

    public RemoveTicket(TicketService ticketService) {
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
        String outUrlOK = "redirect:" + request.getContextPath() + request.getServletPath() + "/tickets" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        System.out.println("In RemoveTicket outUrlOk: " + outUrlOK);
        String outUrlInvalid = "forward:/WEB-INF/util/operation_fail.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());

        String ticketIdParam = request.getParameter(TICKET_ID);

        if (invalidInput(ticketIdParam)) {
            request.setAttribute(MESSAGE, rsBundle.getString("del.ticket.wrong.input"));
            return outUrlInvalid;
        }

        int ticketID = Integer.parseInt(ticketIdParam);
        Ticket ticketToRemove = generateRawTicket(ticketID);

        if (!doesntOwnTicket(sessionUser, ticketToRemove)) {
            request.setAttribute(MESSAGE, rsBundle.getString("del.ticket.not.yours"));
            return outUrlInvalid;
        }

        ticketService.removeTicket(ticketToRemove);
        request.setAttribute(MESSAGE, rsBundle.getString("success.bought"));
        return outUrlOK;
    }

    private boolean doesntOwnTicket(User user, Ticket ticket) {
        List<Ticket> actualTickets = ticketService.getTicketsByUserId(user.getId());
        Optional<Ticket> t = actualTickets.stream().filter(a->a.getId() == ticket.getId()).findFirst();
        return t.isPresent();
    }

    private boolean invalidInput(String ticketID) {
        return !Optional.ofNullable(ticketID).isPresent() || !ticketID.matches("[0-9]+");
    }

    private Ticket generateRawTicket(int id) {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        return ticket;
    }
}
