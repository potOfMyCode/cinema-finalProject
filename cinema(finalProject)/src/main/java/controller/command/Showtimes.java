package controller.command;

import model.entity.Day;
import model.entity.Movie;
import model.entity.User;
import model.service.DayService;
import model.service.MovieService;
import model.util.Languages;
import model.util.LogGen;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Showtimes implements Command {
    private final String DAY_ID_PARAMETER = "day";
    private final String MOVIES_BEAN = "moviesBean";
    private final String SERVLET_CONTEXT = "servletContext";
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";

    private static Logger log = LogGen.getInstance();
    private DayService dayService;
    private MovieService movieService;

    public Showtimes(DayService dayService, MovieService movieService) {
        this.dayService = dayService;
        this.movieService = movieService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Optional<Object> role = Optional.ofNullable(((User) request.getSession().getAttribute(SESSION_USER)).getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));

        dayService.setDaoLocale(locale);
        movieService.setDaoLocale(locale);
        String dayIdParameter = request.getParameter(DAY_ID_PARAMETER);
        int dayID = 1;

        if (Optional.ofNullable(dayIdParameter).isPresent() && dayIdParameter.matches("[0-9]+")) {
            dayID = Integer.parseInt(dayIdParameter);
        }

        Day day = dayService.getDayById(dayID);
        List<Movie> moviesAll = movieService.getAllMovies();

        request.setAttribute(DAY_ID_PARAMETER, day);
        request.setAttribute(MOVIES_BEAN, moviesAll);
        request.setAttribute(SERVLET_CONTEXT, request.getServletPath());

        System.out.println("requset in showtimes: " + request);
        System.out.println("requset.getQueryString() in showtimes: " + request.getQueryString());

        return role.map(o -> "forward:/WEB-INF/" + o.toString() + "/showtimes.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString()))
                .orElse("forward:/login");
    }
}