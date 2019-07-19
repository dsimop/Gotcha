package common.model;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 * 
 * @author Dimitrios Simopoulos
 * @version 2018-03-13
 *
 */
public class QuestionTest {

    @Test
    public void questionTest1() {
        Question testq = new Question();
        testq.setMark(50);
        testq.setQid(1);
        testq.setQuestionText("Demo Question");
        String expected = "[Question qid: 1, question_text: Demo Question, maximum_mark: 50]";
        assertEquals(expected, testq.toString());
    }

    @Test
    public void questionTest2() {
        Question testq = new Question();
        testq.setMark(0);
        testq.setQid(null);
        testq.setQuestionText("Demo Question2");
        String expected = "[Question qid: null, question_text: Demo Question2, maximum_mark: 0]";
        assertEquals(expected, testq.toString());
    }

    @Test
    public void questionTest3() {
        Question testq = new Question();
        int i = 1;
        testq.setMark(40 - 30);
        testq.setQid(i++);
        testq.setQuestionText("Demo Question3");
        String expected = "[Question qid: 1, question_text: Demo Question3, maximum_mark: 10]";
        assertEquals(expected, testq.toString());
    }
}
