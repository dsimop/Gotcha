package frontend.teacher.view.fragment;

import common.Utils;
import common.model.Answer;
import frontend.teacher.model.AnswerPanelModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observer;

/**
 * @author Daohan Chong
 * @version 2018-03-06
 */
public class AnswerList extends JPanel implements Observer {

    private AnswerPanelModel answerPanelModel;

    private JTable answerTable;
    private AnswerTableModel defaultTableModel;
    private JButton newAnswerButton = new JButton("+");
    private JButton deleteAnswerButton = new JButton("-");
    private JButton editAnswerButton = new JButton("Edit");

    public AnswerList(AnswerPanelModel answerPanelModel) {
        this.answerPanelModel = answerPanelModel;
        answerPanelModel.addObserver(this);

        setUpViews();
    }

    private void setUpViews() {
        defaultTableModel = new AnswerTableModel(answers2TableData(answerPanelModel.getAnswers()),
                new String[]{"ID", "Text", "Correct"});
        answerTable = new JTable(defaultTableModel);
        answerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        answerTable.setDefaultEditor(Object.class, null); // disable editing
        answerTable.setPreferredSize(new Dimension(480, 300));
        answerTable.setPreferredScrollableViewportSize(new Dimension(480,
                (int) answerTable.getPreferredSize().getHeight())); // it works!
        answerTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        answerTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRowIndex = answerTable.getSelectedRow();
            answerPanelModel.setSelectedAnswerIndex(selectedRowIndex);
            Utils.logInfo("setSelectedAnswerIndex: " + selectedRowIndex);
        });

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(answerTable), c);

        c.gridy = 1;
        c.gridx = 0;

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(newAnswerButton);
        buttonsPanel.add(deleteAnswerButton);
        buttonsPanel.add(editAnswerButton);
        add(buttonsPanel, c);


        // buttons
        newAnswerButton.setEnabled(false);
        deleteAnswerButton.setEnabled(false);
        editAnswerButton.setEnabled(false);

        setUpButtonActionListeners();
    }

    private void setUpButtonActionListeners() {
        deleteAnswerButton.addActionListener(e -> {
            if (answerPanelModel.hasSelectedAnswer()) {
                answerPanelModel.deleteSelectedAnswer();
            } else {
                JOptionPane.showMessageDialog(null, "Error: No answer is selected.");
            }
        });

        newAnswerButton.addActionListener(e -> {
            if (answerPanelModel.getAnswers().size() >= 4) {
                JOptionPane.showMessageDialog(null, "Error: The max number of answers is 4.");
            } else if (answerPanelModel.hasPersistentQuestion()) {
                displayAnswerEditor(null);
            } else {
                JOptionPane.showMessageDialog(null, "Error: No persistent question is selected.");
            }
        });

        editAnswerButton.addActionListener(e -> {
            if (answerPanelModel.hasSelectedAnswer()) {
                displayAnswerEditor(answerPanelModel.getAnswers().get(answerPanelModel.getSelectedAnswerIndex()));
            } else {
                JOptionPane.showMessageDialog(null, "Error: No answer is selected.");
            }
        });
        // displayAnswerEditor for double-click event of the table
        answerTable.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable table = (JTable) e.getSource();
                    Point point = e.getPoint();
                    int row = table.rowAtPoint(point);
                    displayAnswerEditor(answerPanelModel.getAnswers().get(row));
                    // FIXME: exception here or just remove this double-click feature
                }
            }
        });
    }

    private String[][] answers2TableData(ArrayList<Answer> answers) {
        String[][] strings = new String[answers.size()][3];

        int p = 0;
        for (Answer answer : answers) {
            strings[p][0] = Integer.toString(answer.getAid());
            strings[p][1] = answer.getAnswerText();
            strings[p][2] = Boolean.toString(answer.getCorrect());
            p++;
        }

        return strings;
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
    public void update(java.util.Observable o, Object arg) {
        if (answerPanelModel.getQuestionID() == null || answerPanelModel.getQuestionID() <= 0) {
            // empty selection or new question without qid
            newAnswerButton.setEnabled(false);
            deleteAnswerButton.setEnabled(false);
            editAnswerButton.setEnabled(false);

            defaultTableModel.updateData(answerPanelModel.getAnswers());
        } else {
            newAnswerButton.setEnabled(true);
            deleteAnswerButton.setEnabled(true);
            editAnswerButton.setEnabled(true);

            defaultTableModel.updateData(answerPanelModel.getAnswers());
        }
    }

    private class AnswerTableModel extends DefaultTableModel {
        private Object[] tempColumnNames;

        public AnswerTableModel(Object[][] data, Object[] columnNames) {
            tempColumnNames = columnNames;
            setDataVector(data, columnNames);
        }

        private void updateData(ArrayList<Answer> answers) {
            setDataVector(answers2TableData(answers), tempColumnNames);
            fireTableDataChanged();

            System.out.println("fireTableDataChanged");
            System.out.println(answers);
        }
    }

    private void displayAnswerEditor(Answer answer) {
        String editorTitle = answer == null ? "Add a new answer" : "Edit a answer";

        String[] items = {"True", "False",};
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setSelectedIndex(answer != null && answer.getCorrect() ? 0 : 1);
        JTextField answerTextField = new JTextField(answer == null ? "" : answer.getAnswerText());
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Answer text:"));
        panel.add(answerTextField);
        panel.add(new JLabel("Correct answer:"));
        panel.add(combo);
        int result = JOptionPane.showConfirmDialog(this, panel, editorTitle,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {

            if (answer == null) {
                // create a new answer
                answer = new Answer();
            }

            answer.setQid(answerPanelModel.getQuestionID());
            answer.setCorrect(Objects.equals(combo.getSelectedItem(), "True"));
            answer.setAnswerText(answerTextField.getText());

            if (answer.getAnswerText().equals("")) {
                JOptionPane.showMessageDialog(this, "Error: The answer text cannot be null");
                displayAnswerEditor(answer);
            } else {
                if (answer.getAid() == null || answer.getAid() <= 0) {
                    answerPanelModel.createNewAnswer(answer);
                } else {
                    answerPanelModel.updateExistingAnswer(answer);
                }
            }


        } else {
            System.out.println("Cancelled");
        }
    }
}
