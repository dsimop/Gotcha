package frontend.teacher.view.fragment;

import common.Utils;
import common.model.Question;
import frontend.teacher.model.QuestionPanelModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * The QuestionEditor view using GridBagConstraints
 *
 * @author Daohan Chong
 * @version 2018-02-28
 */
public class QuestionEditor extends JPanel implements Observer {
    private final static int DEFAULT_EDITOR_MARK = 100;

    private JLabel idLabel = new JLabel("ID:");
    private JLabel idValue = new JLabel("[empty]");
    private JLabel questionTextLabel = new JLabel("Question text:");
    private JTextField questionTextField = new JTextField();
    private JLabel markTextFieldLabel = new JLabel("Mark:");
    private JFormattedTextField markTextfield = createMarkTextField();
    //    private JButton editQuestionButton = new JButton("Edit answers");
    private JLabel infoLabel = new JLabel("Note: only question with exactly 4 answers can be presented to students.");
    private JButton statsButton = new JButton("Statistics");
    private JButton saveButton = new JButton("Create");

    private Integer selectedIndex = null;

    private AnswerPanel answerPanel;

    private QuestionPanelModel questionPanelModel;


    /**
     * Construct the quiz editor
     *
     * @param questionPanelModel is the view model
     */
    public QuestionEditor(QuestionPanelModel questionPanelModel) {
        this.questionPanelModel = questionPanelModel;
        answerPanel = new AnswerPanel(questionPanelModel.getAnswerPanelModel());

        questionPanelModel.addObserver(this);

        // setUpButtonListeners
        setUpButtonListeners();

        // Layout and adding components
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        setBorder(new EmptyBorder(10, 25, 10, 25));

        c.fill = GridBagConstraints.HORIZONTAL;

        // row of id
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.15;
        add(idLabel, c);

        c.gridx = 1;
        c.weightx = 0.85;
        c.insets = new Insets(0, 5, 0, 0);
        add(idValue, c);

        // row of question text
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.15;
        c.insets = new Insets(0, 0, 0, 0);
        add(questionTextLabel, c);

        c.gridx = 1;
        c.weightx = 0.85;
        add(questionTextField, c);

        // row of mark
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0.15;
        add(markTextFieldLabel, c);

        c.gridx = 1;
        c.weightx = 0.85;
        add(markTextfield, c);

        c.gridx = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.EAST;
        c.anchor = GridBagConstraints.EAST;
        add(saveButton, c);

        // row of buttons
        c.gridy = 5;
//        c.gridx = 0;
        c.insets = new Insets(10, 0, 0, 0);

        c.gridx = 0;
        c.fill = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        add(infoLabel, c);

        c.ipady = 2;
        c.gridy = 6;
        c.gridwidth = 2;
        c.gridx = 0;
        statsButton.setEnabled(false);
        add(statsButton, c);

        // row of answer list
        GridBagConstraints answerConstraints = new GridBagConstraints();
        answerConstraints.gridy = 4;
        answerConstraints.gridx = 0;
        answerConstraints.gridwidth = 2;
        answerConstraints.fill = GridBagConstraints.BOTH;
        add(answerPanel, answerConstraints);
    }

    private void setCurrentQuestion(Question question) {
        idValue.setText(question == null ? "[null]" : Integer.toString(question.getQid()));
        questionTextField.setText(question == null ? "" : question.getQuestionText());
        markTextfield.setValue(question == null ? DEFAULT_EDITOR_MARK : question.getMark());
        // The default mark is 100
    }

    private void setUpButtonListeners() {
        // Save (or Create) button
        saveButton.addActionListener(e -> {
            if (questionTextField.getText().length() < 1) {
                JOptionPane.showMessageDialog(null, "Error: Question text cannot be empty.");
                return;
            }

            if (markTextfield.getText().length() < 1) {
                JOptionPane.showMessageDialog(null, "Error: Mark cannot be empty.");
                return;
            }

            if (markTextfield.getText().length() > 1 && (Integer) markTextfield.getValue() > 500) {
                JOptionPane.showMessageDialog(null, "Error: Mark cannot be larger than 500.");
                return;
            }

            Question question = new Question();
            question.setQuestionText(questionTextField.getText());
            question.setMark((Integer) markTextfield.getValue());
            Integer id = null;
            if (!isCreatingNewQuestion()) {
                id = Integer.parseInt(idValue.getText());
            }
            questionPanelModel.saveQuestion(question, id);
        });
        statsButton.addActionListener(e -> {
            if (questionPanelModel.getSelectedQuestion() != null) {
                questionPanelModel.getAnswerDistribution(questionPanelModel.getSelectedQuestion());
            } else {
                JOptionPane.showMessageDialog(null, "Error: No selected question.");
            }
        });
    }

    /**
     * @return the formatted text field which only accept integers
     */
    private static JFormattedTextField createMarkTextField() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);

        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);

        return new JFormattedTextField(numberFormatter);
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
        Utils.logInfo("questionPanelModel.getSelectedQuizIndex(): " + questionPanelModel.getSelectedQuestionIndex());
        if (!checkQuestionEqual(selectedIndex, questionPanelModel.getSelectedQuestionIndex())) {
            Utils.logInfo("QuestionEditor updated");
            setCurrentQuestion(questionPanelModel.getSelectedQuestion());
            selectedIndex = questionPanelModel.getSelectedQuestionIndex();

            if (questionPanelModel.getSelectedQuestion() != null && questionPanelModel.getSelectedQuestion().getAnswers().size() == 4) {
                statsButton.setEnabled(true);
            } else {
                statsButton.setEnabled(false);
            }

            if (isCreatingNewQuestion()) {
                saveButton.setText("Create");
            } else {
                saveButton.setText("Save");
            }
        }
    }

    /**
     * @return a boolean value indicating whether the contents of the editor is a new question
     */
    private boolean isCreatingNewQuestion() {
        return selectedIndex == null || selectedIndex == -1;
    }

    /**
     * Check whether the new index and the current are equal
     *
     * @param currentIndex the current index
     * @param newIndex     the new index
     * @return a boolean value indicating whether the new index and the current are equal
     */
    private static boolean checkQuestionEqual(Integer currentIndex, Integer newIndex) {
        if (currentIndex == null && newIndex == null) {
            return true;
        } else if (currentIndex == null || newIndex == null) {
            return false;
        } else {
            return currentIndex.equals(newIndex);
        }
    }
}
