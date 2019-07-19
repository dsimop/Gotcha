package frontend.student.view.variantViews;

import common.model.User;
import frontend.student.model.StudentClientRootModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Top frontend.student list will hold the top 5 students from the quiz. pre-condition: The students must have completed the quiz
 * and their marks for each must be tracked
 * <p>
 * post-condition: A table containing a list of the top 5 students and their position within the leaderboard
 *
 * @author Melisha Trout, Daohan Chong
 * @version 18/03/2018
 */
public class TopStudentList extends JPanel implements Observer {

    private TopStudentModel topStudentModel;
    private TopStudentListModel topStudentListModel;

    /**
     * Connect the view to the corresponding model
     */
    public TopStudentList() {
        this.topStudentListModel = StudentClientRootModel.getSingletonInstance().getTopStudentListModel();
        topStudentListModel.addObserver(this);

        setUpViews();
    }

    //Create the view which will include the table and the title
    private void setUpViews() {

        String[] colNames = new String[]{"Position", "User", "Mark"};
        this.topStudentModel = new TopStudentModel(usersInfo2TableData(topStudentListModel.getCurrentTop5Students()),
                colNames);


        JTable topStudentsTable = new JTable(this.topStudentModel);

        topStudentsTable.setRowSelectionAllowed(false);
        topStudentsTable.setShowGrid(false);
        topStudentsTable.setShowHorizontalLines(false);
        topStudentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        topStudentsTable.getColumnModel().setColumnSelectionAllowed(false);
        topStudentsTable.setFillsViewportHeight(true);
        topStudentsTable.setCellSelectionEnabled(false);
        topStudentsTable.setEnabled(false);
        topStudentsTable.getColumnModel().getColumn(0).setPreferredWidth(115);
        topStudentsTable.getColumnModel().getColumn(1).setPreferredWidth(322);
        topStudentsTable.setRowHeight(70);


        setForeground(new Color(255, 255, 255));
        setBackground(new Color(255, 255, 255));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Panel tableTiltlePanel = new Panel();
        tableTiltlePanel.setForeground(new Color(255, 255, 255));
        tableTiltlePanel.setBackground(new Color(255, 255, 255));
        FlowLayout flowLayout_1 = (FlowLayout) tableTiltlePanel.getLayout();
        flowLayout_1.setAlignOnBaseline(true);
        add(tableTiltlePanel);

        Label tableTitle = new Label("Top 5 Students");
        tableTiltlePanel.add(tableTitle);
        tableTitle.setBackground(new Color(255, 255, 255));
        tableTitle.setForeground(new Color(255, 102, 51));
        tableTitle.setFont(new Font("Consolas", Font.BOLD, 25));
        tableTitle.setAlignment(Label.CENTER);

        JPanel table_panel = new JPanel();
        table_panel.setBackground(new Color(255, 255, 255));
        add(table_panel);
        table_panel.setLayout(new BorderLayout(0, 0));

        topStudentsTable.getColumnModel().getColumn(0).setResizable(false);
        topStudentsTable.getColumnModel().getColumn(0).setPreferredWidth(114);
        topStudentsTable.getColumnModel().getColumn(1).setPreferredWidth(322);
        topStudentsTable.setShowVerticalLines(false);
        table_panel.add(topStudentsTable);

    }

    /**
     * Retrieve the elements within an arraylist and add it to a 2d array.
     *
     * @param users A user object given as an arraylist
     * @return A 2d array, given as a string, containing the top 5 students of the quiz
     */
    private String[][] usersInfo2TableData(ArrayList<User> users) {
        final String[] position = {"1st", "2nd", "3rd", "4th", "5th"};

        final String[][] delta = new String[5][3];

        for( int i = 0; i < position.length; i++ ) {
            delta[i][0] = position[i];
        }

        if( users == null ) {
        } else {
            int p = 0;
            for( User user : users ) {
                delta[p][1] = user.getUsername();
                delta[p][2] = Integer.toString(user.getMark());
                p++;
            }
            return delta;
        }
        return null;
    }

    /**
     * Private inner class that extends DefaultTableModel. This model will act as the middle guy between the
     * TopStudentListModel and this class. It receives the updated list of students via the update method within the
     * main class, notifies the table that the data has changed.
     */
    private class TopStudentModel extends DefaultTableModel {
        private String[] columnNames;

        public TopStudentModel(Object[][] data, String[] columnNames) {
            this.columnNames = columnNames;
            setDataVector(data, columnNames);
        }

        private void updateTableWithNewData(ArrayList<User> users) {
            setDataVector(usersInfo2TableData(users), columnNames);
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
            this.topStudentModel.updateTableWithNewData(topStudentListModel.getCurrentTop5Students());
        });
    }

}
