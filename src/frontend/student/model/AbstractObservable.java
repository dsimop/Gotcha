package frontend.student.model;

/**
 * StudentObservable class combines two methods from the Observable class.
 * This is used to simplify the frontend.student model as the methods will be used interchangeably
 *
 *@author Melisha Trout
 * @version 02/03/2018
 */
public abstract class AbstractObservable extends java.util.Observable {

    /**
     * Combining the setChange and notifyObservers into one method
     */
    protected void setChangeAndNotifyObservers(){
        setChanged();
        notifyObservers();
    }
}
