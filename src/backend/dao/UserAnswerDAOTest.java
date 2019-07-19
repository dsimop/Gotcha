package backend.dao;

import backend.ConnectionFactory;
import common.model.StudentAnswer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UserAnswerDAOTest {

    private UserAnswerDAO userAnswerDAO;

    @Before
    public void setUp() throws Exception {
        userAnswerDAO = new UserAnswerDAO(ConnectionFactory.getConnection());
    }

    @Test
    public void getStudentAnswerList() {
        ArrayList<StudentAnswer> answerList = userAnswerDAO.getStudentAnswerList(78);
        System.out.println("answerList: " + answerList);
    }
}