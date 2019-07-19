package frontend.teacher.model;

import java.util.Observable;

/**
 * AbstractObservable contains common logic for Observable in this project
 *
 * @author Daohan Chong
 * @version 2018-03-01
 */
public abstract class AbstractObservable extends Observable {

    /**
     * Common logic: call setChanged() and notifyObservers()
     * This kind of operation is frequently performed when an field variable of a view model is updated.
     */
    protected void setChangedAndNotifyObservers() {
        setChanged();
        notifyObservers();
    }
}
