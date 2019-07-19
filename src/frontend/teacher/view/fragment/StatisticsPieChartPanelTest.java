package frontend.teacher.view.fragment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatisticsPieChartPanelTest {

    private ArrayList<Double> values = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();


    public void setUp() {
        values.add(1.5);
        values.add(11.0);
        values.add(27.0);

        colors.add(Color.BLACK);
        colors.add(Color.GRAY);
        colors.add(Color.RED);
    }


    public void testUI() {
        JFrame frame = new JFrame("StatisticsPieChartPanelTest");


        setUp();
        StatisticsPieChartPanel pieChartPanel = new StatisticsPieChartPanel(values, colors);

        pieChartPanel.setSize(new Dimension(300, 300));
        pieChartPanel.setPreferredSize(new Dimension(300, 300));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pieChartPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);
        frame.repaint();
        pieChartPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StatisticsPieChartPanelTest test = new StatisticsPieChartPanelTest();

            test.testUI();
        });
    }
}