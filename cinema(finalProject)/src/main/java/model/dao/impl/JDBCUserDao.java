package model.dao.impl;

import model.dao.AbstractDao;
import model.dao.UserDao;
import model.dao.exceptions.DaoException;
import model.dao.mappers.*;
import model.entity.*;
import model.util.LogGen;
import model.util.Role;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

import static model.util.LogMsg.*;
import static model.util.LogMsg.CANT_CLOSE_CONNECTION;

public class JDBCUserDao extends AbstractDao implements UserDao {
    private Connection connection;
    private Logger log = LogGen.getInstance();

    JDBCUserDao(Connection connection) {
        this.connection = connection;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>(20);
        String sqlQuery = "SELECT * FROM cinema_final.users;";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            log.debug(PREP_STAT_OPENED + " in UserDao getAll()");

            try (ResultSet resultSet = prepStatement.executeQuery();){

                log.debug(QUERY_EXECUTED + " in UserDao getAll()");

                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt(1));
                    user.setUsername(resultSet.getString(2));
                    user.setEmail(resultSet.getString(3));
                    user.setPassword(resultSet.getString(4));
                    user.setRole(Role.valueOf(resultSet.getString(5)));
                    list.add(user);
                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return list;
    }

    public User update(User entity) throws DaoException {
        String sqlQuery = "UPDATE `cinema_final`.`users` t " +
                "SET t.`id` = ?, " +
                "t.`username` = ?, " +
                "t.`email` = ?, " +
                "t.`password` = ?, " +
                "t.`role` = ? " +
                "WHERE t.`id` = ?";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            prepStatement.setInt(1, entity.getId());
            prepStatement.setString(2, entity.getUsername());
            prepStatement.setString(3, entity.getEmail());
            prepStatement.setString(4, entity.getPassword());
            prepStatement.setString(5, entity.getRole().getString());
            prepStatement.setInt(6, entity.getId());
            log.debug(PREP_STAT_OPENED + " in UserDao update()");
            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in UserDao update()");
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_UPDATE, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return entity;
    }

    public User getEntityById(Integer id) throws DaoException {
        String sqlQuery = "SELECT * FROM cinema_final.users WHERE id = ?;";
        User user = new User();

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            prepStatement.setInt(1, id);
            log.debug(PREP_STAT_OPENED + " in UserDao getEntityById()");

            try (ResultSet resultSet = prepStatement.executeQuery();){

                log.debug(QUERY_EXECUTED + " in UserDao getEntityById()");

                if (!resultSet.isBeforeFirst()) {
                    throw new DaoException("No such entry in the DB");
                }

                while (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                    user.setUsername(resultSet.getString(2));
                    user.setEmail(resultSet.getString(3));
                    user.setPassword(resultSet.getString(4));
                    user.setRole(Role.contains(resultSet.getString(5)));
                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return user;
    }

    public User getEntityByUsername(String name) throws DaoException {
        String sqlQuery = "SELECT *\n" +
                "FROM cinema_final.users AS u\n" +
                "       LEFT JOIN cinema_final.tickets AS t ON t.user_id = u.id\n" +
                "       LEFT JOIN cinema_final.sessions AS s ON t.session_id = s.id\n" +
                "       LEFT JOIN cinema_final.movies AS m ON s.movie_id = m.id\n" +
                "       LEFT JOIN cinema_final.days AS d ON d.id = s.day_id\n" +
                "       LEFT JOIN cinema_final.days_translate as dt on dt.day_id = d.id\n" +
                "       LEFT JOIN cinema_final.movies_translate as mt on mt.movie_id = m.id\n" +
                "       LEFT JOIN cinema_final.languages as l ON l.id = mt.lang_id && l.id = dt.lang_id\n" +
                "WHERE u.username = \'" + name + "\' && l.lang_tag = \'" + super.getLocale() + "\' || " +
                "u.username = \'" + name + "\' && t.id IS NULL\n" +
                "ORDER BY d.id, s.time;";
        return getUserFromDB(name, sqlQuery);
    }

    public User getEntityByEmail(String email) throws DaoException {
        String sqlQuery = "SELECT *\n" +
                "FROM cinema_final.users AS u\n" +
                "       LEFT JOIN cinema_final.tickets AS t ON t.user_id = u.id\n" +
                "       LEFT JOIN cinema_final.sessions AS s ON t.session_id = s.id\n" +
                "       LEFT JOIN cinema_final.movies AS m ON s.movie_id = m.id\n" +
                "       LEFT JOIN cinema_final.days AS d ON d.id = s.day_id\n" +
                "       LEFT JOIN cinema_final.days_translate as dt on dt.day_id = d.id\n" +
                "       LEFT JOIN cinema_final.movies_translate as mt on mt.movie_id = m.id\n" +
                "       LEFT JOIN cinema_final.languages as l ON l.id = mt.lang_id && l.id = dt.lang_id\n" +
                "WHERE u.email = \'" + email + "\' && l.lang_tag = \'" + super.getLocale() + "\'\n" +
                "ORDER BY d.id, s.time;";
        return getUserFromDB(email, sqlQuery);
    }

    public void delete(Integer id) throws DaoException {
        String sqlQuery1 = "DELETE FROM `cinema_final`.`users` WHERE `id` = ?";
        String sqlQuery2 = "DELETE FROM `cinema_final`.`tickets` WHERE `user_id` = ?";
        String sqlQuery3 = "SELECT * FROM `cinema_final`.`tickets` WHERE `user_id` = ?";

        try (PreparedStatement prepStatement1 = connection.prepareStatement(sqlQuery1);
             PreparedStatement prepStatement2 = connection.prepareStatement(sqlQuery2);
             PreparedStatement prepStatement3 = connection.prepareStatement(sqlQuery3);){
            connection.setAutoCommit(false);
            log.debug(PREP_STAT_OPENED + " in UserDao delete()");

            /*try (ResultSet resultSet = prepStatement3.executeQuery();){

                log.debug(QUERY_EXECUTED + " in UserDao delete()");

                if (resultSet.isBeforeFirst()) {
                    prepStatement2.execute();
                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }*/

            prepStatement2.execute();
            prepStatement1.execute();
            log.debug(QUERY_EXECUTED + " in UserDao delete()");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.warn(SQL_EXCEPTION_WHILE_ROLLBACK, e);
            }
            log.error(SQL_EXCEPTION_WHILE_DELETING, e);
        }
    }

    public void create(User entity) throws DaoException {
        String sqlQuery = "INSERT INTO `cinema_final`.`users` (`username`, `email`, `password`, `role`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            prepStatement.setString(1, entity.getUsername());
            prepStatement.setString(2, entity.getEmail());
            prepStatement.setString(3, entity.getPassword());
            prepStatement.setString(4, entity.getRole().getString());
            log.debug(PREP_STAT_OPENED + " in UserDao create()");

            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in UserDao create()");
            } catch (SQLIntegrityConstraintViolationException e) {
                log.info(SUCH_USER_ALREADY_EXISTS, e);
                throw new DaoException(e.getMessage(), e);
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_CREATE, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
    }

    private User getUserFromDB(String usernameOrEmail, String sqlQuery) throws DaoException {
        User user = new User();

        UserMapper userMapper = new UserMapper();
        TicketMapper ticketMapper = new TicketMapper();
        SessionMapper sessionMapper = new SessionMapper();
        MovieMapper movieMapper = new MovieMapper();
        DayMapper dayMapper = new DayMapper();

        Map<Integer, User> userMap = new HashMap<>();
        Map<Integer, Ticket> ticketMap = new HashMap<>();
        Map<Integer, Session> sessionMap = new HashMap<>();
        Map<Integer, Movie> movieMap = new HashMap<>();
        Map<Integer, Day> dayMap = new HashMap<>();

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            log.debug(PREP_STAT_OPENED + " in UserDao getUserFromDB()");

            try (ResultSet resultSet = prepStatement.executeQuery();){

                log.debug(QUERY_EXECUTED + " in UserDao getUserFromDB()");

                if (!resultSet.isBeforeFirst()) {
                    throw new DaoException("No such entry in the DB");
                }
                while (resultSet.next()) {

                    user = userMapper.extractFromResultSet(resultSet, 1, 2, 4, 3, 5);
                    Ticket ticket = ticketMapper.extractFromResultSet(resultSet, 6, 7, 8, 9, 10);
                    Session session = sessionMapper.extractFromResultSet(resultSet, 11, 12, 13, 14);
                    Day day = dayMapper.extractFromResultSet(resultSet, 17, 21, 22);
                    Movie movie = movieMapper.extractFromResultSet(resultSet, 15, 26, 16);

                    user = userMapper.makeUnique(userMap, user);

                    Optional.ofNullable(ticket).ifPresent((a) -> a = ticketMapper.makeUnique(ticketMap, a));
                    Optional.ofNullable(day).ifPresent((a) -> a = dayMapper.makeUnique(dayMap, a));
                    Optional.ofNullable(movie).ifPresent((a) -> a = movieMapper.makeUnique(movieMap, a));

                    session.setDay(day);
                    session.setMovie(movie);
                    Optional.ofNullable(session).ifPresent((a) -> a = sessionMapper.makeUnique(sessionMap, a));
                    Optional.ofNullable(ticket).ifPresent((a) -> ticket.setSession(session));

                    user.getUserTickets().add(ticket);

                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return user;
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
