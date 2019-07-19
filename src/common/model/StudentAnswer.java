package common.model;

/**
 * StudentAnswer is used for Performance Panel
 * the correct answer for each question, the students mark, and the question text
 *
 * @author Melisha Trout, Daohan Chong
 * @version 22/03/2018
 */
public class StudentAnswer {
    private UserAnswer userAnswer;
    private String userAnswerText;
    private String correctAnswerText;
    private String questionText;


    public String getCorrectAnswerText() {
        return correctAnswerText;
    }

    public void setCorrectAnswerText(String correctAnswerText) {
        this.correctAnswerText = correctAnswerText;
    }

    public UserAnswer getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(UserAnswer userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUserAnswerText() {
        return userAnswerText;
    }

    public void setUserAnswerText(String userAnswerText) {
        this.userAnswerText = userAnswerText;
    }

    @Override
    public String toString() {
        return "[StudentAnswer: uid: " + userAnswer.getUid() + ", qid: " + userAnswer.getQid()
                + ", aid: " + userAnswer.getAid() + ", questionText: " + questionText
                + ", userAnswerText: " + userAnswerText + ", " + userAnswer.isCorrect() + ", "
                + "correctAnswer: " + correctAnswerText;
    }
}
