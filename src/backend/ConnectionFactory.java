package backend;

import common.Utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionFactory class can create database connections using factory
 * pattern. Note: the connection should be close before finishing all the tasks.
 * <p>
 * References to factory pattern: 1.
 * https://en.wikipedia.org/wiki/Factory_(object-oriented_programming) 2.
 * https://en.wikipedia.org/wiki/Factory_method_pattern
 * <p>
 *
 * @author Daohan Chong, Dimitrios Simopoulos, Mel Trout, Yang He
 * @version 2018-02-24
 */
public class ConnectionFactory {

    private final static String SERVER_HOST = "mod-msc-sw1.cs.bham.ac.uk";

    /**
     * Create a new connection of database
     *
     * @return a new database connection
     */
    public static Connection getConnection() {
        // Read the key information for the database connection
        // Reference: https://stackoverflow.com/a/9655701
        String user = "middlesbrough";
        String dbName = "middlesbrough";
        String password = "3vlg9wgff8";

        // Construct the connection URL
        String URL = "jdbc:postgresql://" + SERVER_HOST + ":5432/" + dbName;

        // Set up the database driver
        setUpDbDriver();

        // Set up the connection and statement
        try {
            return DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            System.err.println(e);
            throw new RuntimeException("SQLException while creating a new connection to the database");
        }
    }

    public static void setUpDbDriver() {
        Class<?> driver;
        try {
            driver = Class.forName("org.postgresql.Driver");
            DriverManager.registerDriver((Driver) driver.newInstance());
        } catch (ClassNotFoundException e) {
            Utils.logInfo("ClassNotFoundException at ConnectionFactory");
            throw new RuntimeException("Database driver not found");
        } catch (Exception e) {
            Utils.logInfo("Normal Exception at ConnectionFactory");
            throw new RuntimeException("Driver could not be registered");
        }
    }
}