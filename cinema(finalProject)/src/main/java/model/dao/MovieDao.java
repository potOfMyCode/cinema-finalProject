package model.dao;

import model.dao.exceptions.DaoException;
import model.entity.Movie;

import java.sql.Connection;

public interface MovieDao extends GenericDao<Movie, Integer> {

    void insertTranslatedNameById(int movieID, int languageID, String movieName);

    int getIdByPictureName(String picName) throws DaoException;

    Connection getConnection();

}
