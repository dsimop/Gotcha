package common.model;

import java.util.ArrayList;

/**
 * @author Daohan Chong
 * @version 2018-03-15
 */
public class AnswerDistribution {

    private ArrayList<Answer> answers;
    private ArrayList<Integer> counts;
    private ArrayList<UserAnswer> result;

    public AnswerDistribution(ArrayList<Answer> answers, ArrayList<Integer> counts, ArrayList<UserAnswer> result) {
        this.answers = answers;
        this.counts = counts;
        this.result = result;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public ArrayList<Integer> getCounts() {
        return counts;
    }

    public ArrayList<UserAnswer> getResult() {
        return result;
    }
}
