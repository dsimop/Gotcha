package frontend.teacher.view.fragment;

import common.model.Question;
import frontend.teacher.model.QuestionPanelModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * The UI for QuestionList
 *
 * @author Daohan Chong
 * @version 2018-02-28
 */
public class QuestionList extends JPanel implements Observer {

    private JTable questionTable;
    private MyTableModel defaultTableModel;
    private JButton newQuestionButton = new JButton("New");
    private JButton deleteQuestionButton = new JButton("Delete");

    private int selectedRowIndex = 0;
    private QuestionPanelModel questionPanelModel;

    // This field variable is mainly used for determining whether the list is actually updated
    private final ArrayList<Question> tableQuestions = new ArrayList<>();

    public QuestionList(QuestionPanelModel questionPanelModel) {
        this.questionPanelModel = questionPanelModel;
        questionPanelModel.addObserver(this);

        // set layout
        setLayout(new GridBagLayout());

        // set up questionTable
        String[][] tableData = QuestionPanelModel.questions2TableData(tableQuestions);
        defaultTableModel = new MyTableModel(tableData, new String[]{"ID", "Question Text", "Mark"});
        questionTable = new JTable(defaultTableModel);
        questionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionTable.setDefaultEditor(Object.class, null); // disable editing
        questionTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        questionTable.getColumnModel().getColumn(1).setMinWidth(50);
        questionTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        // fix size
        questionTable.setPreferredScrollableViewportSize(new Dimension(200, (int) questionTable.getPreferredSize().getHeight())); // it works!

        questionTable.getSelectionModel().addListSelectionListener(e -> {
            selectedRowIndex = questionTable.getSelectedRow();
            questionPanelModel.setSelectedQuestionIndex(selectedRowIndex);
            System.out.println("setSelectedQuestionIndex: " + selectedRowIndex);
        });


        final int LIST_HEIGHT_WEIGHT = 10;

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = LIST_HEIGHT_WEIGHT;
        c.weightx = 1.0;
        c.weighty = 1.0;
        // Note: JTable must be wrapped by JScrollPane
        add(new JScrollPane(questionTable), c);

        c.gridy = LIST_HEIGHT_WEIGHT;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.CENTER;
        add(newQuestionButton, c);

        c.gridx = 1;
        c.gridx = 2;
        add(deleteQuestionButton, c);

        setUpButtonListeners();
    }

    private void setUpButtonListeners() {
        newQuestionButton.addActionListener(e -> questionPanelModel.newQuestion());
        deleteQuestionButton.addActionListener(e -> {
            int selectedIndex = questionPanelModel.getSelectedQuestionIndex();
            Question selectedQuestion = questionPanelModel.getSelectedQuestion();
            if (selectedIndex == -1 || selectedQuestion == null) {
                JOptionPane.showMessageDialog(null, "Error: No question is selected.");
                return;
            }
            questionPanelModel.deleteSelectedQuestion();
        });
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        // Clear the selection while the selected index is null
        if (questionPanelModel.getSelectedQuestionIndex() == null) {
            questionTable.clearSelection();
        }

        if (!tableQuestions.equals(questionPanelModel.getQuestions())) {
            tableQuestions.clear();
            tableQuestions.addAll(questionPanelModel.getQuestions());
            defaultTableModel.updateData(tableQuestions);
        }

    }

    /**
     * Customer table model for updating data
     */
    private class MyTableModel extends DefaultTableModel {

        private Object[] tempColumnNames;

        public MyTableModel(Object[][] data, Object[] columnNames) {
            tempColumnNames = columnNames;
            setDataVector(data, columnNames);
        }

        private void updateData(ArrayList<Question> questions) {
            setDataVector(QuestionPanelModel.questions2TableData(questions), tempColumnNames);
            fireTableDataChanged();
        }
    }
}
