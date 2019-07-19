package common.model;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 * 
 * @author Dimitrios Simopoulos
 * @version 2018-03-13
 *
 */
public class AnswerTest {
    @Test
    public void answerTest1() {
        Answer answer = new Answer();
        answer.setAid(null);
        answer.setAnswerText("Test Answer");
        answer.setCorrect(true);
        answer.setQid(2);
        String expected = "Answer{aid=null, qid=2, answerText='Test Answer', isCorrect=true}";
        assertEquals(expected, answer.toString());
    }

    @Test
    public void answerTest2() {
        Answer answer = new Answer();
        answer.setAid(2);
        answer.setAnswerText("Test Answer");
        answer.setCorrect(false);
        answer.setQid(2);
        String expected = "Answer{aid=2, qid=2, answerText='Test Answer', isCorrect=false}";
        assertEquals(expected, answer.toString());
    }

    @Test
    public void answerTest3() {
        Answer answer = new Answer();
        answer.setAid(2);
        answer.setAnswerText("Answer");
        answer.setCorrect(false);
        answer.setQid(2);
        String expected = "Answer{aid=2, qid=2, answerText='Answer', isCorrect=false}";
        assertEquals(expected, answer.toString());
    }
}