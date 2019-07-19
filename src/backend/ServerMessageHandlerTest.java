package backend;

import common.Message;
import common.model.Question;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Daohan Chong
 * @version 2018-03-21
 */
public class ServerMessageHandlerTest {

    /**
     * Make a connection to the mock database for testing
     *
     * @return a Connection to the mock database
     */
    public static Connection makeMockDbConnection() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream("./test-db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String user = properties.getProperty("DB_USER");
        String dbName = properties.getProperty("DB_NAME");
        String password = properties.getProperty("DB_PASSWORD");
        String host = properties.getProperty("DB_HOST");

        // Construct the connection URL
        String URL = "jdbc:postgresql://" + host + ":5432/" + dbName;

        System.out.println("DB url: " + URL);

        // Set up the database driver
        ConnectionFactory.setUpDbDriver();

        // Set up the connection and statement
        try {
            return DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            System.err.println(e);
            throw new RuntimeException("SQLException while creating a new connection to the database");
        }
    }

    @Test
    public void testHandleMessageSendUsersDetails() {
        ServerMessageHandler messageHandler = new ServerMessageHandler(makeMockDbConnection());

        Question question = new Question();
        question.setQuestionText("foo");
        question.setMark(100);
        Message<Question> creatingQuestionMessage = new Message<>(Message.ServerOperations.CREATE_QUESTION, question);

        Message responseMessage = messageHandler.handleMessage(creatingQuestionMessage.toJsonString());


        assertEquals(Message.ClientOperations.UPDATE_QUESTIONS, responseMessage.getOperation());
    }
}