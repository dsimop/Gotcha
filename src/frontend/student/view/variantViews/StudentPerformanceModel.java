package frontend.student.view.variantViews;


import common.model.StudentAnswer;

import java.util.ArrayList;
import java.util.Observable;

/**
 * The Student Performance Model class acts as the internal data structure for the StudentPerformance view.
 * @version 21/03/2018
 * @author Melisha Trout
 */
public class StudentPerformanceModel extends Observable {

    private ArrayList<StudentAnswer> performance = new ArrayList<>();

    /**
     * A setter for the performance field.
     * @param performance
     */
    public void setStudentPerformanceDistribution(ArrayList<StudentAnswer> performance) {
        this.performance = performance;
        setChanged();
        notifyObservers();
    }


    /**
     * Getter for performance
     * @return An Arraylist given as a StudentPerformanceDistribution object.
     *
     */
    public ArrayList<StudentAnswer> getPerformance() {
        return performance;
    }
}
