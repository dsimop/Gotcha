package frontend.student.view.variantViews;

import common.model.StudentAnswer;
import frontend.student.model.StudentClientRootModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * The Student Performance class shows a list containing the question text, the user's answers for each question,
 * the correct answer for each question and the students mark
 *
 * @author Melisha Trout, Daohan Chong
 * @version 18/03/2018
 */
public class StudentPerformance extends JPanel implements Observer {

    private StudentPerformanceListModel studentPerformanceListModel;
    private StudentPerformanceModel studentPerformanceModel;
    private JScrollPane scrollPane;
    private JTable studentP;

    /**
     * Connect the view to the corresponding model
     */
    public StudentPerformance() {
        this.studentPerformanceModel = StudentClientRootModel.getSingletonInstance().getStudentPerformanceModel();
        studentPerformanceModel.addObserver(this);
        setUpViews();
    }

    //Create the view which will include the table and the title
    private void setUpViews() {
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;

        String[] colNames = new String[]{"Question", "Your Answer", "Correct?", "Correct Answer", "Your Mark"};
        this.studentPerformanceListModel = new StudentPerformanceListModel(
                usersInfo2TableData(studentPerformanceModel.getPerformance()),
                colNames);


        studentP = new JTable(this.studentPerformanceListModel);

        studentP.setRowSelectionAllowed(false);
        studentP.setShowGrid(false);
        studentP.setShowHorizontalLines(false);
        studentP.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        studentP.getColumnModel().setColumnSelectionAllowed(false);
        studentP.setFillsViewportHeight(true);
        studentP.setCellSelectionEnabled(false);
        studentP.setEnabled(false);
        studentP.getColumnModel().getColumn(0).setWidth(300);
        studentP.getColumnModel().getColumn(1).setPreferredWidth(322);
        studentP.setRowHeight(35);


        setForeground(new Color(255, 255, 255));
        setBackground(new Color(255, 255, 255));

        Panel tableTitlePanel = new Panel();
        tableTitlePanel.setForeground(new Color(255, 255, 255));
        tableTitlePanel.setBackground(new Color(255, 255, 255));
        FlowLayout flowLayout_1 = (FlowLayout) tableTitlePanel.getLayout();
        flowLayout_1.setAlignOnBaseline(true);
        add(tableTitlePanel, c);

        Label tableTitle = new Label("Your current performance");
        tableTitlePanel.add(tableTitle);
        tableTitle.setBackground(new Color(255, 255, 255));
        tableTitle.setForeground(new Color(0, 0, 0));
        tableTitle.setFont(new Font("Consolas", Font.BOLD, 25));
        tableTitle.setAlignment(Label.CENTER);



        studentP.getColumnModel().getColumn(0).setResizable(false);
        studentP.getColumnModel().getColumn(0).setPreferredWidth(114);
        studentP.getColumnModel().getColumn(1).setPreferredWidth(322);
        studentP.setShowVerticalLines(false);

        scrollPane = new JScrollPane(studentP, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        c.gridy = 1;
        c.weightx = 0.8;
        add(scrollPane, c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
     * Retrieve the elements within an arraylist and add it to a 2d array.
     *
     * @param performance A performance object given as an arraylist
     * @return A 2d array, given as a string, containing the student's performance
     */
    private String[][] usersInfo2TableData(ArrayList<StudentAnswer> performance) {
        String[][] data = new String[performance.size()][5];

        //Extract the relevant ArrayList from the performance
        int p = 0;
        for (StudentAnswer s : performance) {
            data[p][0] = s.getQuestionText();
            data[p][1] = s.getUserAnswerText();
            data[p][2] = s.getUserAnswer().isCorrect() ? "correct" : "wrong";
            data[p][3] = s.getUserAnswer().isCorrect() ? "" : s.getCorrectAnswerText();
            data[p][4] = s.getUserAnswer().isCorrect() ? Integer.toString(s.getUserAnswer().getMark()) : "0";
            p++;
        }

        return data;
    }

    /**
     * Private inner class that extends DefaultTableModel. This model will act as the middle guy between the
     * StudentPerformanceModel and this class. It receives the updated list of students via the update method within the
     * main class, notifies the table that the data has changed.
     */
    private class StudentPerformanceListModel extends DefaultTableModel {
        private Object[] columnNames;

        public StudentPerformanceListModel(Object[][] data, Object[] columnNames) {
            this.columnNames = columnNames;
            setDataVector(data, columnNames);
        }

        private void updateTableWithNewData(ArrayList<StudentAnswer> performance) {
            setDataVector(usersInfo2TableData(performance), this.columnNames);
            fireTableDataChanged();
        }
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
        SwingUtilities.invokeLater(() -> {
            System.out.println("update: " + arg + "\n" + studentPerformanceModel.getPerformance());
            this.studentPerformanceListModel.updateTableWithNewData(studentPerformanceModel.getPerformance());
            studentP.getColumnModel().getColumn(0).setMinWidth(320);
        });
    }

}
