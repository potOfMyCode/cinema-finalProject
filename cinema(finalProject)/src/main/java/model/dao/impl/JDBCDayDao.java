package model.dao.impl;

import model.dao.AbstractDao;
import model.dao.DayDao;
import model.dao.exceptions.DaoException;
import model.dao.mappers.DayMapper;
import model.dao.mappers.MovieMapper;
import model.dao.mappers.SessionMapper;
import model.entity.Day;
import model.entity.Movie;
import model.entity.Session;
import model.util.LogGen;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.util.LogMsg.*;
import static model.util.LogMsg.CANT_CLOSE_CONNECTION;

public class JDBCDayDao extends AbstractDao implements DayDao {
    private Connection connection;
    private Logger log = LogGen.getInstance();

    JDBCDayDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Day> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Day update(Day entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Day getEntityById(Integer id) throws DaoException {
        Day day = new Day();

        MovieMapper movieMapper = new MovieMapper();
        SessionMapper sessionMapper = new SessionMapper();
        DayMapper dayMapper = new DayMapper();

        Map<Integer, Movie> moviesMap = new HashMap<>();
        Map<Integer, Session> sessionsMap = new HashMap<>();
        Map<Integer, Day> daysMap = new HashMap<>();

        String sqlQuery = "SELECT d.id, dt.day_name, dt.day_name_short, s.id, s.time, s.day_id, s.movie_id, m.id, mt.movie_name, m.pic_url\n" +
                "                FROM cinema_final.days AS d\n" +
                "                       LEFT JOIN cinema_final.sessions AS s ON s.day_id = d.id\n" +
                "                       LEFT JOIN cinema_final.movies AS m ON m.id = s.movie_id\n" +
                "                       LEFT JOIN cinema_final.movies_translate AS mt ON mt.movie_id = m.id\n" +
                "                       LEFT JOIN cinema_final.days_translate as dt on dt.day_id = d.id\n" +
                "                       LEFT JOIN cinema_final.languages as l ON l.id = mt.lang_id && l.id = dt.lang_id\n" +
                "                WHERE l.lang_tag = \'" + super.getLocale() +" \' && d.id = " + id + "\n" +
                "                ORDER BY s.time;";

        try (PreparedStatement prepStatement = connection.prepareStatement(sqlQuery);){

            log.debug(PREP_STAT_OPENED + " in DayDao getEntityById()");

            try (ResultSet resultSet = prepStatement.executeQuery();){

                log.debug(QUERY_EXECUTED + " in DayDao getEntityById()");

                while (resultSet.next()) {
                    day = dayMapper.extractFromResultSet(resultSet, 1, 2, 3);
                    day = dayMapper.makeUnique(daysMap, day);
                    Movie movie = movieMapper.extractFromResultSet(resultSet, 8, 9, 10);
                    movie = movieMapper.makeUnique(moviesMap, movie);
                    Session session = sessionMapper.extractFromResultSet(resultSet, 4, 5, 6, 7);
                    session.setDay(day);
                    session.setMovie(movie);
                    session = sessionMapper.makeUnique(sessionsMap, session);
                    day.getSessions().add(session);
                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        }
        return day;
    }

    @Override
    public void delete(Integer id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void create(Day entity) throws DaoException {
        throw new UnsupportedOperationException();
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
