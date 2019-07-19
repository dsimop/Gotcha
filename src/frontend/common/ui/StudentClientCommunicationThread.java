package frontend.common.ui;

import com.google.gson.reflect.TypeToken;
import common.Message;
import common.Message.ServerOperations;
import common.Utils;
import common.model.Question;
import common.model.StudentAnswer;
import common.model.User;
import frontend.GotchaClientApp;
import frontend.student.ConnectionMonitorThread;
import frontend.student.model.StudentClientRootModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static common.Message.ClientOperations.*;
import static common.Message.createGson;


/**
 * The LoginClient deals with the Action listener from the login and sign up screen. It checks the following: 1) whether
 * the user has entered the correct information from the login screen (communicate with the database) - if not then the
 * user will be notify (somehow) that the information they have entered is incorrect.
 * <p>
 * 2) Sign Up - check to see if the password that they have entered matched - if not they will be notified to re-enter
 * it.
 * <p>
 * 2.1) If all information is correct, then notify the server and add their information to the database
 * <p>
 * <p>
 * PLEASE NOT THAT THIS MAY CHANGE IN THE FUTURE
 *
 * @author Melisha Trout, Dimitrios Simopoulos, Daohan Chong
 * @version 2018-03-18
 */
public class StudentClientCommunicationThread extends Thread {

    ObjectOutputStream toServer;
    private RootRegistrationModel rootRegistrationModel;
    private StudentClientRootModel studentClientRootModel;

    public StudentClientCommunicationThread(
            RootRegistrationModel rootRegistrationModel, StudentClientRootModel studentClientRootModel) {
        this.rootRegistrationModel = rootRegistrationModel;
        this.studentClientRootModel = studentClientRootModel;
    }


    @Override
    public void run() {

        System.out.println("Communication thread started");
        try(Socket socket = new Socket(Utils.getServerHostConfig(), Utils.getServerPortConfig());
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream())) {
            System.out.println("Communication established");
            this.toServer = toServer;

//            //FIXME this is for test the leaderboard
//            Message getTop5Students = new Message<>(
//                    ServerOperations.GET_TOP_STUDENTS, null);
//
//            getTop5Students.writeToOutputStream(toServer);


            while( true ) {
                String messageStr = fromServer.readUTF();
                Message rawMessage = Message.createGson().fromJson(messageStr, Message.class);
                System.out.println("messageStr:" + messageStr);

                switch( rawMessage.getOperation() ) {
                    case LOGIN_SUCCEED_STUDENT:
                        Message<User> studentUserMessage =
                                createGson().fromJson(messageStr,
                                        new TypeToken<Message<User>>() {
                                        }.getType());
                        studentClientRootModel.getLoginModel().setCurrentUser(studentUserMessage.getData());
                        studentClientRootModel.getLoginModel().whichClient(studentUserMessage);
                        studentClientRootModel.getLoginModel().broadcastLoginSucceed();
                        Utils.logInfo("Student:", studentUserMessage);
                        break;
                    case LOGIN_SUCCEED_TEACHER:
                        Message<User> teacherUserMessage =
                                createGson().fromJson(messageStr,
                                        new TypeToken<Message<User>>() {
                                        }.getType());
                        studentClientRootModel.getLoginModel().setCurrentUser(teacherUserMessage.getData());
                        RootRegistrationModel.getLoginModel().whichClient(teacherUserMessage);
                        studentClientRootModel.getLoginModel().broadcastLoginSucceed();
                        Utils.logInfo("Teacher:", teacherUserMessage);
                        break;
                    case LOGIN_FAILED:
                        studentClientRootModel.getLoginModel().broadcastLoginFailed();
                        break;
                    case REG_SUCCEED:
                        studentClientRootModel.getRegistrationModel().broadCastRegSucceed();
                        break;
                    case REG_FAIL:
                        studentClientRootModel.getRegistrationModel().broadCastRegFail();
                        break;
                    case UPDATE_FIRST_QUESTION_FOR_USER:
                        Message<Question> questionMessage = createGson().fromJson(messageStr,
                                new TypeToken<Message<Question>>() {
                                }.getType());
                        studentClientRootModel.getQuestionPanelModel().updatePanel(questionMessage.getData());
                        break;
                    case TOP_STUDENTS_RECEIVED:
                        Message<ArrayList<User>> gotTop5 = Message.createGson().fromJson(messageStr,
                                new TypeToken<Message<ArrayList<User>>>() {
                                }.getType());
                        Utils.logInfo("TOP_STUDENTS_RECEIVED: ", gotTop5.getData());
                        studentClientRootModel.getTopStudentListModel().top5Students(gotTop5.getData());
                        break;
                    case STUDENTS_PERFORMANCE_RECIEVED:
                        Message<ArrayList<StudentAnswer>> performance = Message.createGson().fromJson(messageStr,
                                new TypeToken<Message<ArrayList<StudentAnswer>>>() {
                                }.getType());
                        studentClientRootModel.getStudentPerformanceModel().setStudentPerformanceDistribution(performance.getData());
                        break;
                    default:
                        Utils.logInfo("[Student Client] Unhandled message: " + messageStr);
                }
            }

        } catch(IOException e) {
            Utils.logException("IOException: ", e);
            System.out.println("Communication stopped");
            // Try to reconnect
            new Thread(GotchaClientApp:: popReconnecting).start();
            ConnectionMonitorThread cmt = new ConnectionMonitorThread();
            cmt.start();
        }
    }


    public void sendUsersDetails2Server(User user) throws IOException {
        System.out.println("User's details received at the communication thread");
        Message<User> message = new Message<>(Message.ServerOperations.GET_ALL_USERS, user);
        message.writeToOutputStream(toServer);
    }


    public void sendNewUser2Server(User user) {
        System.out.println("Sending new user details to the server");
        Message<User> message = new Message<>(ServerOperations.ADD_NEW_USER, user);
        try {
            message.writeToOutputStream(toServer);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to the server to get the first question for the user
     */
    public void getQuestion4User(int UID) {

        Message getQuestionMessage = new Message<>(
                Message.ServerOperations.GET_FIRST_QUESTION_FOR_USER, null, UID);
        try {
            getQuestionMessage.writeToOutputStream(toServer);
        } catch(IOException e) {
            Utils.logException("IOException: ", e);
        }
    }

    /**
     * Get the top 5 students who have answered the questions correctly. pre-conditions: The students must have
     * completed the questions and their score has been updated post-condition: A Arraylist given as a User object,
     * containing the top 5 students,
     */
    public void getTop5Students() {
        Message getTop5Students = new Message<>(
                ServerOperations.GET_TOP_STUDENTS, null);
        try {
            getTop5Students.writeToOutputStream(toServer);
        } catch(IOException e) {
            Utils.logException("IOException: ", e);
        }
    }

    public void sendMessage(Message message) {
        try {
            System.out.println("sendmessageok");
            message.writeToOutputStream(toServer);
        } catch(IOException e) {
           Utils.logException(e.getMessage());
        }
    }

}







