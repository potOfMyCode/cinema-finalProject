package model.dao;

import model.dao.exceptions.DaoException;
import model.entity.User;

public interface UserDao extends GenericDao<User, Integer> {

    User getEntityByUsername(String name) throws DaoException;

    User getEntityByEmail(String name) throws DaoException;

}