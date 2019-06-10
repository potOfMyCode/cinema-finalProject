package controller.filters;

import model.entity.User;
import model.util.Role;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RolesFilter implements Filter {
    private final String SESSION_USER = "sessionUser";

    private Map<Role, Set<String>> ways;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ways = new HashMap<>();
        ways.put(Role.GUEST, Stream.of(
                "/",
                "/login",
                "/registration",
                "/go_login",
                "/go_registration",
                "/showtimes",
                "/now_playing",
                "/room",
                "/operation_success"
        ).collect(Collectors.toSet()));

        ways.put(Role.USER, Stream.of(
                "/",
                "/logout",
                "/showtimes",
                "/now_playing",
                "/room",
                "/order",
                "/tickets",
                "/remticket"
        ).collect(Collectors.toSet()));

        ways.put(Role.ADMIN, Stream.of(
                "/",
                "/logout",
                "/edit",
                "/showtimes",
                "/now_playing",
                "/room",
                "/remsession",
                "/remmovie",
                "/addsession",
                "/addmovie"
        ).collect(Collectors.toSet()));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().replace(request.getContextPath(), "").replace(request.getServletPath(), "");

        if (request.getSession().getAttribute(SESSION_USER) == null) {
            request.getSession().setAttribute(SESSION_USER, User.getGuestInst());
        }
        Role requestRole = ((User) request.getSession().getAttribute(SESSION_USER)).getRole();

        if (!ways.get(requestRole).contains(path)) {
            request.getRequestDispatcher("/WEB-INF/util/forbidden.jsp").forward(request, response);
        }

        System.out.println("We are in ROLES FILTER");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
