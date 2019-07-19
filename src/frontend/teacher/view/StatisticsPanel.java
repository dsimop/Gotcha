package frontend.teacher.view;

import common.model.Answer;
import common.model.AnswerDistribution;
import common.model.UserAnswer;
import frontend.teacher.view.fragment.StatisticsPieChartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * StatisticsPanel could display the statistics for a single question
 *
 * @author Yang He, Daohan Chong
 * @version 2018-03-16
 */
public class StatisticsPanel extends JPanel {

    private StatisticsPieChartPanel pieChartPanel;
    private JLabel[] answerLabels = new JLabel[4];
    private UserAnswerTableModel userAnswerTableModel;
    private JTable userAnswerTable;
    private ArrayList<UserAnswer> userAnswers;

    private int colorIdx = 0;
    private Color[] normalColors = new Color[]{
            Color.ORANGE,
            Color.MAGENTA,
            Color.PINK,
    };

    public StatisticsPanel(AnswerDistribution answerDistribution) {
        JPanel statistics = new JPanel();
        statistics.setSize(new Dimension(180, 300));
        statistics.setPreferredSize(new Dimension(180, 300));
        statistics.setLayout(new BoxLayout(statistics, BoxLayout.Y_AXIS));

        // initialise labels
        for (int i = 0; i < 4; i++) {
            JLabel label = new JLabel("label: " + i);
            answerLabels[i] = label;
        }

        ArrayList<Color> colors = new ArrayList<>();
        int pointer = 0;
        for (Answer a : answerDistribution.getAnswers()) {
            Color color = a.getCorrect() ? getTrueColor() : getNormalColor();
            colors.add(color);
            String text = a.getAnswerText() + (a.getCorrect() ? " (correct)" : "");
            answerLabels[pointer].setText(text);
            answerLabels[pointer].setForeground(color);
            pointer++;
        }

        ArrayList<Double> counts = new ArrayList<>();
        for (Integer c : answerDistribution.getCounts()) {
            counts.add(new Double(c));
        }

        pieChartPanel = new StatisticsPieChartPanel(counts, colors);
        pieChartPanel.setSize(new Dimension(200, 200));
        pieChartPanel.setPreferredSize(new Dimension(200, 200));
        pieChartPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        statistics.add(Box.createRigidArea(new Dimension(0, 5)));
        statistics.add(pieChartPanel);
        statistics.add(Box.createRigidArea(new Dimension(0, 20)));

        for (JLabel label : answerLabels) {
            statistics.add(label);
        }

        userAnswerTableModel = new UserAnswerTableModel(new String[0][3], new String[]{"Student", "Answer", "Result"});
        userAnswerTableModel.updateData(answerDistribution.getResult());
        userAnswerTable = new JTable(userAnswerTableModel);
        userAnswerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userAnswerTable.setDefaultEditor(Object.class, null); // disable editing
        userAnswerTable.setPreferredSize(new Dimension(300, 300));
        userAnswerTable.setPreferredScrollableViewportSize(new Dimension(300,
                (int) userAnswerTable.getPreferredSize().getHeight()));
        userAnswerTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        JScrollPane listPanel = new JScrollPane(userAnswerTable);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(statistics, c);
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0,30,0,0);
        add(listPanel, c);
    }

    private Color getNormalColor() {
        Color ret = normalColors[colorIdx];
        colorIdx++;
        return ret;
    }

    private Color getTrueColor() {
        return new Color(26, 112, 63, 255);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private class UserAnswerTableModel extends DefaultTableModel {
        private Object[] tempColumnNames;

        public UserAnswerTableModel(Object[][] data, Object[] columnNames) {
            tempColumnNames = columnNames;
            setDataVector(data, columnNames);
        }

        private void updateData(ArrayList<UserAnswer> userAnswers) {
            setDataVector(userAnswers2TableData(userAnswers), tempColumnNames);
            fireTableDataChanged();
        }
    }

    private String[][] userAnswers2TableData(ArrayList<UserAnswer> userAnswers) {
        String[][] strings = new String[userAnswers.size()][3];

        int p = 0;
        for (UserAnswer userAnswer : userAnswers) {
            strings[p][0] = userAnswer.getUsername();
            strings[p][1] = userAnswer.getAnswer();
            strings[p][2] = userAnswer.isCorrect() ? "correct" : "wrong";
            p++;
        }

        return strings;
    }
}
