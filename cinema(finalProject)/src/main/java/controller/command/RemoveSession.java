package controller.command;

import model.entity.User;
import model.service.SessionService;
import model.util.Languages;
import model.util.LogGen;
import model.util.Role;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class RemoveSession implements Command{
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";
    private final String LOCAL_RB_BASE_NAME = "lang";
    private final String MESSAGE = "msg";
    private final String SESSION_ID_PARAM = "sessionId";

    private static Logger log = LogGen.getInstance();
    private SessionService sessionService;

    public RemoveSession(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User sessionUser = (User) request.getSession().getAttribute(SESSION_USER);
        Optional<Role> role = Optional.ofNullable((sessionUser).getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));
        ResourceBundle rsBundle = ResourceBundle.getBundle(LOCAL_RB_BASE_NAME, locale);

        sessionService.setDaoLocale(locale);
        String outUrlOK = "redirect:" + request.getContextPath() + request.getServletPath() + "/showtimes" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        String outUrlInvalid = "forward:/WEB-INF/util/operation_fail.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());

        String sessionIdParam = request.getParameter(SESSION_ID_PARAM);
        int sessionId;

        if (invalidInput(sessionIdParam)) {
            request.setAttribute(MESSAGE, rsBundle.getString("del.ticket.wrong.input"));
            return outUrlInvalid;
        } else {
            sessionId = Integer.parseInt(sessionIdParam);
        }

        sessionService.removeSessionById(sessionId);
        return outUrlOK;
    }

    private boolean invalidInput(String sessionID) {
        return !Optional.ofNullable(sessionID).isPresent() || !sessionID.matches("[0-9]+");
    }
}
