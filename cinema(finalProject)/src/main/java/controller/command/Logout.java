package controller.command;

import model.entity.User;
import model.service.UserService;
import model.util.LogGen;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static model.util.LogMsg.USER_LOGGED_OUT;

public class Logout implements Command{
    private final String SESSION_USER = "sessionUser";

    private static Logger log = LogGen.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(SESSION_USER, User.getGuestInst());
        log.info(USER_LOGGED_OUT + " : " + ((User)request.getSession().getAttribute(SESSION_USER)).getUsername());

        return "redirect:" + request.getContextPath() + request.getServletPath() + "/" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }
}
