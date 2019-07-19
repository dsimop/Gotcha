package frontend.teacher.model;

import common.model.Answer;
import frontend.teacher.TeacherClient;

import java.util.ArrayList;

public class AnswerPanelModel extends AbstractObservable {

    private Integer questionID = null;
    private ArrayList<Answer> answers = new ArrayList<>();
    private int selectedAnswerIndex = -1;

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setQuestionIDAndAnswers(Integer questionID, ArrayList<Answer> answers) {
        this.questionID = questionID;
        this.answers = answers;
        if (answers == null) {
            this.selectedAnswerIndex = -1;
            this.answers = new ArrayList<>();
        }
        setChangedAndNotifyObservers();
    }

    public Integer getQuestionID() {
        return questionID;
    }

    public void setSelectedAnswerIndex(int selectedAnswerIndex) {
        this.selectedAnswerIndex = selectedAnswerIndex;
    }

    public int getSelectedAnswerIndex() {
        return selectedAnswerIndex;
    }

    public void deleteSelectedAnswer() {
        TeacherClient.communicationThread.sendDeletingAnswer2Server(answers.get(getSelectedAnswerIndex()));
    }

    public void createNewAnswer(Answer answer) {
        TeacherClient.communicationThread.sendNewAnswer2Server(answer);
    }

    public void updateExistingAnswer(Answer answer) {
        TeacherClient.communicationThread.sendUpdatingAnswer2Server(answer);
    }

    public boolean hasPersistentQuestion() {
        return questionID != null && questionID > 0;
    }

    public boolean hasSelectedAnswer() {
        return getSelectedAnswerIndex() >= 0;
    }
}
