package model.dao.impl;

import model.dao.AbstractDao;
import model.dao.TicketDao;
import model.dao.exceptions.DaoException;
import model.dao.mappers.DayMapper;
import model.dao.mappers.MovieMapper;
import model.dao.mappers.SessionMapper;
import model.dao.mappers.TicketMapper;
import model.entity.Day;
import model.entity.Movie;
import model.entity.Session;
import model.entity.Ticket;
import model.util.LogGen;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static model.util.LogMsg.*;

public class JDBCTicketDao extends AbstractDao implements TicketDao {
    private Connection connection;
    private Logger log = LogGen.getInstance();

    public JDBCTicketDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Ticket> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ticket update(Ticket entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ticket getEntityById(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer id) throws DaoException {
        String sqlQuery = "DELETE FROM `cinema_final`.`tickets` WHERE `id` = " + id;

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            log.debug(PREP_STAT_OPENED + " in TicketDao delete()");
            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in TicketDao delete()");
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_DELETING, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
    }

    @Override
    public void create(Ticket entity) throws DaoException {
        String sqlQuery = "INSERT INTO `cinema_final`.`tickets` (`place`, `user_id`, `session_id`, `date`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            prepStatement.setInt(1, entity.getPlace());
            prepStatement.setInt(2, entity.getUserID());
            prepStatement.setInt(3, entity.getSessionID());
            prepStatement.setDate(4, Date.valueOf(entity.getDate()));
            log.debug(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS + " in TicketDao create()");
            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in TicketDao create()");
            } catch (SQLIntegrityConstraintViolationException e) {
                log.error(CANT_CREATE_TICKET, e);
                throw new DaoException(e.getMessage(), e);
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_CREATE, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
    }

    @Override
    public List<Ticket> getByUserId(int userId) {
        List<Ticket> ticketList = new LinkedList<>();

        MovieMapper movieMapper = new MovieMapper();
        SessionMapper sessionMapper = new SessionMapper();
        DayMapper dayMapper = new DayMapper();
        TicketMapper ticketMapper = new TicketMapper();

        Map<Integer, Movie> moviesMap = new HashMap<>();
        Map<Integer, Session> sessionsMap = new HashMap<>();
        Map<Integer, Day> daysMap = new HashMap<>();
        Map<Integer, Ticket> ticketMap = new HashMap<>();

        String sqlQuery = "SELECT t.id,\n" +
                "       t.place,\n" +
                "       t.user_id,\n" +
                "       t.session_id,\n" +
                "       t.date,\n" +
                "       s.id,\n" +
                "       s.time,\n" +
                "       s.day_id,\n" +
                "       s.movie_id,\n" +
                "       d.id,\n" +
                "       dt.day_name,\n" +
                "       dt.day_name_short,\n" +
                "       m.id,\n" +
                "       mt.movie_name,\n" +
                "       m.pic_url\n" +
                "FROM cinema_final.tickets AS t\n" +
                "            LEFT JOIN cinema_final.users AS u ON t.user_id = u.id\n" +
                "            LEFT JOIN cinema_final.sessions AS s ON t.session_id = s.id\n" +
                "            LEFT JOIN cinema_final.movies AS m ON s.movie_id = m.id\n" +
                "            LEFT JOIN cinema_final.days AS d ON d.id = s.day_id\n" +
                "            LEFT JOIN cinema_final.days_translate as dt on dt.day_id = d.id\n" +
                "            LEFT JOIN cinema_final.movies_translate as mt on mt.movie_id = m.id\n" +
                "            LEFT JOIN cinema_final.languages as l ON l.id = mt.lang_id && l.id = dt.lang_id\n" +
                "WHERE l.lang_tag = \'" + super.getLocale().toLanguageTag() + "\' && t.user_id = \'" + userId + "\'\n" +
                "ORDER BY t.date desc, d.id, s.time";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            log.debug(PREP_STAT_OPENED + " in TicketDao getByUserId()");

            try (ResultSet resultSet = prepStatement.executeQuery()){

                log.debug(QUERY_EXECUTED + " in TicketDao getByUserId()");

                while (resultSet.next()) {
                    Ticket ticket = ticketMapper.extractFromResultSet(resultSet, 1, 2, 3, 4, 5);
                    Session session = sessionMapper.extractFromResultSet(resultSet, 6, 7, 8, 9);
                    Day day = dayMapper.extractFromResultSet(resultSet, 10, 11, 12);
                    Movie movie = movieMapper.extractFromResultSet(resultSet, 13, 14, 15);

                    ticket = ticketMapper.makeUnique(ticketMap, ticket);
                    day = dayMapper.makeUnique(daysMap, day);
                    movie = movieMapper.makeUnique(moviesMap, movie);

                    session.setDay(day);
                    session.setMovie(movie);
                    session = sessionMapper.makeUnique(sessionsMap, session);

                    ticket.setSession(session);
                    ticketList.add(ticket);
                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return ticketList;
    }

    @Override
    public void close() {
        try {
            connection.close();
            log.debug(CONNECTION_CLOSED);
        } catch (SQLException e) {
            log.error(CANT_CLOSE_CONNECTION, e);
        }
    }
}
