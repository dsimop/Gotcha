package common.model;

/**
 * Answer is the data structure of a row in answers table in the database
 *
 * @author Daohan Chong, Yang He, Dimitrios Simopoulos, Mel Trout
 * @version 2018-02-24
 */
public class Answer {
    private Integer aid;
    private Integer qid;
    private String answerText;
    private Boolean isCorrect;


    /**
     * Get the answer id
     *
     * @return the answer id
     */
    public Integer getAid() {
        return aid;
    }

    /**
     * Set the answer id
     *
     * @param aid is the new answer id
     */
    public void setAid(Integer aid) {
        this.aid = aid;
    }

    /**
     * Get the question id
     *
     * @return the question id
     */
    public Integer getQid() {
        return qid;
    }

    /**
     * Set the question id
     *
     * @param qid is the new question id
     */
    public void setQid(Integer qid) {
        this.qid = qid;
    }

    /**
     * Get the answer text
     *
     * @return the answer text
     */
    public String getAnswerText() {
        return answerText;
    }

    /**
     * Set the answer text
     *
     * @param answerText is the new answer text
     */
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    /**
     * Get the boolean value indicating whether the answer is correct
     *
     * @return whether the answer is correct
     */
    public Boolean getCorrect() {
        return isCorrect;
    }

    /**
     * Set the boolean value indicating whether the answer is correct
     *
     * @param correct is the new boolean value
     */
    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "aid=" + aid +
                ", qid=" + qid +
                ", answerText='" + answerText + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
