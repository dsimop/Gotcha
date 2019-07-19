package frontend.teacher.model;

import common.Message;
import common.model.AnswerDistribution;
import common.model.Question;
import frontend.teacher.TeacherClient;

import java.util.ArrayList;

/**
 * @author Daohan Chong
 * @version 2018-03-01
 */
public class QuestionPanelModel extends AbstractObservable {
    private AnswerPanelModel answerPanelModel = new AnswerPanelModel();

    private Integer selectedQuestionIndex = null;
    private ArrayList<Question> questions = new ArrayList<>();

    private AnswerDistribution answerDistribution = null;

    /**
     * Get the selected quiz index. If no quiz is selected, it returns null.
     *
     * @return the index of selected quiz or null
     */
    public Integer getSelectedQuestionIndex() {
        return selectedQuestionIndex;
    }

    /**
     * Set the selected quiz index. If no quiz is selected, the index should be null
     */
    public void setSelectedQuestionIndex(Integer selectedQuestionIndex) {
        this.selectedQuestionIndex = selectedQuestionIndex;
        Question currentQuestion = getSelectedQuestion();
        if (currentQuestion == null) {
            answerPanelModel.setQuestionIDAndAnswers(null, new ArrayList<>());
        } else {
            answerPanelModel.setQuestionIDAndAnswers(currentQuestion.getQid(), currentQuestion.getAnswers());
        }

        setChangedAndNotifyObservers();
    }


    /**
     * Get the selected quiz. If no quiz is selected, it returns null.
     *
     * @return the selected quiz or null
     */
    public Question getSelectedQuestion() {
        if (hasSelectedQuestion()) {
            return questions.get(selectedQuestionIndex);
        } else {
            return null;
        }
    }

    /**
     * Set the editor to a new quiz
     */
    public void newQuestion() {
        setSelectedQuestionIndex(null);
        // setSelectedQuestionIndex will notify observers
    }


    /**
     * Getter for questions
     *
     * @return an array list of questions
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * Setter for questions
     *
     * @param questions is a list of questions
     */
    public void setQuestions(ArrayList<Question> questions) {
        if (questions == null) {
            return;
        }
        this.questions = questions;

        if (hasSelectedQuestion()) {
            answerPanelModel.setQuestionIDAndAnswers(getSelectedQuestion().getQid(), getSelectedQuestion().getAnswers());

        }
        setChangedAndNotifyObservers();
    }

    public void deleteSelectedQuestion() {
        TeacherClient.communicationThread.sendDeletingQuestion2Server(this.getSelectedQuestion());
        selectedQuestionIndex = -1;
    }

    public void getAnswerDistribution(Question question) {
        Message<Question> questionMessage = new Message<>(Message.ServerOperations.GET_ANSWER_DISTRIBUTION, question);
        TeacherClient.communicationThread.sendMessageToServer(questionMessage);
    }

    private boolean hasSelectedQuestion() {
        return selectedQuestionIndex != null && selectedQuestionIndex > -1;
    }

    public void saveQuestion(Question question, Integer id) {
        if (id == null) {
            TeacherClient.communicationThread.sendNewQuestion2Server(question);
        } else {
            question.setQid(id);
            TeacherClient.communicationThread.sendUpdatedQuestion2Server(question);
        }
    }

    /**
     * Getter for answerPanelModel
     *
     * @return the current answerPanelModel
     */
    public AnswerPanelModel getAnswerPanelModel() {
        return answerPanelModel;
    }

    public static String[][] questions2TableData(ArrayList<Question> questions) {
        String[][] strings = new String[questions.size()][3];

        int p = 0;
        for (Question question : questions) {
            strings[p][0] = Integer.toString(question.getQid());
            strings[p][1] = question.getQuestionText();
            strings[p][2] = Integer.toString(question.getMark());
            p++;
        }

        return strings;
    }

    public void setAnswerDistribution(AnswerDistribution answerDistribution) {
        this.answerDistribution = answerDistribution;
        setChanged();
        notifyObservers(answerDistribution);
    }
}
