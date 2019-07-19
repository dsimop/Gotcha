package common.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * The test cases for User
 *
 * @author Dimitrios Simopoulos, Daohan Chong
 * @version 2018-03-13
 */
public class UserTest {

    private User user1;

    @Before
    public void setUp() {
        user1 = new User();
        user1.setMark(52);
        user1.setPassword("test");
        user1.setTeacher(true);
        user1.setUid(1);
        user1.setUsername("test");
    }

    @Test
    public void userTest1() {
        String expected = "[User uid: 1, username: test, mark: 52, is_teacher: true]";
        assertEquals(expected, user1.toString());
    }

    @Test
    public void userTest2() {
        User user = new User();
        user.setMark(19);
        user.setPassword("test");
        user.setTeacher(true);
        user.setUid(3);
        user.setUsername("test");
        String expected = "[User uid: 3, username: test, mark: 19, is_teacher: true]";
        assertEquals(expected, user.toString());
    }

    @Test
    public void userTest3() {
        User user = new User();
        user.setMark(55);
        user.setPassword("test");
        user.setTeacher(false);
        user.setUid(3);
        user.setUsername("test");
        String expected = "[User uid: 3, username: test, mark: 55, is_teacher: false]";
        assertEquals(expected, user.toString());
    }


    /**
     * The case when the two ids are identical and not zero
     */
    @Test
    public void testUserEquals1() {
        User expectedUser = new User();
        expectedUser.setMark(52);
        expectedUser.setPassword("test");
        expectedUser.setTeacher(true);
        expectedUser.setUid(1);
        expectedUser.setUsername("test");

        assertEquals(expectedUser, user1);
    }

    /**
     * The case when the two ids are identical and not zero
     */
    @Test
    public void testUserEquals2() {
        User expectedUser = new User();
        expectedUser.setMark(52);
        expectedUser.setPassword("test");
        expectedUser.setTeacher(true);
        expectedUser.setUid(2);
        expectedUser.setUsername("test");

        assertNotEquals(expectedUser, user1);
    }

    /**
     * The case when the ids are all zero
     */
    @Test
    public void testUserEquals3() {
        User actualUser = new User();
        actualUser.setMark(52);
        actualUser.setPassword("test");
        actualUser.setTeacher(true);
        actualUser.setUid(0);
        actualUser.setUsername("test");

        User expectedUser = new User();
        expectedUser.setMark(52);
        expectedUser.setPassword("test");
        expectedUser.setTeacher(true);
        expectedUser.setUid(0);
        expectedUser.setUsername("test");

        assertEquals(expectedUser, actualUser);
    }

    /**
     * The case when the ids are all zero
     */
    @Test
    public void testUserEquals4() {
        User actualUser = new User();
        actualUser.setMark(55);
        actualUser.setPassword("test");
        actualUser.setTeacher(true);
        actualUser.setUid(0);
        actualUser.setUsername("test");

        User expectedUser = new User();
        expectedUser.setMark(52);
        expectedUser.setPassword("test");
        expectedUser.setTeacher(true);
        expectedUser.setUid(0);
        expectedUser.setUsername("test");

        assertNotEquals(expectedUser, actualUser);
    }
}
