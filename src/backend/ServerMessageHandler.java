package backend;

import backend.dao.AnswerDAO;
import backend.dao.QuestionDAO;
import backend.dao.UserAnswerDAO;
import backend.dao.UserDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.Message;
import common.Message.ClientOperations;
import common.Message.ServerOperations;
import common.Utils;
import common.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static common.Message.ClientOperations.*;

/**
 * ServerMessageHandler is the handler for messages from a single client
 * The instance of this class should be used as a field variable of ServerThread
 *
 * @author Daohan Chong, Yang He, Dimitrios Simopoulos, Melisha Trout
 * @version 2018-03-18
 */
public class ServerMessageHandler {
    private Gson gson = Message.createGson();
    private Connection connection;

    public ServerMessageHandler() {
        connection = ConnectionFactory.getConnection();
    }

    public ServerMessageHandler(Connection connection) {
        this.connection = connection;
    }

    public Message handleMessage(String messageStr) {
        System.out.println("read message: " + messageStr);
        Message rawMessage = gson.fromJson(messageStr, Message.class);

        switch (rawMessage.getOperation()) {
            case Message.ServerOperations.GET_QUESTIONS:
                return sendAllQuestionsToClient();
            case Message.ServerOperations.CREATE_QUESTION:
                if (createNewQuestion(messageStr)) {
                    Utils.logInfo("Created a new question: {0}", rawMessage.getData());
                    return sendAllQuestionsToClient();
                }
                break;
            case Message.ServerOperations.UPDATE_QUESTION:
                if (updateExistingQuestion(rawMessage)) {
                    Utils.logInfo("Updated a question: {0}", rawMessage.getData());
                    return sendAllQuestionsToClient();
                }
                break;
            case Message.ServerOperations.DELETE_QUESTION:
                if (deleteExistingQuestion(rawMessage)) {
                    Utils.logInfo("Deleted a question: {0}", rawMessage.getData());
                    return sendAllQuestionsToClient();
                }
                break;
            case Message.ServerOperations.GET_ALL_USERS:
                return sendUsersDetails(rawMessage);
            case Message.ServerOperations.DELETE_ANSWER:
                if (deleteExistingAnswer(rawMessage)) {
                    Utils.logInfo("Deleted an answer: {0}", rawMessage.getData());
                    return sendAllQuestionsToClient();
                }
                break;
            case Message.ServerOperations.UPDATE_ANSWER:
                if (updateExistingAnswer(messageStr)) {
                    Utils.logInfo("Updated an answer: {0}", rawMessage.getData());
                    return sendAllQuestionsToClient();
                } else {
                    break;
                }
            case Message.ServerOperations.GET_ANSWER_DISTRIBUTION:
                return handlerGetAnswerDistribution(messageStr);
            case Message.ServerOperations.CREATE_ANSWER:
                if (createNewAnswer(messageStr)) {
                    Utils.logInfo("Created an answer: {0}", rawMessage.getData());
                    return sendAllQuestionsToClient();
                } else {
                    break;
                }
            case Message.ServerOperations.ADD_NEW_USER:
                return handleCreatingUser(messageStr);
            case Message.ServerOperations.GET_ALL_QUESTIONS_WITH_4_ANSWERS:
                return sendAllQuestionsWithAnswersToClient();
            case Message.ServerOperations.GET_FIRST_QUESTION_FOR_USER:
                return handleGetFirstQuestionForUser(messageStr);
            case Message.ServerOperations.GET_TOP_STUDENTS:
                return sendTopStudents();
            case Message.ServerOperations.ANSWER_QUESTION:
                sendClickedAnswer(messageStr);
                return handleGetFirstQuestionForUser(messageStr);
            case ServerOperations.GET_STUDENTS_PERFORMANCE:
                return handleGetStudentPerformance(messageStr);
            default:
                Utils.logInfo("Unhandled message: " + messageStr);
        }
        return null;
    }


    private Message<AnswerDistribution> handlerGetAnswerDistribution(String messageStr) {
        Message<Question> questionMessage = Message.createGson().fromJson(messageStr,
                new TypeToken<Message<Question>>() {
                }.getType());
        QuestionDAO questionDAO = new QuestionDAO(connection);
        AnswerDistribution answerDistribution = questionDAO.getUserAnswerDistribution(questionMessage.getData());

        Message<AnswerDistribution> answerDistributionMessage = new Message<>(
                Message.ClientOperations.UPDATE_ANSWER_DISTRIBUTION, answerDistribution);
        return answerDistributionMessage;
    }

    private ArrayList<Question> retrieveAllQuestionsWithAnswers() {
        QuestionDAO questionDAO = new QuestionDAO(connection);
        return questionDAO.getAllQuestionsWithAnswers();
    }

    private Message<ArrayList<Question>> sendAllQuestionsToClient() {
        ArrayList<Question> questions = retrieveAllQuestionsWithAnswers();

        return new Message<>(Message.ClientOperations.UPDATE_QUESTIONS,
                questions);
    }

    private boolean createNewQuestion(String messageStr) {
        Message<Question> questionMessage = Message.createGson().fromJson(messageStr,
                new TypeToken<Message<Question>>() {
                }.getType());

        QuestionDAO questionDAO = new QuestionDAO(connection);
        return questionDAO.createNewQuestion(questionMessage.getData());
    }

    /**
     * Update an existing question Precondition: the input rawMessage must have the
     * operation named ServerOperations.UPDATE_QUESTION, the data should be of type
     * Question and this Question instance should have an id
     *
     * @param rawMessage is the input message
     * @return a boolean value indicating whether the operation is successful
     */
    private boolean updateExistingQuestion(Message rawMessage) {
        Message<Question> questionMessage = Message.createGson().fromJson(rawMessage.toJsonString(),
                new TypeToken<Message<Question>>() {
                }.getType());

        QuestionDAO questionDAO = new QuestionDAO(connection);
        return questionDAO.updateQuestion(questionMessage.getData());
    }

    /**
     * Delete an existing question Precondition: the input rawMessage must have the
     * operation named ServerOperations.DELETE_QUESTION, the data should be of type
     * Question and this Question instance should have an id
     *
     * @param rawMessage is the input message
     * @return a boolean value indicating whether the operation is successful
     */
    private boolean deleteExistingQuestion(Message rawMessage) {
        Message<Question> questionMessage = Message.createGson().fromJson(rawMessage.toJsonString(),
                new TypeToken<Message<Question>>() {
                }.getType());

        QuestionDAO questionDAO = new QuestionDAO(connection);
        return questionDAO.deleteQuestion(questionMessage.getData());
    }

    private boolean deleteExistingAnswer(Message rawMessage) {
        Message<Answer> answerMessage = Message.createGson().fromJson(rawMessage.toJsonString(),
                new TypeToken<Message<Answer>>() {
                }.getType());

        AnswerDAO answerDAO = new AnswerDAO(connection);
        return answerDAO.deleteAnswer(answerMessage.getData());
    }

    private boolean updateExistingAnswer(String messageStr) {
        Message<Answer> answerMessage = Message.createGson().fromJson(messageStr, new TypeToken<Message<Answer>>() {
        }.getType());

        AnswerDAO answerDAO = new AnswerDAO(connection);
        return answerDAO.updateAnswer(answerMessage.getData());
    }

    private boolean createNewAnswer(String messageStr) {
        Message<Answer> answerMessage = Message.createGson().fromJson(messageStr, new TypeToken<Message<Answer>>() {
        }.getType());

        AnswerDAO answerDAO = new AnswerDAO(connection);
        return answerDAO.createNewAnswer(answerMessage.getData());
    }

    /**
     * This method first check to see whether the user is in the database. If true,
     * then it retrieves the users details and sends it back to the client.
     *
     * @throws IOException
     */
    private Message sendUsersDetails(Message rawMessage) {
        Message<User> user = Message.createGson().fromJson(rawMessage.toJsonString(), new TypeToken<Message<User>>() {
        }.getType());

        UserDAO userDAO = new UserDAO(connection);
        // check to see if the user is in the database
        User validUser = userDAO.login(user.getData());
        if (validUser != null) {
            System.out.println("User is in the database, extracting full details");
            // create a User object of the result from the database
            if (validUser.getTeacher()) {
                Message<User> messageToClient = new Message<>(LOGIN_SUCCEED_TEACHER, validUser);
                Utils.logInfo("Teacher:", messageToClient.getData());
                return messageToClient;
            } else {
                Message<User> messageToClient = new Message<>(LOGIN_SUCCEED_STUDENT, validUser);
                Utils.logInfo("Student:", messageToClient.getData());
                return messageToClient;
            }
        } else {
            return new Message<>(LOGIN_FAILED, null);
        }
    }

    /**
     * The handleGetFirstQuestionForUser sends a message to the DAO to retrieve the
     * questions.
     *
     * @param messageStr The message sent from the client which contains the user's ID
     * @throws IOException
     */
    private Message<Question> handleGetFirstQuestionForUser(String messageStr) {
        Message message = gson.fromJson(messageStr, Message.class);
        int userID = message.getUserID();
        QuestionDAO questionDAO = new QuestionDAO(connection);
        Question question = questionDAO.getTheFirstQuestionForUserToAnswer(userID);
        Message<Question> messageToClient = new Message<>(UPDATE_FIRST_QUESTION_FOR_USER, question);
        System.out.println("ok");
        return messageToClient;
    }

    /**
     * Add new user to the database
     *
     * @param messageStr
     * @return
     */
    private Message handleCreatingUser(String messageStr) {
        Message<User> user = Message.createGson().fromJson(messageStr, new TypeToken<Message<User>>() {
        }.getType());
        UserDAO userDAO = new UserDAO(connection);
        boolean result = userDAO.addUserToDatabase(user.getData());

        Message messageToClient = new Message(
                result ? Message.ClientOperations.REG_SUCCEED : Message.ClientOperations.REG_FAIL);
        return messageToClient;
    }

    private ArrayList<Question> getAllQWith4AForUser(int userID) {
        QuestionDAO questionDAO = new QuestionDAO(connection);
        return questionDAO.getAllQuestionsWith4Answers2(1);
    }

    private Message<ArrayList<Question>> sendAllQuestionsWithAnswersToClient() {
        ArrayList<Question> questions = getAllQWith4AForUser(1);

        Message<ArrayList<Question>> messageToClient = new Message<>(Message.ClientOperations.QUESTIONS_RECEIVED, questions);
        return messageToClient;
    }

    private ArrayList<User> requestTopStudents() {
        UserDAO userDAO = new UserDAO(connection);
        return userDAO.getLeaderboard();
    }

    private Message<ArrayList<User>> sendTopStudents() {
        ArrayList<User> users = requestTopStudents();
        Message<ArrayList<User>> messageToClient = new Message<>(Message.ClientOperations.TOP_STUDENTS_RECEIVED, users);
        return messageToClient;
    }

    private void sendClickedAnswer(String message) {
        Message<UserAnswer> messageofClickedAnswer = gson.fromJson(message, new TypeToken<Message<UserAnswer>>() {
        }.getType());
        QuestionDAO answeredq = new QuestionDAO(connection);
        answeredq.answerQuestion(messageofClickedAnswer.getData());
    }

    /**
     * Send a message to the UserAnswerDAO to retrieve the student's performance, and send the result back to the client
     * @param messageStr The message which is being sent to the server which contains the students ID
     * @return A message given as a ArrayList<StudentAnswer> type.
     */
    private Message<ArrayList<StudentAnswer>> handleGetStudentPerformance(String messageStr) {
        Message incomingMessage = Message.createGson().fromJson(messageStr, Message.class);

        UserAnswerDAO userAnswerDAO = new UserAnswerDAO(connection);
        ArrayList<StudentAnswer> studentAnswers = userAnswerDAO.getStudentAnswerList(incomingMessage.getUserID());

        Message<ArrayList<StudentAnswer>> performanceMessage = new Message<>(
                ClientOperations.STUDENTS_PERFORMANCE_RECIEVED, studentAnswers);

        return performanceMessage;
    }

    public void closeDbConnection() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (!connection.isClosed()) {
            connection.close();
        }

        super.finalize();
    }
}
