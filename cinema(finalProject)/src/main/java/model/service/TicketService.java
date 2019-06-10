package model.service;

import model.dao.DaoFactory;
import model.dao.TicketDao;
import model.dao.exceptions.DaoException;
import model.entity.Ticket;
import model.util.LogGen;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;

import static model.util.LogMsg.*;
import static model.util.LogMsg.CANT_REMOVE_TICKET;

public class TicketService {
    private Logger log = LogGen.getInstance();
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public void createTicket(Ticket ticket) throws DaoException {

        try (TicketDao dao = daoFactory.createTicketDao()) {
            dao.create(ticket);
            log.info(TICKET_CREATED);
        }
    }

    public List<Ticket> getTicketsByUserId(int userId) {
        try (TicketDao dao = daoFactory.createTicketDao()) {
            return dao.getByUserId(userId);
        }
    }

    public void removeTicket(Ticket ticket) {
        try (TicketDao dao = daoFactory.createTicketDao()) {
            dao.delete(ticket.getId());
            log.info(TICKET_REMOVED);
        } catch (DaoException e) {
            log.error(CANT_REMOVE_TICKET, e);
        }
    }

    public void setDaoLocale(Locale locale) {
        daoFactory.setDaoLocale(locale);
        log.info(DAO_LOCALE_IS_SET + " for " + daoFactory.getClass().getSimpleName() + " as " + locale.toString());
    }
}
