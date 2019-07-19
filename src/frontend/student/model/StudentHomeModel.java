package frontend.student.model;

import common.Message;
import common.model.User;

import java.util.ArrayList;
import java.util.Observable;

import static frontend.GotchaClientApp.studentClientCommunicationThread;
import static frontend.student.model.GameState.*;

/**
 * THe StudentHomeModel class acts as an internal data structure for the Student Home Model
 */
public class StudentHomeModel extends Observable {

    private ArrayList<String> currentUser = new ArrayList<>();
    private int UID;
    private String username;
    public boolean isInView = false;
    private GameState gameState;


    public ArrayList<String> getCurrentUserDetials(Message<User> users) {
        this.UID = users.getData().getUid();
        String uID = Integer.toString(this.UID);
        this.username = users.getData().getUsername();
        String currentUsersName = this.username;
        this.currentUser.add(uID);
        this.currentUser.add(currentUsersName);
        return this.currentUser;
    }

    public int getCurrentUsersID() {
        return this.UID;
    }

    public String getCurrentUsersUsername() {
        return this.username;
    }

    /**
     * send the user's name to the homescreen.
     */
    public void updateStudentHomeName() {
        //retrieve the username of the current user
        String username = this.getCurrentUsersUsername();
        //notify the users
        isInView = true;
        setChanged();
        notifyObservers(username);
    }

    /**
     * Send a message to the server to retrieve the top 5 students
     */
    public void getTopStudentsFromServer() {
        studentClientCommunicationThread.getTop5Students();
    }

    /**
     * change the panel to the Leader board
     */

    public void goToLeaderBoard() {
        //update the leader board
        getTopStudentsFromServer();
        gameState = LEADERBOARD;
        setChanged();
        notifyObservers(LEADERBOARD);
    }

    public void loadQuestions4Student() {
        gameState = STARTGAME;
        studentClientCommunicationThread.getQuestion4User(getCurrentUsersID());
        setChanged();
        notifyObservers(STARTGAME);
    }


    public void goBackToHome() {
        gameState = HOME;
        setChanged();
        notifyObservers(HOME);
    }

}
