package common.model;


import java.util.ArrayList;


/**
 * This class is used to set/retrieve the Questions that the student participated in, which includes their answer,
 * the correct answer, and their mark for each question
 *
 * @author Melisha Trout
 * @version 21/03/2018
 */
public class StudentPerformanceDistribution {
    private ArrayList<Question> questionText;
    private ArrayList<UserAnswer> usersAnswerText;
    private ArrayList<Answer> correctAnswerText;
    private ArrayList<Integer> usersMark;


    /**
     * A constructor for the class
     * @param questionText An arraylist of questions that the student answered during the quiz
     * @param usersAnswerText An arraylist of the students answers for each question
     * @param correctAnswerText An arraylist of the correct answer for each question
     * @param usersMark An arrayList of the marks for each question given to the student
     */
    public StudentPerformanceDistribution(ArrayList<Question> questionText, ArrayList<UserAnswer> usersAnswerText,
                                          ArrayList<Answer> correctAnswerText, ArrayList<Integer> usersMark) {
        this.questionText = questionText;
        this.usersAnswerText = usersAnswerText;
        this.correctAnswerText = correctAnswerText;
        this.usersMark = usersMark;
    }

    /**
     * Getter for the questions
     * @return
     */
    public ArrayList<Question> getQuestion() {
        return questionText;
    }

    /**
     * Getter for the users answers
     * @return
     */
    public ArrayList<UserAnswer> getUsersAnswer() {
        return usersAnswerText;
    }

    /**
     * Getter for the correct answers
     * @return
     */
    public ArrayList<Answer> getCorrectAnswer() {
        return correctAnswerText;
    }

    /**
     * A getter for the user's mark
     * @return
     */
    public ArrayList<Integer> getUsersMark() {
        return usersMark;
    }



}
