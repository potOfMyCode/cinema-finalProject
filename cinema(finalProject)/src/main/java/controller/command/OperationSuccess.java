package controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OperationSuccess implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "forward:/WEB-INF/util/operation_success.jsp" +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }
}
