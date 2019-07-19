package frontend.teacher.model;

import common.model.Question;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Daohan Chong
 * @version 2018-03-15
 */
public class QuestionPanelModelTest {

    private QuestionPanelModel questionPanelModel = new QuestionPanelModel();
    private ArrayList<Question> questions;

    @Before
    public void setUp() throws Exception {
        questions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Question question = new Question();
            question.setQuestionText("Question: " + (i + 1));
            question.setQid(i + 1);
            question.setMark(i * 100);
            questions.add(question);
        }
    }

    private void setQuestionsToModel() {
        questionPanelModel.setQuestions(questions);
    }

    @Test
    public void testSelectedQuestionIndex() {
        setQuestionsToModel();
        questionPanelModel.setSelectedQuestionIndex(1);
        assertEquals(new Integer(1), questionPanelModel.getSelectedQuestionIndex());
    }

    @Test
    public void testGetSelectedQuestion() {
        setQuestionsToModel();
        questionPanelModel.setSelectedQuestionIndex(1);
        assertEquals(questions.get(1), questionPanelModel.getSelectedQuestion());
    }

    @Test
    public void testNewQuestion() {
        setQuestionsToModel();
        questionPanelModel.newQuestion();
        assertNull(questionPanelModel.getSelectedQuestionIndex());
        assertNull(questionPanelModel.getSelectedQuestion());
    }

    @Test
    public void testGetQuestions() {
        setQuestionsToModel();
        assertEquals(questions, questionPanelModel.getQuestions());
    }

    @Test
    public void testGetAnswerPanelModel() {
        assertNotNull(questionPanelModel.getAnswerPanelModel());
    }

    @Test
    public void testQuestions2TableData() {
        String[][] actual = QuestionPanelModel.questions2TableData(questions);
        String[][] expected = new String[][]{
                new String[]{"1", "Question: 1", "0"},
                new String[]{"2", "Question: 2", "100"},
                new String[]{"3", "Question: 3", "200"},
                new String[]{"4", "Question: 4", "300"},
                new String[]{"5", "Question: 5", "400"},
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testObserver() {
        // TODO: test observer
//        questionPanelModel.addObserver((o, args) -> {
//            assertEquals(questions, questionPanelModel.getQuestions());
//        });
//        setQuestionsToModel();
    }
}