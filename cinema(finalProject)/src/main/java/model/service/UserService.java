package model.service;

import model.dao.DaoFactory;
import model.dao.UserDao;
import model.dao.exceptions.DaoException;
import model.entity.User;
import model.service.exceptions.ServiceException;
import model.util.LogGen;
import model.util.Regexps;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

import static model.util.LogMsg.EXCEPTION_WRITE_RESPONSE;
import static model.util.LogMsg.USER_WAS_LOGGED_IN;


public class UserService {
    private String SESSION_USER = "sessionUser";

    private Logger log = LogGen.getInstance();
    private String usernameRegex = Regexps.USERNAME_REGEX;
    private String emailRegex = Regexps.MAIL_REGEX;
    private String passRegex = Regexps.PASSWORD_REGEX;

    private DaoFactory daoFactory = DaoFactory.getInstance();

    public User login(String usernameOrMail) throws DaoException {
        User user = new User();

        try (UserDao dao = daoFactory.createUserDao()) {
            if (usernameOrMail.matches(emailRegex)) {
                user = dao.getEntityByEmail(usernameOrMail);
            } else if (usernameOrMail.matches(usernameRegex)) {
                user = dao.getEntityByUsername(usernameOrMail);
            }
        }
        log.info(USER_WAS_LOGGED_IN);
        return user;
    }

    public void authorize(User user, String password, HttpServletRequest request) throws ServiceException {
        if (password.equals(user.getPassword())) {
            request.getSession().setAttribute(SESSION_USER, user);
        } else {
            throw new ServiceException("User: (username=" + user.getUsername() + ", password=" + user.getPassword() +
                    ") Couldn't authorize with password: " + password);
        }
    }

    public void register(User user) throws DaoException {
        try (UserDao dao = daoFactory.createUserDao()) {
            dao.create(user);
        }
    }

    public void setResponseStatus(int status, String msg, HttpServletResponse response) {
        response.setStatus(status);
        try {
            response.getWriter().write(msg);
        } catch (IOException e) {
            System.out.println("EXCEPTION_WRITE_RESPONSE!!!!");
            log.error(EXCEPTION_WRITE_RESPONSE, e);
        }
    }

    public boolean validate(String usernameOrMail, String password) {
        return usernameOrMail != null && !usernameOrMail.isEmpty() && password != null && !password.isEmpty();
    }

    public boolean ifInvalidRegData(String username, String email, String pass, String confPass) {
        return !username.matches(usernameRegex) || !email.matches(emailRegex) || !pass.matches(passRegex) || !pass.equals(confPass);
    }

    public String getFaultRegistrationReason(String username, String email, String pass, String confPass, ResourceBundle resBundle) {
        String faultReson = "no fault";
        if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confPass.isEmpty()) {
            faultReson = resBundle.getString("invalid.fillAll");
        } else if (!username.matches(usernameRegex)) {
            faultReson = resBundle.getString("registr.bad.username");
        } else if (!email.matches(emailRegex)) {
            faultReson = resBundle.getString("registr.bad.email");
        } else if (!pass.matches(passRegex)) {
            faultReson = resBundle.getString("registr.bad.password");
        } else if (!pass.equals(confPass)) {
            faultReson = resBundle.getString("registr.confirm.pass");
        }
        return faultReson;
    }
}
