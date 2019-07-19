package frontend.teacher.view;

import common.model.AnswerDistribution;
import frontend.teacher.model.QuestionPanelModel;
import frontend.teacher.view.fragment.QuestionEditor;
import frontend.teacher.view.fragment.QuestionList;

import javax.swing.*;
import java.awt.*;
import java.util.Observer;

/**
 * QuestionPanel contains the UI to create/edit/delete quizzes.
 *
 * @author Daohan Chong
 * @version 2018-02-28
 */
public class QuestionPanel extends JPanel implements Observer {

    private QuestionList questionList;
    private QuestionEditor questionEditor;
    private QuestionPanelModel questionPanelModel;

    /**
     * Construct the quiz panel with the corresponding view model
     *
     * @param questionPanelModel is the view model
     */
    public QuestionPanel(QuestionPanelModel questionPanelModel) {
        this.questionPanelModel = questionPanelModel;
        this.questionPanelModel.addObserver(this);

        questionList = new QuestionList(questionPanelModel);
        questionEditor = new QuestionEditor(questionPanelModel);
        // FIXME: prevent the widths of questionList and questionEditor from changing

        setLayout(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1.0;
        c.gridwidth = 1;

        add(questionList, c);

        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.BOTH;
        c2.gridx = 0;
        c2.gridx = 1;
        c2.weightx = 0.8;
//        c2.gridwidth = 100; // figure out why not work
        add(questionEditor, c2);
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
        if (arg instanceof AnswerDistribution) {
            AnswerDistribution answerDistribution = (AnswerDistribution) arg;
            if (answerDistribution.getAnswers().size() == 0) {
                JOptionPane.showMessageDialog(null, "Sorry, there is no user who has answered this question.");
            } else {
                StatisticsPanel statisticsPanel = new StatisticsPanel(answerDistribution);
                JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Statistics", true);
                dialog.setSize(600, 400);
                dialog.setPreferredSize(new Dimension(600, 400));
                dialog.setLocationRelativeTo(this);
                dialog.setLayout(new GridBagLayout());

                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weighty = 0.8;
                c.fill = GridBagConstraints.BOTH;
                dialog.add(statisticsPanel, c);

                JButton disposeButton = new JButton("Close");
                disposeButton.addActionListener(e -> {
                    dialog.dispose();
                });
                c.gridx = 0;
                c.gridy = 1;
                c.weighty = 0.2;
                c.fill = GridBagConstraints.CENTER;
                dialog.add(disposeButton, c);

                //dialog.pack();
                dialog.setVisible(true);
            }
        }
    }
}
