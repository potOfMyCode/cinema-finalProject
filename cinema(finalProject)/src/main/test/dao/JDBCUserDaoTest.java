package dao;

import model.dao.DaoFactory;
import model.dao.UserDao;
import model.dao.exceptions.DaoException;
import model.entity.Ticket;
import model.entity.User;
import model.util.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

public class JDBCUserDaoTest {
    private UserDao factory;

    @Before
    public void init(){
        factory = DaoFactory.getInstance().createUserDao();
    }

    @After
    public void after() {
        factory.close();
    }

    @Test
    public void createTest() throws DaoException {
        User user = new User();
        user.setUsername("Misha");
        user.setRole(Role.USER);

        user.setEmail("misha@gmail.com");
        user.setPassword("password");

        factory.create(user);

        assertNotNull(factory.getEntityByUsername("Misha"));
    }

    @Test(expected = DaoException.class)
    public void createWithExceptionTest() throws DaoException {
        User user = new User();
        user.setUsername("Misha");
        user.setRole(Role.USER);

        user.setEmail("misha@gmail.com");
        user.setPassword("password");
        factory.create(user);
    }

    @Test
    public void getEntityByUserNameTest() throws DaoException {
        User user = new User();
        user.setUsername("dima123");
        user.setRole(Role.USER);

        user.setEmail("dima@gmail.com");
        user.setPassword("dimasik");
        user.setId(2);
        assertNotNull(factory.getEntityByUsername("dima123"));
    }

    public void getEntityByEmailNameTest() throws DaoException {
        assertNotNull(factory.getEntityByEmail("dima@gmail.com"));
    }

    @Test(expected = DaoException.class)
    public void getEntityByUserNameWithExceptionTest() throws DaoException {
        assertNull(factory.getEntityByUsername("dog"));
    }

    @Test(expected = DaoException.class)
    public void getEntityByEmailWithExceptionTest() throws DaoException {
        assertNull(factory.getEntityByEmail("kot@gmail.com"));
    }

    @Ignore("use only if user has tickets")
    @Test
    public void deleteTest() throws DaoException {
        assertNotNull(factory.getEntityByUsername("Misha"));
        User user = factory.getEntityByUsername("Misha");
        factory.delete(user.getId());
        assertNull(factory.getEntityByUsername("Misha"));
    }

    @Test
    public void getEntityByIdTest() throws DaoException {
        assertNotNull(factory.getEntityByUsername("dima123"));
        User user = factory.getEntityByUsername("dima123");
        assertNotNull(factory.getEntityById(user.getId()));
    }
}
