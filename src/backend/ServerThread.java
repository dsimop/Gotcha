package backend;

import common.Message;
import common.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * The ServerThread class is used for handling any incoming socket connection
 * from the clients
 *
 * @author Daohan Chong, Yang He, Dimitrios Simopoulos, Melisha Trout
 * @version 2018-02-25
 */
public class ServerThread extends Thread {

    private Socket client;
    private ServerMessageHandler serverMessageHandler = new ServerMessageHandler();

    /**
     * Construct a ServerThread instance
     *
     * @param client
     *            is the client socket
     */
    public ServerThread(Socket client) {
        this.client = client;

        Utils.logInfo("new connection from one client");
    }

    @Override
    public void run() {
        handleClientConnection();
    }

    /**
     * Use a loop to handle client messages.
     */
    private void handleClientConnection() {
        System.out.println("start to handle connection");
        try (ObjectOutputStream toClient = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream fromClient = new ObjectInputStream(client.getInputStream())) {
            String messageStr;
            while (true) {
                messageStr = fromClient.readUTF();
                Message responseMessageToBeSent = serverMessageHandler.handleMessage(messageStr);
                if (responseMessageToBeSent != null) {
                    toClient.writeUTF(responseMessageToBeSent.toJsonString());
                    toClient.flush();
                }
            }

        } catch (IOException e) {
            Utils.logException("A connection for client is lost");
        } finally {
            // Cleaning
            try {
                client.close();
            } catch (IOException e) {
                Utils.logException("A connection for client is lost: minor problem while closing the client socket");
            }

            try {
                serverMessageHandler.closeDbConnection();
            } catch (SQLException e) {
                Utils.logException("Closing db connection while socket connection is lost", e);
            }
        }
    }
}