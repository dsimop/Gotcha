package backend.dao;

import common.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import backend.ConnectionFactory;

import java.sql.Connection;

import static org.junit.Assert.*;

/**
 * Test cases for UserDAO
 *
 * @author Daohan Chong
 */
public class UserDAOTest {

//    private Connection connection;
//    private UserDAO userDAO;
//
//    @Before
//    public void setUp() {
//        connection = ConnectionFactory.getConnection();
//        userDAO = new UserDAO(connection);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        connection.close();
//    }
//
//    @Test
//    public void testLoginSuccessful() {
//        User user = userDAO.login("mtrout", "test");
//        assertNotNull(user);
//        assertEquals("mtrout", user.getUsername());
//    }
//
//    @Test
//    public void testLoginSuccessfulWithWrongPassword() {
//        User user = userDAO.login("mtrout", "wrong");
//        assertNull(user);
//    }
//
//    @Test
//    public void testLoginSuccessfulWithInvalidUser() {
//        User user = userDAO.login("invalid_user", "wrong");
//        assertNull(user);
//    }
//    */
}