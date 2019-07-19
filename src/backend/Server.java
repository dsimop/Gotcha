package backend;

import common.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The entry class for the server of this system By default, the server will
 * listen on port 9090, but this may change if it is not available
 *
 * @author Daohan Chong, Yang He, Dimitrios Simopoulos, Mel Trout
 * @version 2018-02-25
 */
public class Server {

    public static void main(String[] args) {

        int port = Utils.getServerPortConfig();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("This server starts to listen on port " + port);
            handleConnections(serverSocket);
        } catch (IOException e) {
            Utils.logException("starting the server:", e);
            Utils.logInfo("Cannot listen on port: " + port);
            System.exit(-1);
        }
    }

    /**
     * This void method is responsible for handling the Connection with the Server.
     * 
     * @param serverSocket
     *            is the incoming Socket of the Server
     */
    private static void handleConnections(ServerSocket serverSocket) {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerThread s = new ServerThread(clientSocket);
                s.start();
            }
        } catch (IOException e) {
            Utils.logException("problem found while calling serverSocket.accept()");
        }
    }

}