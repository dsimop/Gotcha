package backend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAOUtils class contains the helper methods in relation to database access
 *
 * @author Daohan Chong
 * @version 2018-02-25
 */
public class DAOUtils {
    /**
     * Close the statement and resultSet objects if they are not null
     *
     * @param statement is the statement to be closed
     * @param resultSet is the result set to be closed
     */
    public static void cleanUpStatementAndResultSet(Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ignored) {

        }
    }
}
