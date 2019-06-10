package dao;

import model.dao.DaoFactory;
import model.dao.MovieDao;
import model.dao.SessionDao;
import model.dao.exceptions.DaoException;
import model.entity.Movie;
import model.entity.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class JDBCSessionDaoTest {
    private SessionDao factory;

    @Before
    public void init(){
        factory = DaoFactory.getInstance().createSessionDao();
    }

    @After
    public void after() {
        factory.close();
    }


    @Test(expected = DaoException.class)
    public void createTest() throws DaoException {
        Session session = new Session();
        Time time = Time.valueOf("15:00:00");
        session.setTime(time);
        session.setDayID(8);
        session.setMovieID(1000);

        factory.create(session);
    }

    @Test
    public void deleteTest() throws DaoException {}

    @Test
    public void getEntityByIdTest() throws DaoException {
        assertNotNull(factory.getEntityById(1));
    }
}
