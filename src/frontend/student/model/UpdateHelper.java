package frontend.student.model;

/**
 * The update helper interface allows any views that need to be updated regularly i.e. during the game_state
 *
 * @author Melisha Trout
 * @version 16/03/2018
 */
public interface UpdateHelper {

    /**
     * Update the view
     */
    void update();


    /**
     * Allow the current Thread to continue to run.
     */

    boolean stillRunning();

}

