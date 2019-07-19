package frontend.teacher.view.fragment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * StatisticsPieChartPanel to show statistics
 *
 * @author Daohan Chong
 * @version 2018-03-15
 */
public class StatisticsPieChartPanel extends JPanel {

    private final ArrayList<Double> values;
    private final ArrayList<Color> colors;

    public StatisticsPieChartPanel(ArrayList<Double> values, ArrayList<Color> colors) {
        this.values = values;
        this.colors = colors;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("paintComponents");

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getSize().width;
        int height = width;
        int lastAngle = -270;

        Double sum = 0.0;

        for (Double v : values) {
            sum += v;
        }

        // draw pies
        for (int i = 0; i < values.size(); i++) {
            graphics2D.setColor(colors.get(i));

            Double value = values.get(i);
            Double angle = (value / sum) * 360;

            int toValue = -angle.intValue();
            System.out.println("toValue: " + toValue);
            if (i == values.size() - 1) {
                int tempLast = lastAngle + toValue;
                int delta = -630 - tempLast;

                toValue = toValue + delta;
            }

            graphics2D.fillArc(0, 0, width, height, lastAngle, toValue);

            lastAngle = lastAngle + toValue;
        }
    }
}
