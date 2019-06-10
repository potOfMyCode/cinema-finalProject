package controller.command;

import model.dao.exceptions.DaoException;
import model.entity.User;
import model.service.UserService;
import model.service.exceptions.ServiceException;
import model.util.Languages;
import model.util.LogGen;
import model.util.Role;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.util.LogMsg.*;

public class Register implements Command{
    private final String LOCAL_RB_BASE_NAME = "lang";
    private final String CUR_LANG = "curLang";
    private final String USERNAME_PARAM = "username";
    private final String EMAIL_PARAM = "email";
    private final String PASSWORD_PARAM = "password";
    private final String CONFIRM_PASS_PARAM = "confirmPassword";
    private final String MESSAGE2 = "msg2";

    private static Logger log = LogGen.getInstance();
    private UserService userService;

    public Register(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(LOCAL_RB_BASE_NAME,
                Locale.forLanguageTag((String) request.getSession().getAttribute(CUR_LANG)));

        String username = request.getParameter(USERNAME_PARAM.trim());
        String mail = request.getParameter(EMAIL_PARAM.trim());
        String password = request.getParameter(PASSWORD_PARAM.trim());
        String confirmPassword = request.getParameter(CONFIRM_PASS_PARAM.trim());
        String returnPath = "";

        if (userService.ifInvalidRegData(username, mail, password, confirmPassword)) {
            userService.setResponseStatus(400, userService.getFaultRegistrationReason(username, mail, password,
                    confirmPassword, resourceBundle), response);
            return returnPath;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(mail);
        user.setPassword(password);
        user.setRole(Role.USER);

        String outUrlInvalid = "forward:/WEB-INF/util/operation_fail.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());

        try {
            userService.register(user);

            request.setAttribute(MESSAGE2, resourceBundle.getString("register.success"));

            response.getWriter().write(
                    request.getScheme() + "://" +
                            request.getServerName() +
                            ":" + request.getServerPort() +
                            request.getContextPath() +
                            request.getServletPath() + "/operation_success" +
                            (request.getQueryString() == null ? "" : "?" + request.getQueryString())
            );
            log.info(REGISTERED_SUCCESSFULLY + " : " + user.toString());
        } catch (DaoException e) {
            userService.setResponseStatus(400, resourceBundle.getString("register.already.exists"), response);
            log.error(REGISTER_ALREADY_EXISTS, e);
        } catch (IOException e) {
            log.error(EXCEPTION_WRITE_RESPONSE, e);
        }
        return "";
    }
}
