package frontend;

import frontend.common.ui.*;
import frontend.student.model.StudentClientRootModel;
import frontend.student.view.LeaderBoardPanel;
import frontend.student.view.PerformancePanel;
import frontend.student.view.QuestionPanel;
import frontend.student.view.StudentHome;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * this is the main JFrame for the Gotcha login-signUp
 *
 * @author Melisha Trout, Daohan Chong
 * @version 2018-03-17
 */
public class GotchaClientApp extends JFrame implements Observer {

    public static StudentClientCommunicationThread studentClientCommunicationThread;
    public LoginScreen login;
    public SignupScreen signUp;
    public QuestionPanel questionPanel;
    public LeaderBoardPanel leaderBoardPanel;
    public StudentHome studentHome;
    public PerformancePanel performancePanel;


    private static RootRegistrationModel rootRegistrationModel;
    private static StudentClientRootModel studentClientRootModel;

    private static Reconnecting reconnecting;

    public static GotchaClientApp frame = null;

    public GotchaClientApp() {
        //create new instances of the models
        rootRegistrationModel = new RootRegistrationModel();
        studentClientRootModel = new StudentClientRootModel();

        //create new instances of the panel
        this.login = new LoginScreen(studentClientRootModel.getLoginModel());
        this.signUp = new SignupScreen(studentClientRootModel.getRegistrationModel());
        this.questionPanel = new QuestionPanel(studentClientRootModel.getQuestionPanelModel());
        this.leaderBoardPanel = new LeaderBoardPanel(studentClientRootModel.getLeaderBoardModel());
        this.studentHome = new StudentHome(studentClientRootModel.getStudentHomeModel());
        this.performancePanel = new PerformancePanel();

        //set up the communication for the communication thread
        setUpCommunication();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Gotcha: Set up the server information");

        ServerInputPanelModel serverInputPanelModel = new ServerInputPanelModel();
        JPanel addressInput = new ServerInputPanel(serverInputPanelModel, frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(addressInput);
        frame.setSize(450, 260);
        frame.setPreferredSize(new Dimension(450, 260));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);
        
        frame.addComponentListener(new ComponentAdapter() 
        {  
                public void componentResized(ComponentEvent evt) {
                    Component c = (Component)evt.getSource();
                    System.out.println("resized, new size: " + c.getSize());
                }
        });
    }

    public static void startMainApp() {
        frame = new GotchaClientApp();
        SwingUtilities.invokeLater(frame::setUpGUI);
    }

    /**
     * Create the frame
     */
    public void setUpGUI() {
        frame.setSize(450, 300);
        frame.setPreferredSize(new Dimension(450, 300));
        //frame.setBounds(100, 100, 450, 300);
        frame.setLocationRelativeTo(null);
        setDefaultLookAndFeelDecorated(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(login);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);

        reconnecting = new Reconnecting(frame);
    }

    /**
     * Start a CommunicationThread in order to send and receive messages
     * asynchronously
     */
    public static void setUpCommunication() {
        studentClientCommunicationThread = new StudentClientCommunicationThread(rootRegistrationModel,
                studentClientRootModel);
        studentClientCommunicationThread.start();
    }

    /**
     * This method is called whenever the observed object is changed. An application calls an <tt>Observable</tt>
     * object's <code>notifyObservers</code> method to have all the object's observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {

    }

    // Code for displaying reconnecting panel while the connection is lost
    public static void popReconnecting() {
        reconnecting.pop();
    }

    public static void closeReconnecting() {
        reconnecting.close();
    }
}
