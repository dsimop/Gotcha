package frontend.student.model;

import common.Message;
import common.Message.ServerOperations;
import common.model.Answer;
import common.model.Question;
import common.model.UserAnswer;
import frontend.GotchaClientApp;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;

import static frontend.GotchaClientApp.frame;
import static frontend.GotchaClientApp.studentClientCommunicationThread;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * @author Dimitrios Simopoulos, Melisha Trout
 */

public class QuestionPanelModel extends Observable {
    private Question question;

    /**
     * The updatePanel method first retrieves the first question question for the user from the database using the
     * user's ID and populate the questionPanel with the questions
     */
    public void updatePanel(Question question) {
        System.out.println("updatePanel: question model");
        this.question = question;
        if( question == null ) {
            GotchaClientApp.studentClientCommunicationThread.getTop5Students();
            showMessageDialog(javax.swing.JOptionPane.getRootFrame(), "The quiz has finished!", "Information",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(() -> {
                GotchaClientApp.frame.remove(frame.questionPanel);
                GotchaClientApp.frame.setContentPane(frame.leaderBoardPanel);
                GotchaClientApp.frame.revalidate();
            });
        } else {
            setChanged();
            notifyObservers(question);
        }
    }

    public void sendUsersAnswer2D(int index) {
        UserAnswer ua = new UserAnswer();
        ua.setAid(question.getAnswers().get(index).getAid());
        ua.setQid(question.getQid());
        ua.setMark(question.getMark());
        int uid = StudentClientRootModel.getSingletonInstance().getLoginModel().getCurrentUserID();
        ua.setUid(uid);
        Message<UserAnswer> newmsg = new Message<>(ServerOperations.ANSWER_QUESTION, ua, uid);
        studentClientCommunicationThread.sendMessage(newmsg);
    }


    /**
     * This method checks if the clicked answer is correct.
     *
     * @param clickedAnswer is the clicked answer
     * @return true or false depending on the answer if it is correct or not
     */
    public void checkClickedAnswer(String clickedAnswer) {
        ArrayList<Answer> answers = question.getAnswers();
        int getCorrectAns = - 1;
        for( int i = 0; i < answers.size(); i++ ) {
            if( answers.get(i).getCorrect() == true ) {
                getCorrectAns = i;
                break;
            }
        }
        if( clickedAnswer.equals(answers.get(getCorrectAns).getAnswerText()) ) {
            System.out.println("correct");
            setChanged();
            notifyObservers(true);
        } else {
            System.out.println("false");
            setChanged();
            notifyObservers(false);
        }
    }

}