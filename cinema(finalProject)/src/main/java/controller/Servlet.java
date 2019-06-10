package controller;

import controller.command.*;
import model.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Servlet extends HttpServlet {
    private final String CUR_REQ_URL = "curReqURL";

    private Map<String, Command> commandsMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        commandsMap.put("/", new Home());
        commandsMap.put("/login", new Login(new UserService()));
        commandsMap.put("/go_login", new GoLogin());
        commandsMap.put("/logout", new Logout());
        commandsMap.put("/registration", new Register(new UserService()));
        commandsMap.put("/go_registration", new GoRegister());
        commandsMap.put("/now_playing", new NowPlaying(new MovieService()));
        commandsMap.put("/showtimes", new Showtimes(new DayService(), new MovieService()));
        commandsMap.put("/room", new Room(new SessionService()));
        commandsMap.put("/addmovie", new AddMovie(new MovieService()));
        commandsMap.put("/remmovie", new RemoveMovie(new MovieService()));
        commandsMap.put("/addsession", new AddSession(new SessionService()));
        commandsMap.put("/remsession", new RemoveSession(new SessionService()));
        commandsMap.put("/order", new PurchaseTicket(new TicketService()));
        commandsMap.put("/tickets", new MyTickets(new TicketService()));
        commandsMap.put("/remticket", new RemoveTicket(new TicketService()));
        commandsMap.put("/operation_success", new OperationSuccess());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = req.getPathInfo();
        System.out.println("req.getPathInfo(): " + req.getPathInfo());
        req.setAttribute(CUR_REQ_URL, req.getRequestURL());
        System.out.println("req.getRequestURL(): " + req.getRequestURL());
        Command command = commandsMap.get(path);
        String page = command.execute(req, resp);

        if (page.contains("redirect")) {
            resp.sendRedirect(page.replace("redirect:", ""));
        } else if (page.contains("forward")) {
            req.getRequestDispatcher(page.replace("forward:", "")).forward(req, resp);
        }
    }
}
