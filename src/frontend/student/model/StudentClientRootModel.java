package frontend.student.model;

import common.Utils;
import frontend.common.ui.LoginModel;
import frontend.common.ui.RegistrationModel;
import frontend.student.view.variantViews.StudentPerformanceModel;
import frontend.student.view.variantViews.TopStudentListModel;

import java.util.Observable;

public class StudentClientRootModel extends Observable {

    private static StudentClientRootModel singletonInstance;
    private LoginModel loginModel = new LoginModel();
    private RegistrationModel registrationModel = new RegistrationModel();
    private QuestionPanelModel questionPanelModel = new QuestionPanelModel();
    private LeaderBoardModel leaderBoardModel = new LeaderBoardModel();
    private TopStudentListModel topStudentListModel = new TopStudentListModel();
    private StudentHomeModel studentHomeModel = new StudentHomeModel();
    private StudentPerformanceModel studentPerformanceModel = new StudentPerformanceModel();


    public TopStudentListModel getTopStudentListModel() {
        return topStudentListModel;
    }

    public LeaderBoardModel getLeaderBoardModel() {

        return leaderBoardModel;
    }

    public StudentClientRootModel() {
        singletonInstance = this;
        Utils.logInfo("Created a instance of StudentClientRootModel");
    }


    public static StudentClientRootModel getSingletonInstance() {
        return singletonInstance;
    }


    public RegistrationModel getRegistrationModel() {
        return registrationModel;
    }

    public QuestionPanelModel getQuestionPanelModel() {
        return questionPanelModel;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }


    public StudentHomeModel getStudentHomeModel() {
        return studentHomeModel;
    }

    public StudentPerformanceModel getStudentPerformanceModel() {
        return studentPerformanceModel;
    }

}
