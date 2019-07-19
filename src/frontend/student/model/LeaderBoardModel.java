package frontend.student.model;

import common.Message;
import common.Message.ServerOperations;
import common.model.StudentPerformanceDistribution;
import frontend.GotchaClientApp;
import frontend.student.view.variantViews.TopStudentListModel;

import java.util.Observable;

import static frontend.student.model.GameState.HOME;
import static frontend.student.model.GameState.PERFORMANCE;
import static frontend.student.model.GameState.STARTGAME;
import static frontend.student.model.StudentClientRootModel.*;

/**
 * LeaderBoardModel that acts as the internal data structure for the LeaderBoard Panel
 *
 * @author Melisha Trout
 * @Version 16/03/2018
 */
public class LeaderBoardModel extends Observable {

    public boolean isInViewL = false;
    private GameState gameState;

    private TopStudentListModel topStudentListModel = new TopStudentListModel();


    private TopStudentListModel getTopStudentListModel() {
        return this.topStudentListModel;
    }

    /**
     * Get a single Instance of the StudentClientRootModel
     *
     * @return
     */
    private StudentClientRootModel getStudentClientRootModel() {
        return getSingletonInstance();
    }

    public void updateLeaderBoard() {
        //retrieve the username of the current user
        //notify the users
        getTopStudents();
        isInViewL = true;
        setChanged();
        notifyObservers();
    }

    /**
     * Send a message to the server to retrieve the top 5 students
     */
    public void getTopStudents() {
        GotchaClientApp.studentClientCommunicationThread.getTop5Students();
    }

    /**
     * Send the user back to the home view
     */

    public void goBack2Home() {
        getSingletonInstance().getStudentHomeModel().goBackToHome();

        gameState = HOME;
        setChanged();
        notifyObservers(HOME);
    }

    /**
     * Retrieve the current UsersID
     *
     * @return The current User's ID given as an integer.
     */
    public int getUsersID() {
        return getSingletonInstance().getStudentHomeModel().getCurrentUsersID();
    }

    /**
     * Send a message to the Student Client Communication Thread to get the student performance.
     */
    public void sendMessage2Server2GetStudentsPerformance() {
        int UID = getUsersID();
        Message<StudentPerformanceDistribution> performance = new Message<>(ServerOperations.GET_STUDENTS_PERFORMANCE,
                null, UID);
        GotchaClientApp.studentClientCommunicationThread.sendMessage(performance);
    }

    public void goToStudentPerformance() {
        if( getSingletonInstance().getStudentPerformanceModel().getPerformance() == null ) {
            gameState = GameState.STARTGAME;
            setChanged();
            notifyObservers(STARTGAME);

        } else {
            sendMessage2Server2GetStudentsPerformance();
            gameState = GameState.PERFORMANCE;
            setChanged();
            notifyObservers(PERFORMANCE);
        }
    }
}

