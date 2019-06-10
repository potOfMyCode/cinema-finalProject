package controller.command;

import model.dao.exceptions.DaoException;
import model.entity.Session;
import model.entity.User;
import model.service.SessionService;
import model.util.Languages;
import model.util.LogGen;
import model.util.Role;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static model.util.LogMsg.CANT_FIND_SESSION;

public class Room implements Command {
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";
    private final String SESSION_ID_PARAM = "sessionId";
    private final String SHOW_SESSION = "showSession";
    private final String CUR_DATE = "curDate";

    private static Logger log = LogGen.getInstance();
    private SessionService sessionService;

    public Room(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Optional<Role> role = Optional.ofNullable(((User) request.getSession().getAttribute(SESSION_USER)).getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));

        sessionService.setDaoLocale(locale);
        String sessionIdParam = request.getParameter(SESSION_ID_PARAM);
        int sessionId;

        LocalDate date = LocalDate.now();
        request.setAttribute(CUR_DATE, date);

        if (Optional.ofNullable(sessionIdParam).isPresent() && sessionIdParam.matches("[0-9]+")) {
            sessionId = Integer.parseInt(sessionIdParam);
            try {
                Session session = sessionService.getSessionById(sessionId);
                request.setAttribute(SHOW_SESSION, session);
            } catch (DaoException e) {
                log.warn(CANT_FIND_SESSION, e);
            }
        } else {
            return "forward:/WEB-INF/util/forbidden.jsp" +
                    (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        }
        return "forward:/WEB-INF/" + role.orElse(Role.GUEST).toString() + "/room.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }
}
