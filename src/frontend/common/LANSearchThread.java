package frontend.common;

import frontend.common.ui.ServerInputPanelModel;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Author Yang He
 * @Version 2018/3/14
 */
public class LANSearchThread extends Thread {
    private ServerInputPanelModel serverInputPanelModel;
    private int port;

    public LANSearchThread(ServerInputPanelModel serverInputPanelModel, int port) {
        this.serverInputPanelModel = serverInputPanelModel;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String addrs = addr.getHostAddress();

            int k=addrs.lastIndexOf(".");
            String ss = addrs.substring(0,k+1);

            for(int i=1; i <= 255; i++) {
                String ip = ss + i;
                new Thread(() -> {
                    try{
                        Socket socket = new Socket(ip, port);
                        socket.sendUrgentData(0xFF);
                        System.out.println("Server found: " + ip);
                        String[] server = {ip, Integer.toString(port)};
                        this.serverInputPanelModel.addServer(server);
                    }catch(Exception e){
                    }
                }).start();
            }
        } catch (UnknownHostException e) {
        }
    }
}
