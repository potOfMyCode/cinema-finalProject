package controller.command;

import model.entity.Movie;
import model.entity.User;
import model.service.MovieService;
import model.util.Languages;
import model.util.LogGen;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class NowPlaying implements Command {
    private final String MOVIES_BEAN = "moviesBean";
    private final String SERVLET_CONTEXT = "servletContext";
    private final String SESSION_USER = "sessionUser";
    private final String CUR_LANG = "curLang";

    private static Logger log = LogGen.getInstance();
    private MovieService movieService;

    public NowPlaying(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Optional<Object> role = Optional.ofNullable(((User) request.getSession().getAttribute(SESSION_USER)).getRole());
        String localeTag = Optional.ofNullable((String) request.getSession().getAttribute(CUR_LANG)).orElse("en");
        Locale locale = Locale.forLanguageTag(Languages.isLangOrGetDefault(localeTag));
        movieService.setDaoLocale(locale);

        List<Movie> moviesBank = movieService.getAllMovies();
        request.setAttribute(MOVIES_BEAN, moviesBank);
        request.setAttribute(SERVLET_CONTEXT, request.getServletPath());

        int page = 1;
        int recordsPerPage =1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));

        int noOfRecords = moviesBank.size();
        moviesBank = generateNeedsRecords(moviesBank, (page-1)*recordsPerPage, (page-1)*recordsPerPage+1);

        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        request.setAttribute("moviesForShow", moviesBank);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);

        System.out.println("requset in now playing: " + request);
        System.out.println("requset.getQueryString() in now playing: " + request.getQueryString());

        return role.map(o -> "forward:/WEB-INF/" + o.toString() + "/now_playing.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString()))
                .orElse("forward:/login");
    }

    private List<Movie> generateNeedsRecords(List<Movie> list, int from, int to){
        return list.subList(from, to);
    }
}
