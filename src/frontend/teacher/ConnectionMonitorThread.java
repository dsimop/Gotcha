package frontend.teacher;

import common.Utils;
import frontend.GotchaClientApp;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Yang He
 * @version 2018/3/13
 */
public class ConnectionMonitorThread extends Thread {
    private Socket testSocket;

    public ConnectionMonitorThread() {
        this.testSocket = null;
    }

    @Override
    public void run() {
        while (this.testSocket == null) {
            try {
                Socket socket = new Socket(Utils.getServerHostConfig(), Utils.getServerPortConfig());
                try {
                    socket.sendUrgentData(0xFF);
                    this.testSocket = socket;
                } catch (Exception e) {
                    System.out.println("Sending urgent data failed");
                }
            } catch (IOException e) {
                System.out.println("Connection status: disconnected");
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Utils.logException("InterruptedException:", e);
            }
        }

        GotchaClientApp.closeReconnecting();
        // TODO: fix this logic for teacher or student only
        TeacherClient.setUpCommunication();
    }
}
