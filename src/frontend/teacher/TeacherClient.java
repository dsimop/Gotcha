package frontend.teacher;

import frontend.common.ui.ServerInputPanel;
import frontend.common.ui.ServerInputPanelModel;
import frontend.teacher.model.RootPanelModel;
import frontend.teacher.view.RootPanel;

import javax.swing.*;

/**
 * TeacherClient is the entry class for the frontend.teacher client application
 *
 * @author Daohan Chong, Yang He
 * @version 2018-03-17
 */
public class TeacherClient {

    private final static String APPLICATION_TITLE = "Gotcha: TeacherClient";

    private RootPanel rootPanel;
    public static RootPanelModel rootPanelModel;
    public static CommunicationThread communicationThread;

    public TeacherClient() {
        rootPanelModel = new RootPanelModel();
        rootPanel = new RootPanel(rootPanelModel);

        setUpCommunication();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Input the server information");

        ServerInputPanelModel serverInputPanelModel = new ServerInputPanelModel();
        JPanel addressInput = new ServerInputPanel(serverInputPanelModel, frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(addressInput);
        frame.setSize(200, 150);
        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);
    }

    /**
     * @deprecated this method has been migrated into GotchaClientApp
     */
    public static void showMainScreen() {
        final TeacherClient teacherClient = new TeacherClient();

        // Initialise the UI with help method invokeLater()
        SwingUtilities.invokeLater(teacherClient::setUpUI);
    }

    /**
     * Set up the user interface
     */
    private void setUpUI() {
        JFrame frame = new JFrame(APPLICATION_TITLE);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(rootPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * Start a CommunicationThread in order to send and receive messages asynchronously
     */
    public static void setUpCommunication() {
        communicationThread = new CommunicationThread(rootPanelModel);
        communicationThread.start();
    }



}
