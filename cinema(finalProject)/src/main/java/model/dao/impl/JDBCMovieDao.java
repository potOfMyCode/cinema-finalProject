package model.dao.impl;

import model.dao.AbstractDao;
import model.dao.MovieDao;
import model.dao.exceptions.DaoException;
import model.dao.mappers.DayMapper;
import model.dao.mappers.MovieMapper;
import model.dao.mappers.SessionMapper;
import model.entity.Day;
import model.entity.Movie;
import model.entity.Session;
import model.util.LogGen;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

import static model.util.LogMsg.*;
import static model.util.LogMsg.CANT_CLOSE_CONNECTION;

public class JDBCMovieDao extends AbstractDao implements MovieDao {
    private Connection connection;
    private Logger log = LogGen.getInstance();

    JDBCMovieDao(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Movie> getAll() {

        MovieMapper movieMapper = new MovieMapper();
        SessionMapper sessionMapper = new SessionMapper();
        DayMapper dayMapper = new DayMapper();

        Map<Integer, Movie> moviesMap = new HashMap<>();
        Map<Integer, Session> sessionsMap = new HashMap<>();

        String sqlQuery = "SELECT s.id, s.time, s.day_id, s.movie_id, m.id, lm.movie_name, m.pic_url, d.id, ld.day_name, ld.day_name_short\n" +
                "                FROM cinema_final.movies AS m\n" +
                "                       LEFT JOIN cinema_final.sessions AS s ON s.movie_id = m.id\n" +
                "                       LEFT JOIN cinema_final.days AS d ON d.id = s.day_id\n" +
                "                       LEFT JOIN cinema_final.days_translate as ld on d.id = ld.day_id\n" +
                "                       LEFT JOIN cinema_final.movies_translate as lm ON m.id = lm.movie_id && (ld.lang_id = lm.lang_id || ld.lang_id IS NULL)\n" +
                "                       LEFT JOIN cinema_final.languages as l ON l.id = lm.lang_id\n" +
                "                WHERE l.lang_tag = \'" + super.getLocale() +" \'\n" +
                "                ORDER BY s.movie_id, d.id, s.time;";


        try(PreparedStatement prepStatement = connection.prepareStatement(sqlQuery)){
            log.debug(PREP_STAT_OPENED + " in MovieDao getAll()");
            try (ResultSet resultSet = prepStatement.executeQuery();){

                log.debug(QUERY_EXECUTED + " in MovieDao getAll()");
                while (resultSet.next()) {
                    Movie movie = movieMapper.extractFromResultSet(resultSet, 5, 6 ,7);
                    movie = movieMapper.makeUnique(moviesMap, movie);

                    Session rowSession = sessionMapper.extractFromResultSet(resultSet, 1, 2, 3, 4);
                    Day day = dayMapper.extractFromResultSet(resultSet, 8, 9, 10);

                    Optional.ofNullable(day).ifPresent(rowSession::setDay);
                    rowSession.setMovie(movie);
                    rowSession = sessionMapper.makeUnique(sessionsMap, rowSession);
                    Movie finalMovie = movie;
                    Optional.ofNullable(rowSession).ifPresent(e -> finalMovie.getSessions().add(e));
                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return new LinkedList<>(moviesMap.values());
    }

    @Override
    public Movie update(Movie entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Movie getEntityById(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer id) throws DaoException {
        SessionMapper sessionMapper = new SessionMapper();

        String sqlQuery1 = "DELETE FROM `cinema_final`.`movies` WHERE `id` = " + id;
        String sqlQuery2 = "DELETE FROM `cinema_final`.`movies_translate` WHERE `movie_id` = " + id;
        String sqlQuery3 = "select * FROM `cinema_final`.`sessions` where sessions.movie_id = " + id;
        String sqlQuery4 = "DELETE FROM `cinema_final`.`sessions` WHERE `id` = (?)" ;
        String sqlQuery5 = "DELETE FROM `cinema_final`.`tickets` WHERE `session_id` = (?)";

        try (PreparedStatement prepStatement1 = connection.prepareStatement(sqlQuery1);
             PreparedStatement prepStatement2 = connection.prepareStatement(sqlQuery2);
             PreparedStatement prepStatement3 = connection.prepareStatement(sqlQuery3);
             PreparedStatement prepStatement4 = connection.prepareStatement(sqlQuery4);
             PreparedStatement prepStatement5 = connection.prepareStatement(sqlQuery5)){

            log.debug(PREP_STAT_OPENED + " in MovieDao delete()");

            try {
                connection.setAutoCommit(false);
                ResultSet resultSet = prepStatement3.executeQuery();
                Session session = new Session();
                while(resultSet.next()) {
                    session = sessionMapper.extractFromResultSet(resultSet, 1, 2, 3, 4);
                    int idSession = session.getId();

                    prepStatement4.setInt(1, idSession);
                    prepStatement5.setInt(1, idSession);

                    prepStatement5.execute();
                    prepStatement4.execute();
                }

                prepStatement2.execute();
                prepStatement1.execute();
                log.debug(QUERY_EXECUTED + " in MovieDao delete()");
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                log.error(SQL_EXCEPTION_WHILE_DELETING, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
    }

    @Override
    public void create(Movie entity) throws DaoException {
        String sqlQuery = "INSERT INTO `cinema_final`.`movies` (`pic_url`) VALUES (?)";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery)){

            prepStatement.setString(1, entity.getPicUrl());
            log.debug(PREP_STAT_OPENED + " in MovieDao create()");
            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in MovieDao create()");
            } catch (SQLIntegrityConstraintViolationException e) {
                log.error(SQL_EXCEPTION_WHILE_INSERT, e);
                throw new DaoException(e.getMessage(), e);
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_INSERT, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
    }

    @Override
    public void insertTranslatedNameById(int movieID, int languageID, String movieName) {
        String sqlQuery = "INSERT INTO `cinema_final`.`movies_translate` (`movie_id`, `lang_id`, `movie_name`) VALUES (?, ?, ?)";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery)){

            prepStatement.setInt(1, movieID);
            prepStatement.setInt(2, languageID);
            prepStatement.setString(3, movieName);
            log.debug(PREP_STAT_OPENED + " in MovieDao insertTranslatedNameById()");
            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in MovieDao insertTranslatedNameById()");
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
    }

    @Override
    public int getIdByPictureName(String picName) throws DaoException{
        int movieID = 0;
        String sqlQuery = "SELECT m.id FROM `cinema_final`.`movies` AS m WHERE pic_url = \'" + picName + "\' ORDER BY m.id DESC";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery)){

            log.debug(PREP_STAT_OPENED + " in MovieDao getIdByPictureName()");
            try (ResultSet resultSet = prepStatement.executeQuery();){

                log.debug(QUERY_EXECUTED + " in MovieDao getIdByPictureName()");
                if (!resultSet.isBeforeFirst()) {
                    log.info(NO_SUCH_ENTRY_IN_DB + " in MovieDao getIdByPictureName()");
                    throw new DaoException("No such entry in the DB");
                }
                resultSet.next();
                movieID = resultSet.getInt(1);
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return movieID;
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

    public Connection getConnection() {
        return connection;
    }
}
