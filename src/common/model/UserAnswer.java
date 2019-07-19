package common.model;

/**
 * UserAnswer model
 *
 * @version 2018-03-07
 */
public class UserAnswer {
    private int id;
    private int uid;
    private String username;
    private int qid;
    private int aid;
    private String answer;
    private boolean isCorrect;
    private int mark;

    public UserAnswer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    /**
     * Override toString() method for debugging
     *
     * @return the string representation fo a Question
     */
    @Override
    public String toString() {
        return "[UserAnswer qid: " + qid + ", User uid: " + uid + ", Id: " + id + ", AnswerID aid" + aid + ", isCorrect: "
                + isCorrect + ", Mark: " + mark + "]";
    }
}
