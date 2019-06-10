package dao;

import model.dao.DaoFactory;
import model.dao.MovieDao;
import model.dao.UserDao;
import model.dao.exceptions.DaoException;
import model.entity.Movie;
import model.entity.User;
import model.util.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class JDBCMovieDaoTest {
    private MovieDao factory;

    @Before
    public void init(){
        factory = DaoFactory.getInstance().createMovieDao();
    }

    @After
    public void after() {
        factory.close();
    }

    @Test
    public void getAllTest() throws DaoException {
        assertNotNull(factory.getAll());
    }

    @Test
    public void createTest() throws DaoException {
        Movie movie = new Movie();
        String nameUkr = "ТестФільм";
        String nameEng = "TestFilm";
        String picUrl = "testFilm.jpg";

        movie.setPicUrl(picUrl);

        factory.create(movie);

        int movieId = factory.getIdByPictureName(movie.getPicUrl());

        factory.insertTranslatedNameById(movieId, 1, nameEng);
        factory.insertTranslatedNameById(movieId, 2, nameUkr);

        assertNotNull(factory.getIdByPictureName(picUrl));
    }

    @Test
    public void getIdByPictureNameTest() throws DaoException {
        assertEquals(1, factory.getIdByPictureName("TheGodfather.jpg"));
    }

    @Test(expected = DaoException.class)
    public void deleteTest() throws DaoException {
        String picUrl = "testFilm.jpg";
        assertNotNull(factory.getIdByPictureName(picUrl));
        int movieId = factory.getIdByPictureName(picUrl);

        factory.delete(movieId);

        factory.getIdByPictureName(picUrl);
    }

}
