package common.model;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * 
 * @author Dimitrios Simopoulos
 * @version 2018-03-13
 *
 */
public class UserAnswerTest {

    @Test
    public void userAnswerTest1() {
        UserAnswer usera = new UserAnswer();
        usera.setAid(3);
        usera.setCorrect(true);
        usera.setId(3);
        usera.setMark(70);
        usera.setQid(4);
        usera.setUid(8);
        String expected = "[Question qid: 4, User uid: 8, Id: 3, AnswerID aid3, isCorect: true, Mark: 70]";
        assertEquals(expected, usera.toString());
    }

    @Test
    public void userAnswerTest2() {
        UserAnswer userb = new UserAnswer();
        int i = 5;
        userb.setAid(5 + i--);
        userb.setCorrect(false);
        userb.setId(5 + i++);
        userb.setMark(70);
        userb.setQid(4);
        userb.setUid(8);
        String expected = "[Question qid: 4, User uid: 8, Id: 9, AnswerID aid10, isCorect: false, Mark: 70]";
        assertEquals(expected, userb.toString());
    }

    @Test
    public void userAnswerTest3() {
        UserAnswer userc = new UserAnswer();
        int i = 5;
        userc.setAid(5 + ++i);
        userc.setCorrect(true);
        userc.setId(5 + i++);
        userc.setMark(70);
        userc.setQid(3);
        userc.setUid(9);
        System.out.println(userc.toString());
        String expected = "[Question qid: 3, User uid: 9, Id: 11, AnswerID aid11, isCorect: true, Mark: 70]";
        assertEquals(expected, userc.toString());
    }
}