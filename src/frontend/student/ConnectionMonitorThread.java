package frontend.student;

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

    public ConnectionMonitorThread(){
        this.testSocket = null;
    }

    @Override
    public void run(){
        while(this.testSocket == null){
            try{
                Socket socket = new Socket(Utils.getServerHostConfig(), Utils.getServerPortConfig());
                try{
                    socket.sendUrgentData(0xFF);
                    this.testSocket = socket;
                }catch(Exception e){
                    Utils.logException("Sending urgent data failed: ", e);
                }
            }catch(IOException e){
                Utils.logException("Connection status: disconnected: ", e);
            }

            try{
                Thread.sleep(3000);
            }catch(InterruptedException e){
                Utils.logException("InterruptedException: ", e);
            }
        }

        GotchaClientApp.closeReconnecting();
        GotchaClientApp.setUpCommunication();
    }
}
