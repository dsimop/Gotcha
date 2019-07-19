package frontend.teacher;

import com.google.gson.reflect.TypeToken;
import common.Message;
import common.Utils;
import common.model.Answer;
import common.model.AnswerDistribution;
import common.model.Question;
import frontend.GotchaClientApp;
import frontend.teacher.model.RootPanelModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * CommunicationThread is used for communicate with the server asynchronously
 * If this kind of operations are put in the main thread, the user interface will be blocked.
 * Note: If the UI needs to be updated while new messages are received, the thread need to call the view model,
 * instead of updating the UI directly. This is because the UI is not thread-safe.
 * For more details, please read: Lesson: Concurrency in Swing
 * (https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html)
 *
 * @author Daohan Chong
 * @version 2018-03-02
 */
public class CommunicationThread extends Thread {

    private RootPanelModel rootPanelModel;
    ObjectOutputStream toServer;


    /**
     * Construct the CommunicationThread class
     *
     * @param rootPanelModel is the RootPanelModel created before
     */
    public CommunicationThread(RootPanelModel rootPanelModel) {
        this.rootPanelModel = rootPanelModel;
    }

    @Override
    public void run() {
        System.out.println("Communication thread started");

        try (Socket socket = new Socket(Utils.getServerHostConfig(), Utils.getServerPortConfig());
             ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream())) {
            this.toServer = toServer;

            Message message = new Message(Message.ServerOperations.GET_QUESTIONS);
            message.writeToOutputStream(toServer);

            while (true) {
                String messageStr = fromServer.readUTF();
                Message messageFromServer = Message.createGson().fromJson(messageStr, Message.class);

                System.out.println("messageStr:" + messageStr);
                switch (messageFromServer.getOperation()) {
                    case Message.ClientOperations.UPDATE_QUESTIONS:

                        Message<ArrayList<Question>> gotMessage =
                                Message.createGson().fromJson(messageStr,
                                        new TypeToken<Message<ArrayList<Question>>>() {
                                        }.getType());
                        System.out.println("gotMessage: " + gotMessage.getOperation());
                        System.out.println("gotMessage: " + gotMessage.getData());
                        rootPanelModel.getQuestionPanelModel().setQuestions(gotMessage.getData());
                        break;
                    case Message.ClientOperations.UPDATE_ANSWER_DISTRIBUTION:
                        Message<AnswerDistribution> answerDistributionMessage =
                                Message.createGson().fromJson(messageStr,
                                        new TypeToken<Message<AnswerDistribution>>() {
                                        }.getType());
                        rootPanelModel.getQuestionPanelModel().setAnswerDistribution(answerDistributionMessage.getData());
                        break;
                    default:
                        Utils.logInfo("Unhandled message: " + messageStr);
                }
            }

        } catch (IOException e) {
            Utils.logException("InterruptedException: ", e);
        } finally {
            System.out.println("Communication thread stopped");
            // Try to reconnect
            new Thread(() -> GotchaClientApp.popReconnecting()).start();
            ConnectionMonitorThread cmt = new ConnectionMonitorThread();
            cmt.start();
        }
    }


    private static void handleMessage(Message message) {

    }

    public void sendNewQuestion2Server(Question question) {
        Message<Question> message = new Message<>(Message.ServerOperations.CREATE_QUESTION, question);
        try {
            message.writeToOutputStream(toServer);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle the exception or retry this operation
        }
    }

    public void sendUpdatedQuestion2Server(Question question) {
        Message<Question> message = new Message<>(Message.ServerOperations.UPDATE_QUESTION, question);
        try {
            message.writeToOutputStream(toServer);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle the exception or retry this operation
        }
    }

    public void sendDeletingQuestion2Server(Question question) {
        Message<Question> message = new Message<>(Message.ServerOperations.DELETE_QUESTION, question);
        try {
            message.writeToOutputStream(toServer);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle the exception or retry this operation
        }
    }

    public void sendDeletingAnswer2Server(Answer answer) {
        Message<Answer> message = new Message<>(Message.ServerOperations.DELETE_ANSWER, answer);
        try {
            message.writeToOutputStream(toServer);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle the exception or retry this operation
        }
    }

    public void sendMessageToServer(Message message) {
        try {
            message.writeToOutputStream(toServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendNewAnswer2Server(Answer answer) {
        Message<Answer> message = new Message<>(Message.ServerOperations.CREATE_ANSWER, answer);
        try {
            message.writeToOutputStream(toServer);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle the exception or retry this operation
        }
    }

    public void sendUpdatingAnswer2Server(Answer answer) {
        Message<Answer> message = new Message<>(Message.ServerOperations.UPDATE_ANSWER, answer);
        try {
            message.writeToOutputStream(toServer);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle the exception or retry this operation
        }
    }
}
