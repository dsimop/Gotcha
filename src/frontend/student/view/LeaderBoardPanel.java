package frontend.student.view;

import frontend.student.model.GameState;
import frontend.student.model.LeaderBoardModel;
import frontend.student.view.variantViews.TopStudentList;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static frontend.GotchaClientApp.frame;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * The LeaderBoardPanel shows the top 5 students who have the most correctly answered questions during the quiz.
 * @Author Melisha Trout
 * @Version 16/03/2018
 */
public class LeaderBoardPanel extends JPanel implements Observer {
    private LeaderBoardModel leaderBoardModel;
    private TopStudentList topStudentList;
    private Panel optionsNav;
    private JPanel homePanel;
    private Button goHome;
    private JPanel performancePanel;
    private Button viewPerformance;
    private JLabel label;
    private Panel tablePanel;
    private FlowLayout f_tablePanel;


    //Connect the panel to the right model
    public LeaderBoardPanel(LeaderBoardModel leaderBoardModel) {
        this.leaderBoardModel = leaderBoardModel;
        this.leaderBoardModel.addObserver(this);
        setUPView();
    }

    public void setUPView() {
        setBackground(new Color(255, 255, 255));
        setLayout(new BorderLayout(0, 0));

        this.topStudentList = new TopStudentList();

        //Add the TopStudentList panel to the center
        this.tablePanel = new Panel();
        this.f_tablePanel = (FlowLayout) tablePanel.getLayout();
        this.tablePanel.add(this.topStudentList);
        add(this.tablePanel, BorderLayout.CENTER);


        //add a panel for the buttons i.e. home/performance
        this.optionsNav = new Panel();
        this.optionsNav.setSize(this.getWidth(), 400);
        this.optionsNav.setBackground(new Color(255, 255, 255));
        add(this.optionsNav, BorderLayout.SOUTH);
        this.optionsNav.setLayout(new BoxLayout(this.optionsNav, BoxLayout.X_AXIS));

        this.homePanel = new JPanel();
        this.homePanel.setBackground(new Color(255, 255, 255));
        this.optionsNav.add(homePanel);
        this.homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.X_AXIS));

        this.goHome = new Button("Home");
        this.goHome.addActionListener(e -> {
            leaderBoardModel.goBack2Home();
        });
        this.homePanel.add(this.goHome);

        this.goHome.setFont(new Font("Consolas", Font.BOLD, 20));
        this.goHome.setBackground(new Color(255, 255, 255));

        this.performancePanel = new JPanel();
        this.performancePanel.setBackground(new Color(255, 255, 255));
        this.optionsNav.add(this.performancePanel);
        this.performancePanel.setLayout(new BoxLayout(this.performancePanel, BoxLayout.X_AXIS));

        this.viewPerformance = new Button("Performance");
        this.viewPerformance.addActionListener(e -> leaderBoardModel.goToStudentPerformance());
        this.performancePanel.add(this.viewPerformance);
        this.viewPerformance.setFont(new Font("Consolas", Font.BOLD, 20));
        this.viewPerformance.setBackground(new Color(255, 255, 255));

        this.label = new JLabel("Leader Board");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Consolas", Font.BOLD, 35));
        add(label, BorderLayout.NORTH);
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
        if( arg instanceof Enum ) {
            System.out.println("Update: " + arg);

            if(arg == GameState.PERFORMANCE){
                SwingUtilities.invokeLater(() -> {
                    System.out.println("Going from Leader board to Student performance");
                    frame.remove(frame.leaderBoardPanel);
                    frame.setContentPane(frame.performancePanel);
                    frame.setTitle("StudentClient: Performance");
                    frame.revalidate();
                    System.out.println("finished");
                });
            }
            if(arg == GameState.STARTGAME){
                SwingUtilities.invokeLater(() -> {
                    showMessageDialog(this, "You have not completed the quiz \n" +
                                    "Please start the quiz to view your performance.",
                            "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                });

            }
            if(arg == GameState.HOME){
                SwingUtilities.invokeLater(() -> {
                    System.out.println("Going from leader board to student home.");
                    frame.remove(frame.leaderBoardPanel);
                    frame.setContentPane(frame.studentHome);
                    frame.setTitle("StudentClient: Home");
                    frame.revalidate();
                    System.out.println("finished");
                });

            }


        }

    }
}
