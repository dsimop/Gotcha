package frontend.common.ui;

import java.util.Observable;

public class RegistrationModel extends Observable {

    public void broadCastRegSucceed() {
        setChanged();
        notifyObservers(true);
    }

    public void broadCastRegFail() {
        setChanged();
        notifyObservers(false);
    }
}
