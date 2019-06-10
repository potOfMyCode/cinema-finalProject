package dao;

import model.dao.DaoFactory;
import model.dao.MovieDao;
import model.dao.TicketDao;
import model.dao.exceptions.DaoException;
import model.entity.Ticket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDBCTicketDaoTest {
    private TicketDao factory;

    @Before
    public void init(){
        factory = DaoFactory.getInstance().createTicketDao();
    }

    @After
    public void after() {
        factory.close();
    }

    @Test
    public void getByUserIdTest() throws DaoException {
        assertNotNull(factory.getByUserId(5));

        List<Ticket> ticketList = new LinkedList<>();
        ticketList = factory.getByUserId(5);

        for (Ticket ticket : ticketList) {
            System.out.println("Ticket getId         - " + ticket.getId());
            System.out.println("Ticket getPlace      - " + ticket.getPlace());
            System.out.println("Ticket getUserID     - " + ticket.getUserID());
            System.out.println("Ticket getSessionID  - " + ticket.getSessionID());

            System.out.println("    Session getId             - " + ticket.getSession().getId());
            System.out.println("    Session getTimeHoursMins  - " + ticket.getSession().getTimeHoursMins());
            System.out.println("    Session getDayID          - " + ticket.getSession().getDayID());

            System.out.println("            Movie getId       - " + ticket.getSession().getMovie().getId());
            System.out.println("            Movie getName     - " + ticket.getSession().getMovie().getName());
            System.out.println("            Movie getPic     - " + ticket.getSession().getMovie().getPicUrl());

            System.out.println("            Day getId         - " + ticket.getSession().getDay().getId());
            System.out.println("            Day getName       - " + ticket.getSession().getDay().getName());
            System.out.println("            Day getShortName  - " + ticket.getSession().getDay().getShortName());


        }
    }

    @Test
    public void getByUserIdlForAdminTest() throws DaoException {
        assertTrue(factory.getByUserId(1).isEmpty());
    }

    @Test(expected = DaoException.class)
    public void createTest() throws DaoException {
        Ticket ticket = new Ticket();
        ticket.setPlace(1);
        ticket.setUserID(100);
        ticket.setSessionID(1);
        LocalDate date = LocalDate.now();
        ticket.setDate(date);

        factory.create(ticket);
    }

    @Test
    public void deleteTest() throws DaoException {
    }
}
