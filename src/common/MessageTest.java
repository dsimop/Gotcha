package common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.Message.ClientOperations;
import common.model.Question;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MessageTest {

    @Test
    public void testToJson1() {
        Message message = new Message(ClientOperations.LOGIN_SUCCEED_STUDENT);
        String messageJsonString = message.toJson();
        String expected = "{\"userID\":null,\"operation\":\"C_LOGIN_SUCCEED_STUDENT\",\"data\":null}";
        assertEquals(expected, messageJsonString);
    }

    @Test
    public void testToJson2() {
        Question question = new Question();
        question.setQid(101);
        question.setMark(900);
        question.setQuestionText("Hello, world!");
        Message<Question> message = new Message<>(Message.ServerOperations.CREATE_QUESTION, question, null);
        String messageJsonString = message.toJson();
        String expected = "{\"userID\":null,\"operation\":\"S_CREATE_QUESTION\",\"data\":{\"qid\":101,"
                + "\"questionText\":\"Hello, world!\",\"mark\":900,\"answers\":[]}}";

        System.out.println("actual: " + messageJsonString);
        System.out.println("expected: " + expected);

        assertEquals(expected, messageJsonString);
    }

    @Test
    public void testFromJson1() {
        String jsonString = "{\"aid\":0,\"answerText\":\"Answer + 0\"}";
        Message message = Message.fromJson(jsonString);

        assertEquals(null, message.getOperation());
    }

    @Test
    public void testFromJson2() {
        Gson gson = Message.createGson();
        String jsonString = "{\"userID\":null,\"operation\":\"S_CREATE_QUESTION\",\"data\":{\"qid\":101,"
                + "\"questionText\":\"Hello, world!\",\"mark\":900}}";
        Question question = new Question();
        question.setQid(101);
        question.setMark(900);
        question.setQuestionText("Hello, world!");
        Message<Question> expected = new Message<>(Message.ServerOperations.CREATE_QUESTION, question, null);
        Message<Question> actual = gson.fromJson(jsonString, new TypeToken<Message<Question>>() {
        }.getType());

        Object data = actual.getData();

        String actualJsonDataStr = gson.toJson(data);
        Question actualQuestion = gson.fromJson(actualJsonDataStr, Question.class);
        System.out.println(actual.getOperation());
        System.out.println(Message.ServerOperations.CREATE_QUESTION);
        if (actual.getOperation().equals(Message.ServerOperations.CREATE_QUESTION)) {
            actual = new Message<Question>(Message.ServerOperations.CREATE_QUESTION, actualQuestion, null);
            if (expected.equals(actual)) {
                return;
            }
        }

        fail("Assertion failed: the two questions are not equal: \n Expected: " + expected + "\nActual: " + actual);

        System.out.println("actual: " + actual);
        System.out.println("expected: " + expected);
    }
    //
    // @Test
    // public void testFromJson3() {
    // }
}