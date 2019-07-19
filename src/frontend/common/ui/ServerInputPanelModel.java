package frontend.common.ui;

import frontend.common.LANSearchThread;

import java.util.ArrayList;
import java.util.Observable;

/**
 * @Author Yang He
 * @Version 2018/3/14
 */
public class ServerInputPanelModel extends Observable {
    private ArrayList<String[]> serverList;

    public ServerInputPanelModel() {
        this.serverList = new ArrayList<>();
    }

    public ArrayList<String[]> getServerList() {
        return serverList;
    }

    public void searchServer(int port) {
        this.serverList = new ArrayList<>();
        setChanged();
        notifyObservers();

        new Thread(new LANSearchThread(this, port)).start();
    }

    public void addServer(String[] server) {
        serverList.add(server);
        setChanged();
        notifyObservers();
    }
}
