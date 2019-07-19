package frontend.teacher.model;


/**
 * The view model for RootPanel
 *
 * @author Daohan Chong
 * @version 2018-03-02
 */
public class RootPanelModel extends AbstractObservable {

    private QuestionPanelModel questionPanelModel = new QuestionPanelModel();

    public QuestionPanelModel getQuestionPanelModel() {
        return questionPanelModel;
    }
}
