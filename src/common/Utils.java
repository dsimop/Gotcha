package common;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Common logic for both the clients and the server.
 *
 * @author Daohan Chong
 * @version 2018-03-18
 */
public class Utils {

    private final static Logger LOGGER = Logger.getLogger("Gotcha");
    private static String serverHost;
    private static String serverPortStr;

    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Log an INFO message, with no arguments.
     * <p>
     * If the logger is currently enabled for the given message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     *
     * @param msg The string message (or a key in the message catalog)
     */
    public static void logInfo(String msg) {
        getLogger().log(Level.INFO, msg);
    }

    /**
     * Log an INFO message, with one object parameter.
     * <p>
     * If the logger is currently enabled for the given message
     * level then a corresponding LogRecord is created and forwarded
     * to all the registered output Handler objects.
     * <p>
     *
     * @param msg    The string message (or a key in the message catalog)
     * @param param1 parameter to the message
     */
    public static void logInfo(String msg, Object param1) {
        getLogger().log(Level.INFO, msg + " " + param1);
    }

    /**
     * Log an INFO message, with an array of object arguments.
     * <p>
     * If the logger is currently enabled for the given message
     * level then a corresponding LogRecord is created and forwarded
     * to all the registered output Handler objects.
     * <p>
     *
     * @param msg    The string message (or a key in the message catalog)
     * @param params array of parameters to the message
     */
    public static void logInfo(String msg, Object params[]) {
        getLogger().log(Level.INFO, msg, params);
    }

    /**
     * Log an exception
     *
     * @param msg       is the message for this exception
     * @param exception is the exception
     */
    public static void logException(String msg, Exception exception) {
        logInfo("Handled exception: " + msg, exception.getMessage());
    }

    /**
     * Log an exception without the exception object
     *
     * @param msg is the message for this exception
     */
    public static void logException(String msg) {
        logInfo("Handled exception:" + msg);
    }

    public static void logSQLException(SQLException sqlException) {
        logInfo("Handled SQL exception (might lead to null return value, stated in javadoc of this method): "
                + sqlException.getSQLState() + ": " + sqlException.getMessage());
    }

    /**
     * Log an INFO message with an SQL statement
     *
     * @param sqlStatement is the SQL statement object
     */
    public static void logSQL(Object sqlStatement) {
        getLogger().log(Level.INFO, "SQL: {0}", sqlStatement);
    }

    /**
     * Get the server host according to the environment variable "SERVER_HOST"
     * If this environment variable is null, the default host will be "localhost".
     *
     * @return the host name
     */
    public static String getServerHostConfig() {
        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null || serverHost.equals("")) {
            serverHost = Utils.serverHost;
        }
        if (serverHost == null || serverHost.equals("")) {
            serverHost = "localhost";
        }
        return serverHost;
    }

    public static void setServerHost(String serverHost) {
        Utils.serverHost = serverHost;
    }

    /**
     * Get the server port for communication according to the environment variable "SERVER_PORT"
     * If this environment variable is null, the default host will be 50050.
     *
     * @return the port number
     */
    public static int getServerPortConfig() {
        String serverPortStr = System.getenv("SERVER_PORT");
        if (serverPortStr == null || serverPortStr.equals("")) {
            serverPortStr = Utils.serverPortStr;
        }
        if (serverPortStr == null || serverPortStr.equals("")) {
            serverPortStr = "50050";
        }
        return Integer.parseInt(serverPortStr);
    }

    public static void setServerPortStr(String serverPortStr) {
        Utils.serverPortStr = serverPortStr;
    }

    /**
     * Compare two objects when either or both of them could be null
     *
     * @param a is the one nullable object
     * @param b is the other nullable object
     * @return a boolean value indicating whether they are equal
     */
    public static boolean equalsWithNulls(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else if (!a.getClass().equals(b.getClass())) {
            return false;
        } else {
            return a.equals(b);
        }
    }
}
