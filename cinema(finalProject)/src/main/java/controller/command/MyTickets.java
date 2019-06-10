package controller.command;

import model.entity.Ticket;
import model.entity.User;
import model.service.TicketService;
import model.util.Languages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class MyTickets implements Command{
    private final String TICKET_LIST = "tickets";
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";
    private final String CUR_DATE = "curDate";

    private TicketService ticketService;

    public MyTickets(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User curUser = (User) request.getSession().getAttribute(SESSION_USER);
        Optional<Object> role = Optional.ofNullable(curUser.getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));
        ticketService.setDaoLocale(locale);

        LocalDate date = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        System.out.println("localdate.now(): " + date);
        request.setAttribute(CUR_DATE, date);

        List<Ticket> tickets = ticketService.getTicketsByUserId(curUser.getId());
        request.setAttribute(TICKET_LIST, tickets);

        return role.map(o -> "forward:/WEB-INF/" + o.toString() + "/my_tickets.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString()))
                .orElse("forward:/login");
    }
}
