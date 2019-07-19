package frontend.student.view;

import common.model.Question;
import frontend.student.model.QuestionPanelModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * The Questionpanel is the main frame for the question quiz for the frontend.student
 * client
 *
 * @author Melisha Trout, Dimitrios Simopoulos
 * @version 04/03/18
 */
public class QuestionPanel extends JPanel implements Observer {

    private JButton answerButton1;
    private JButton answerButton2;
    private JButton answerButton3;
    private JButton answerButton4;
    private JTextArea questionTextarea;

    /**
     * Construct the QuestionPanel with the corresponding model
     *
     * @param questionPanelModel The model for this view
     */
    public QuestionPanel(QuestionPanelModel questionPanelModel) {
        questionPanelModel.addObserver(this);

        setLayout(null);

        answerButton1 = new JButton("New button1");
        answerButton1.setFont(new Font("Comic Sans MS", Font.BOLD, 12));

        answerButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ansTxt1 = answerButton1.getText();

                //check is the clicked answer is correct
                questionPanelModel.checkClickedAnswer(ansTxt1);
                //send users response to the database
                questionPanelModel.sendUsersAnswer2D(0);

            }
        });
        answerButton1.setBounds(111, 474, 310, 69);
        add(answerButton1);

        answerButton2 = new JButton("New button2");
        answerButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ansTxt1 = answerButton2.getText();

                //check is the clicked answer is correct
                questionPanelModel.checkClickedAnswer(ansTxt1);
                //send users response to the database
                questionPanelModel.sendUsersAnswer2D(1);

            }
        });
        answerButton2.setBounds(609, 474, 262, 61);
        answerButton2.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        add(answerButton2);

        answerButton3 = new JButton("New button3");
        answerButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String ansTxt1 = answerButton3.getText();

                //check is the clicked answer is correct
                questionPanelModel.checkClickedAnswer(ansTxt1);
                //send users response to the database
                questionPanelModel.sendUsersAnswer2D(2);
            }
        });
        answerButton3.setBounds(111, 312, 310, 69);
        answerButton3.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        add(answerButton3);

        answerButton4 = new JButton("New button4");
        answerButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ansTxt1 = answerButton4.getText();
                //check is the clicked answer is correct
                questionPanelModel.checkClickedAnswer(ansTxt1);
                //send users response to the database
                questionPanelModel.sendUsersAnswer2D(3);

            }
        });
        answerButton4.setBounds(609, 312, 262, 69);
        answerButton4.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        add(answerButton4);

        questionTextarea = new JTextArea();
        questionTextarea.setFont(new Font("Lato Semibold", Font.PLAIN, 12));
        questionTextarea.setBounds(149, 50, 665, 232);
        add(questionTextarea);

    }


    @Override
    public void update(Observable o, Object arg) {

        final Object finalArg = arg;
        System.out.println(finalArg);
        if (finalArg instanceof String) {
            this.questionTextarea.append(finalArg.toString());
            this.questionTextarea.getLineWrap();
        }
        if (finalArg instanceof Question) {
            answerButton1.setText(((Question) finalArg).getAnswers().get(0).getAnswerText());
            answerButton2.setText(((Question) finalArg).getAnswers().get(1).getAnswerText());
            answerButton3.setText(((Question) finalArg).getAnswers().get(2).getAnswerText());
            answerButton4.setText(((Question) finalArg).getAnswers().get(3).getAnswerText());
            questionTextarea.setText(((Question) finalArg).getQuestionText());
        }
        if (finalArg instanceof Boolean) {
            if ((Boolean) finalArg == true) {
                showMessageDialog(this, "Correct!", "Information",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                showMessageDialog(javax.swing.JOptionPane.getRootFrame(), "That was the wrong answer!",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}