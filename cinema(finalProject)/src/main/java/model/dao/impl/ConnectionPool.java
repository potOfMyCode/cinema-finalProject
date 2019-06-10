package model.dao.impl;

import model.util.LogGen;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.util.ResourceBundle;

import static model.util.LogMsg.CONNECTION_POOL_CREATED;

class ConnectionPool {
    private static Logger log = LogGen.getInstance();

    private static final String DRIVER_CLASS_NAME;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static volatile DataSource dataSource;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
        DRIVER_CLASS_NAME = resourceBundle.getString("driver.class.name");
        URL = resourceBundle.getString("db.url");
        USERNAME = resourceBundle.getString("db.username");
        PASSWORD = resourceBundle.getString("db.password");
    }

    private ConnectionPool() {
    }

    static DataSource getDataSource() {

        if (dataSource == null) {
            synchronized (ConnectionPool.class) {
                if (dataSource == null) {
                    BasicDataSource ds = new BasicDataSource();
                    ds.setDriverClassName(DRIVER_CLASS_NAME);
                    ds.setUrl(URL);
                    ds.setUsername(USERNAME);
                    ds.setPassword(PASSWORD);
                    ds.setMinIdle(5);
                    ds.setMaxIdle(10);
                    ds.setMaxTotal(25);
                    ds.setMaxOpenPreparedStatements(100);
                    dataSource = ds;
                    log.info(CONNECTION_POOL_CREATED);
                }
            }
        }
        return dataSource;
    }
}

