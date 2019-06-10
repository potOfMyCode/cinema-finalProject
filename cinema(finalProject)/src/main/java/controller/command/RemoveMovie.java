package controller.command;

import model.entity.User;
import model.service.MovieService;
import model.util.Languages;
import model.util.LogGen;
import model.util.Role;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class RemoveMovie implements Command{
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";
    private final String LOCAL_RB_BASE_NAME = "lang";
    private final String MESSAGE = "msg";
    private final String MOVIE_ID = "movieId";

    private static Logger log = LogGen.getInstance();
    private MovieService movieService;

    public RemoveMovie(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User sessionUser = (User) request.getSession().getAttribute(SESSION_USER);
        Optional<Role> role = Optional.ofNullable((sessionUser).getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));
        ResourceBundle rsBundle = ResourceBundle.getBundle(LOCAL_RB_BASE_NAME, locale);

        movieService.setDaoLocale(locale);
        String outUrlOK = role.map(o -> "forward:/WEB-INF/" + o.toString() + "/index.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString()))
                .orElse("forward:/login");
        String outUrlInvalid = "redirect:/WEB-INF/util/operation_fail.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());

        String movIdParam = request.getParameter(MOVIE_ID);
        int movId;

        if (invalidInput(movIdParam)) {
            request.setAttribute(MESSAGE, rsBundle.getString("del.ticket.wrong.input"));
            return outUrlInvalid;
        } else {
            movId = Integer.parseInt(movIdParam);
        }

        movieService.removeMovieById(movId);
        return outUrlOK;
    }

    private boolean invalidInput(String movieID) {
        return !Optional.ofNullable(movieID).isPresent() || !movieID.matches("[0-9]+");
    }
}
