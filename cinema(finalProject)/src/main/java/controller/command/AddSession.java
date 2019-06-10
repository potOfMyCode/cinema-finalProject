package controller.command;

import model.entity.Session;
import model.entity.User;
import model.service.SessionService;
import model.util.Languages;
import model.util.LogGen;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Time;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.util.LogMsg.INVALID_DATA_SESSION_CREATION;
import static model.util.LogMsg.SESSION_CREATED_SUCCESSFULLY;

public class AddSession implements Command{
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";
    private final String LOCAL_RB_BASE_NAME = "lang";
    private final String MESSAGE = "msg";
    private final String MOVIE_ID = "movieId";
    private final String TIME_HOURS_ID = "hours";
    private final String TIME_MINS_ID = "mins";
    private final String DAY_ID_PARAMETER = "day";

    private static Logger log = LogGen.getInstance();
    private SessionService sessionService;

    public AddSession(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User curUser = (User) request.getSession().getAttribute(SESSION_USER);
        Optional<Object> role = Optional.ofNullable(curUser.getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));
        ResourceBundle rsBundle = ResourceBundle.getBundle(LOCAL_RB_BASE_NAME, locale);

        String outUrlOK = "redirect:" + request.getContextPath() + request.getServletPath() + "/showtimes" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        String outUrlInvalid = "forward:/WEB-INF/common/oper_fail.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());

        sessionService.setDaoLocale(locale);
        String dayIdParam = request.getParameter(DAY_ID_PARAMETER);
        String movieIdParam = request.getParameter(MOVIE_ID);
        String timeHours = request.getParameter(TIME_HOURS_ID);
        String timeMins = request.getParameter(TIME_MINS_ID);

        if (invalidData(dayIdParam, movieIdParam, timeHours, timeMins)) {
            request.setAttribute(MESSAGE, rsBundle.getString("wrong.data"));
            log.info(INVALID_DATA_SESSION_CREATION);
            return outUrlInvalid;
        }

        int dayId = Integer.parseInt(dayIdParam);
        int movieId = Integer.parseInt(movieIdParam);
        int hours = Integer.parseInt(timeHours);
        int mins = Integer.parseInt(timeMins);

        Time sessionTime = createTime(hours, mins);
        Session session = generateSession(dayId, movieId, sessionTime);
        sessionService.createSession(session);

        log.info(SESSION_CREATED_SUCCESSFULLY);
        return outUrlOK;
    }

    private Time createTime(int hours, int mins) {
        return new Time((long) hours * 60 * 60 * 1000 + mins * 60 * 1000);
    }

    private Session generateSession(int dayId, int movieId, Time time) {
        Session session = new Session();
        session.setDayID(dayId);
        session.setMovieID(movieId);
        session.setTime(time);
        return session;
    }

    private boolean invalidData(String dayID, String movID, String hours, String mins) {
        return !Optional.ofNullable(dayID).isPresent() ||
                !Optional.ofNullable(movID).isPresent() ||
                !Optional.ofNullable(hours).isPresent() ||
                !Optional.ofNullable(mins).isPresent() ||
                !dayID.matches("[0-9]+") ||
                !movID.matches("[0-9]+") ||
                !hours.matches("[0-9]+") ||
                !mins.matches("[0-9]+");
    }
}
