package common.model;

import common.Utils;

import java.util.ArrayList;
/**
 * Question is the data structure of a row in questions table in the database
 *
 * @author Daohan Chong, Mel Trout, Dimitrios Simopoulos, Yang He
 * @version 2018-02-24
 */
public class Question {

    private Integer qid;
    private String questionText;
    private int mark;
    private ArrayList<Answer> answers = new ArrayList<>();

    /**
     * Get the question text
     *
     * @return the question text
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Set the question text
     *
     * @param questionText is the new question text
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Get the maximum mark
     *
     * @return the maximum mark
     */
    public int getMark() {
        return mark;
    }

    /**
     * Set the maximum mark
     *
     * @param mark is the new maximum mark
     */
    public void setMark(int mark) {
        this.mark = mark;
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
     * Override toString() method for debugging
     *
     * @return the string representation fo a Question
     */
    @Override
    public String toString() {
        return "[Question qid: " + qid + ", question_text: " + questionText + ", maximum_mark: " + mark + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!obj.getClass().equals(this.getClass())) {
            return false;
        } else {
            Question toBeCompared = (Question) obj;
            boolean ret = Utils.equalsWithNulls(qid, toBeCompared.qid)
                    && Utils.equalsWithNulls(questionText, toBeCompared.questionText)
                    && (mark == toBeCompared.mark);

            return ret;
        }
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }
}
