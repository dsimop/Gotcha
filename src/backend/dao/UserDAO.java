package backend.dao;

import common.Utils;
import common.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * UserDAO is the Data Access Object for User model in relation to the database
 * table "users". Interface is not used for DAO in this project because there is
 * only one type of database (i.e. PostgreSQL) supported in this project.
 * <p>
 * References: 1. Core J2EE Patterns - Data Access Object:
 * http://www.oracle.com/technetwork/java/dataaccessobject-138824.html 2.
 * Building Simple Data Access Layer Using JDBC:
 * https://dzone.com/articles/building-simple-data-access-layer-using-jdbc
 *
 * @author Daohan Chong
 * @version 2018-02-26
 */
public class UserDAO extends AbstractDAO {

    /**
     * Construct the DAO instance with a database connection
     *
     * @param connection is a database connection
     */
    public UserDAO(Connection connection) {
        super(connection);
    }

    /**
     * Extract the user data from a ResultSet
     *
     * @param resultSet is the ResultSet to read user information
     * @return a User object containing the information
     * @throws SQLException if the ResultSet is not valid to read user information
     */
    private static User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        while (resultSet.next()) {
            user.setUid(resultSet.getInt("uid"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setMark(resultSet.getInt("mark"));
            user.setTeacher(resultSet.getBoolean("is_teacher"));
        }
        return user;
    }

    /**
     * Extract the user data from a ResultSet
     *
     * @param resultSet is the ResultSet to read user information
     * @return an ArrayList of User containing the information
     * @throws SQLException if the ResultSet is not valid to read user information
     */
    private static ArrayList<User> extractUserArrayFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setUid(resultSet.getInt("uid"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setMark(resultSet.getInt("mark"));
            user.setTeacher(resultSet.getBoolean("is_teacher"));
            users.add(user);
        }
        return users;
    }

    /**
     * Find out if the user's login details are in the database. pre-condition: the
     * username and password must be valid post-condition: the statement and result
     * set created in the method should be closed.
     *
     * @param userForValidation A user object containing the username and password
     * @return User if the user's info (username and password) is stored within the
     * database, else null.
     */
    public User login(User userForValidation) {
        Utils.logInfo("Starting extracting user from database");
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            statement.setString(1, userForValidation.getUsername());
            statement.setString(2, userForValidation.getPassword());
            ResultSet rs = statement.executeQuery();
            Utils.logSQL(statement);
            if (rs.next()) {
                User validUser = new User();
                validUser.setUsername(rs.getString("username"));
                validUser.setTeacher(rs.getBoolean("is_teacher"));
                validUser.setMark(rs.getInt("mark"));
                int uid = rs.getInt("uid");
                Utils.logInfo("uid:", uid);
                validUser.setUid(rs.getInt("uid"));
                return validUser;
            }
            return null;
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    public User login2(User user) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        System.out.println("Starting extracting user from database " + user);
        try {
            statement = getConnection().prepareStatement("SELECT * FROM users WHERE username=? AND password =?");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            resultSet = statement.executeQuery();
            return extractUserFromResultSet(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    /**
     * Get the user with specified uid
     * <p>
     * Precondition: the uid should be a valid id for the database. For example, the
     * uid cannot be 0. Post-condition: the statement and result set created in the
     * method should be closed.
     *
     * @param uid is the uid which is used to search for the user in the database
     * @return a User object containing the related information. It could also
     * return null if there is SQLException during the runtime or the
     * specified user cannot be found.
     */
    public User getUser(int uid) {
        System.out.println("Getting User's full details");
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = getConnection().prepareStatement("SELECT * FROM users WHERE uid = ?");
            statement.setInt(1, uid);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            Utils.logSQLException(e);
        } finally {
            DAOUtils.cleanUpStatementAndResultSet(statement, resultSet);
        }

        return null;
    }

    /**
     * Get the top 5 users from the database
     *
     * @return an ArrayList of users
     */
    public ArrayList<User> getLeaderboard() {
        try (PreparedStatement statement = getConnection()
                .prepareStatement("SELECT * FROM users ORDER BY mark DESC LIMIT 5;")) {
            ResultSet resultSet = statement.executeQuery();
            Utils.logSQL(statement);
            return extractUserArrayFromResultSet(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    /**
     * Adds a user to the database.
     *
     * @param user A user object that contains information about the user
     * @return An update if the statement was successful.
     */
    public boolean addUserToDatabase(User user) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        System.out.println("Starting adding user into database");
        try {
            statement = getConnection()
                    .prepareStatement("INSERT INTO users (username, " + "password, is_teacher)VALUES (?, ?, ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setBoolean(3, user.getTeacher());

            return statement.executeUpdate() >= 1;
        } catch (SQLException e) {
            Utils.logInfo(e.toString());
        } finally {
            DAOUtils.cleanUpStatementAndResultSet(statement, resultSet);
        }
        return false;
    }
}