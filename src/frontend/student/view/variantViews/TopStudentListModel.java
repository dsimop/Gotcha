package frontend.student.view.variantViews;

import common.model.User;

import java.util.ArrayList;
import java.util.Observable;

/**
 * The TopStudentListModel acts as the internal data structure for the TopStudentList view.
 * @author Melisha Trout, Daohan Chong
 * @version 18/03/2018
 */
public class TopStudentListModel extends Observable {

    private ArrayList<User> currentTop5Students = new ArrayList<>();


    /**
     * Recieves an arraylist given as a User object of the top 5 users.
     * @param users An arraylist of users.
     */
    public void top5Students(ArrayList<User> users) {
        currentTop5Students = users;
        setChanged();
        notifyObservers();
    }

    /**
     * A getter for the currentTop5Students
     * @return The current top 5 users.
     */
    public ArrayList<User> getCurrentTop5Students() {
        return currentTop5Students;
    }
}
