package backend.dao;

import java.sql.Connection;

/**
 * Extract the common logic from all Data Access Objects
 *
 * @author Daohan Chong, Yang He
 * @version 2018-02-26
 */
public abstract class AbstractDAO {

    private Connection connection;

    /**
     * Construct the DAO instance with a database connection
     *
     * @param connection is a database connection
     */
    AbstractDAO(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() {
        return connection;
    }
}
