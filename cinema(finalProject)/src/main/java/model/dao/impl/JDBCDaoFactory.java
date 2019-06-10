package model.dao.impl;

import model.dao.*;
import model.util.LogGen;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static model.util.LogMsg.CONNECTION_NOT_RECEIVED;

public class JDBCDaoFactory extends DaoFactory {
    private static Logger log = LogGen.getInstance();

    private DataSource dataSource = ConnectionPool.getDataSource();

    @Override
    public UserDao createUserDao() {
        AbstractDao dao = new JDBCUserDao(getConnection());
        dao.setLocale(super.getDaoLocale());
        return (UserDao)dao;
    }

    @Override
    public MovieDao createMovieDao() {
        AbstractDao dao = new JDBCMovieDao(getConnection());
        dao.setLocale(super.getDaoLocale());
        return (MovieDao)dao;
    }

    @Override
    public SessionDao createSessionDao() {
        AbstractDao dao = new JDBCSessionDao(getConnection());
        dao.setLocale(super.getDaoLocale());
        return (SessionDao)dao;
    }

    @Override
    public DayDao createDayDao() {
        AbstractDao dao = new JDBCDayDao(getConnection());
        dao.setLocale(super.getDaoLocale());
        return (DayDao)dao;
    }

    @Override
    public TicketDao createTicketDao() {
        AbstractDao dao = new JDBCTicketDao(getConnection());
        dao.setLocale(super.getDaoLocale());
        return (TicketDao)dao;
    }

    private Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error(CONNECTION_NOT_RECEIVED);
            throw new RuntimeException(e);
        }
    }
}
