package backend.dao;

import backend.ConnectionFactory;
import common.model.Answer;
import common.model.Question;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class QuestionDAOTest {

    private Connection connection;
    private QuestionDAO questionDAO;

    @Before
    public void setUp() throws Exception {
        connection = ConnectionFactory.getConnection();
        questionDAO = new QuestionDAO(connection);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testCreateNewQuestion() {
        Question question = new Question();
        question.setQuestionText("test new question");
        question.setMark(150);

        assertTrue(questionDAO.createNewQuestion(question));
    }

    @Test
    public void testGetAllQuestionsWithAnswers() {
        ArrayList<Question> questions = questionDAO.getAllQuestionsWithAnswers();
        System.out.println("questions: " + questions);
    }

    @Test
    public void testGetAllQuestionsWith4Answers() {
        ArrayList<Question> questions = questionDAO.getAllQuestionsWith4Answers(1);
        for (Question question : questions) {
            System.out.println(question);
            for (Answer answer: question.getAnswers()) {
                System.out.println("--" + answer);
            }
        }
    }

    // [Demo] Step 2.2: Add test for the related DAO method
    @Test
    public void testGetTheFirstQuestionForUserToAnswer() {
        Question question = questionDAO.getTheFirstQuestionForUserToAnswer(3);

        assertNotNull(question);
        assertEquals(4, question.getAnswers().size());
    }
}