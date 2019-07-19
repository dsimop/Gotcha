package frontend.student.view;

import frontend.student.view.variantViews.StudentPerformance;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static frontend.GotchaClientApp.frame;

public class PerformancePanel extends JPanel implements Observer {

    private StudentPerformance studentPerformance;


    public PerformancePanel() {
        setLayout(new GridBagLayout());
        setSize(1000, 600);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;

        this.studentPerformance = new StudentPerformance();
        studentPerformance.setSize(new Dimension(1000, 300));

        setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(150, 80));
        titlePanel.setBackground(Color.WHITE);


        titlePanel.setLayout(new BorderLayout(0, 0));

        JLabel title = new JLabel("Your Performance");
        title.setLabelFor(titlePanel);
        title.setHorizontalTextPosition(SwingConstants.CENTER);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 29));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);

        JPanel backHomePanel = new JPanel();
        backHomePanel.setBackground(Color.WHITE);
        backHomePanel.setPreferredSize(new Dimension(10, 80));
        backHomePanel.setLayout(new BorderLayout(0, 0));

        JButton goBackHome = new JButton("Back");
        backHomePanel.add(goBackHome, BorderLayout.EAST);
        goBackHome.setBackground(new Color(0, 204, 0));
        goBackHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        goBackHome.addActionListener(e -> {
            frame.remove(frame.performancePanel);
            frame.setContentPane(frame.leaderBoardPanel);
            frame.setTitle("StudentClient: Home");
            frame.revalidate();
        });


        add(titlePanel, c);

        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 1;
        c.ipadx = 1;
        c.weighty = 1;
        c.weightx = 1;
        add(studentPerformance, c);

        c.gridy = 2;
        c.ipady = 0;
        c.ipadx = 0;
        c.weighty = 0.0;
        c.weightx = 0.0;
        add(backHomePanel, c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println("Performance Panel size: " + getSize());
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
}
