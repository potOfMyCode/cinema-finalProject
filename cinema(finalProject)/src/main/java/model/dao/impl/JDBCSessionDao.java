package model.dao.impl;

import model.dao.AbstractDao;
import model.dao.SessionDao;
import model.dao.exceptions.DaoException;
import model.dao.mappers.*;
import model.entity.*;
import model.util.LogGen;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static model.util.LogMsg.*;
import static model.util.LogMsg.CANT_CLOSE_CONNECTION;

public class JDBCSessionDao extends AbstractDao implements SessionDao {
    private Connection connection;
    private Logger log = LogGen.getInstance();

    JDBCSessionDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Session> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Session update(Session entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Session getEntityById(Integer id) throws DaoException {
        Session session = new Session();

        DayMapper dayMapper = new DayMapper();
        MovieMapper movieMapper = new MovieMapper();
        SessionMapper sessionMapper = new SessionMapper();
        TicketMapper ticketMapper = new TicketMapper();
        UserMapper userMapper = new UserMapper();

        Map<Integer, Day> daysMap = new HashMap<>();
        Map<Integer, Movie> moviesMap = new HashMap<>();
        Map<Integer, Session> sessionsMap = new HashMap<>();
        Map<Integer, Ticket> ticketMap = new HashMap<>();

        String sqlQuery = "SELECT *\n" +
                "FROM cinema_final.sessions AS s\n" +
                "       LEFT JOIN cinema_final.days AS d ON s.day_id = d.id\n" +
                "       LEFT JOIN cinema_final.movies AS m ON m.id = s.movie_id\n" +
                "       LEFT JOIN cinema_final.days_translate as dt on dt.day_id = d.id\n" +
                "       LEFT JOIN cinema_final.movies_translate as mt on mt.movie_id = m.id\n" +
                "       LEFT JOIN cinema_final.tickets AS t ON t.session_id = s.id\n" +
                "       LEFT JOIN cinema_final.users AS u ON u.id = t.user_id\n" +
                "       LEFT JOIN cinema_final.languages as l ON l.id = mt.lang_id && l.id = dt.lang_id\n" +
                "WHERE l.lang_tag = \'" + super.getLocale() + "\' && s.id = " + id + "\n" +
                "ORDER BY t.place;";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            log.debug(PREP_STAT_OPENED + " in SessionDao getEntityById()");
            try (ResultSet resultSet = prepStatement.executeQuery();){

                log.debug(QUERY_EXECUTED + " in SessionDao getEntityById()");

                if (!resultSet.isBeforeFirst()) {
                    log.info(NO_SUCH_ENTRY_IN_DB + "in SessionDao getEntityById()");
                    throw new DaoException("No such session with id (" + id + ") in the DB");
                }

                while (resultSet.next()) {
                    Ticket ticket;
                    session = sessionMapper.extractFromResultSet(resultSet, 1, 2, 3, 4);
                    session.setDay(dayMapper.makeUnique(daysMap, dayMapper.extractFromResultSet(resultSet, 5, 9, 10)));
                    session.setMovie(movieMapper.makeUnique(moviesMap, movieMapper.extractFromResultSet(resultSet, 6, 14, 7)));
                    session = sessionMapper.makeUnique(sessionsMap, session);

                    System.out.println(session + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    User user = userMapper.extractFromResultSet(resultSet, 22, 23, 24, 25, 26);

                    if (user != null) {
                        ticket = ticketMapper.extractFromResultSet(resultSet, 17, 18, 19, 20, 21);
                        ticket.setOwner(user);
                        ticket.setSession(session);
                        ticket = ticketMapper.makeUnique(ticketMap, ticket);
                        Optional.ofNullable(ticket).ifPresent(session.getTicketList()::add);
                    }
                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return session;
    }

    @Override
    public void delete(Integer id) throws DaoException {
        String sqlQuery1 = "DELETE FROM `cinema_final`.`sessions` WHERE `id` = " + id;
        String sqlQuery2 = "DELETE FROM `cinema_final`.`tickets` WHERE `session_id` = " + id;

        try (PreparedStatement prepStatement1 = connection.prepareStatement(sqlQuery1);
             PreparedStatement prepStatement2 = connection.prepareStatement(sqlQuery2)){

            log.debug(PREP_STAT_OPENED + " in SessionDao delete()");
            try {
                connection.setAutoCommit(false);
                prepStatement2.execute();
                prepStatement1.execute();
                log.debug(QUERY_EXECUTED + " in SessionDao delete()");
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                log.error(SQL_EXCEPTION_WHILE_DELETING, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
    }

    @Override
    public void create(Session entity) throws DaoException {
        String sqlQuery = "INSERT INTO `cinema_final`.`sessions` (`time`, `day_id`, `movie_id`) VALUES (?, ?, ?)";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){
            prepStatement.setTime(1, entity.getTime());
            prepStatement.setInt(2, entity.getDayID());
            prepStatement.setInt(3, entity.getMovieID());
            log.debug(PREP_STAT_OPENED + " in SessionDao create()");
            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in SessionDao create()");
            } catch (SQLIntegrityConstraintViolationException e) {
                log.error(CANT_CREATE_SESSION, e);
                throw new DaoException(e.getMessage(), e);
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_CREATE, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
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