package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static common.Utils.equalsWithNulls;

/**
 * Message is the application-level communication protocol between the server and clients. This class has 3 field
 * variables: 1. userID is for identifying the user. 2. operation is the identifier for the operation to be processed by
 * the action dispatchers. 3. data is the context value such as a Question or Answer instance for this operation.
 *
 * @author Daohan Chong, Yang He, Dimitrios Simopoulos, Mel Trout
 * @version 2018-02-27
 */
public class Message<DataType> {

    // Enums for operations
    // Operations for server
    public final static class ServerOperations {
        public final static String CREATE_QUESTION = "S_CREATE_QUESTION";
        public final static String UPDATE_QUESTION = "S_UPDATE_QUESTION";
        public final static String DELETE_QUESTION = "S_DELETE_QUESTION";
        public final static String GET_ANSWER_DISTRIBUTION = "S_GET_ANSWER_DISTRIBUTION";
        public final static String CREATE_ANSWER = "S_CREATE_ANSWER";
        public final static String GET_QUESTIONS = "S_GET_QUESTIONS";
        public final static String CLOSE_CONNECTION = "S_CLOSE_CONNECTION";
        public static final String DELETE_ANSWER = "S_DELETE_ANSWER";
        public static final String UPDATE_ANSWER = "S_UPDATE_ANSWER";
        public static final String GET_ALL_USERS = "S_GET_ALL_USERS";
        public static final String ADD_NEW_USER = "S_ADD_NEW_USERS";
        public static final String ANSWER_QUESTION="S_ANSWER_QUESTION";
        public static final String GET_ALL_QUESTIONS_WITH_4_ANSWERS = "S_GET_ALL_QUESTIONS_WITH_4_ANSWERS";
        public static final String GET_FIRST_QUESTION_FOR_USER = "S_GET_FIRST_QUESTION_FOR_USER";
        public static final String GET_TOP_STUDENTS = "S_GET_TOP_STUDENTS";
        public static final String GET_STUDENTS_PERFORMANCE = "S_GET_STUDENTS_PERFORMANCE";
        // [Demo ] Step 1: Add the messages needed for communication

    }

    // Operations for client
    public final static class ClientOperations {
        public final static String LOGIN_SUCCEED_STUDENT = "C_LOGIN_SUCCEED_STUDENT";
        public final static String LOGIN_SUCCEED_TEACHER = "C_LOGIN_SUCCEED_TEACHER";
        public final static String LOGIN_FAILED = "C_LOGIN_FAILED";
        public final static String REG_SUCCEED = "C_REG_SUCCEED";
        public final static String REG_FAIL = "C_REG_FAIL";
        public final static String UPDATE_QUESTIONS = "C_UPDATE_QUESTIONS";
        public final static String QUESTIONS_RECEIVED = "C_QUESTIONS_RECEIVED";
        public final static String UPDATE_ANSWER_DISTRIBUTION = "C_UPDATE_ANSWER_DISTRIBUTION";
        public static final String UPDATE_FIRST_QUESTION_FOR_USER = "C_GET_FIRST_QUESTION_FOR_USER";
        // [Demo ] Step 1:Add the messages needed for communication
        public final static String TOP_STUDENTS_RECEIVED = "C_TOP_STUDENTS_RECEIVED";
        public final static String STUDENTS_PERFORMANCE_RECIEVED = "C_STUDENTS_PERFORMANCE_RECIEVED";


    }

    private Integer userID;
    private String operation;
    private DataType data;

    /**
     * Construct a Message instance
     *
     * @param operation is the operation identifier
     * @param data      is the data to be processed, which could also be null.
     * @param userID    is the userID for this operation, which could be null. There is also a overloaded constructor
     *                  for that case.
     */
    public Message(String operation, DataType data, Integer userID) {
        this.operation = operation;
        this.data = data;
        this.userID = userID;
    }

    /**
     * Construct a Message instance without userID
     *
     * @param operation is the operation identifier
     * @param data      is the data to be processed, which could also be null.
     */
    public Message(String operation, DataType data) {
        this(operation, data, null);
    }

    /**
     * Construct a Message instance without userID and data
     *
     * @param operation is the operation identifier
     */
    public Message(String operation) {
        this(operation, null, null);
    }

    /**
     * This is a getter of the operation
     *
     * @return the current operation
     */
    public String getOperation() {
        return operation;
    }

    public DataType getData() {
        return data;
    }

    /**
     * This is a getter of the user ID
     *
     * @return the userID
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * This is a from Gson toJson converter
     *
     * @return a toJson String
     */
    public String toJson() {
        Gson gson = createGson();
        return gson.toJson(this, this.getClass());
    }

    /**
     * Create a Message object from JSON string
     *
     * @param str is the JSON string
     * @return an
     * @throws JsonSyntaxException
     */
    public static Message fromJson(String str) throws JsonSyntaxException {
        return createGson().fromJson(str, Message.class);
    }

    /**
     * This method creates a new Gson Object, using the imported gson jar
     *
     * @return a Gson Object
     */
    public static Gson createGson() {
        return new GsonBuilder().serializeNulls().create();
    }

    @Override
    public boolean equals(Object obj) {
        if( obj == null ) {
            return false;
        } else if( ! obj.getClass().equals(this.getClass()) ) {
            return false;
        } else {
            Message toBeCompared = (Message) obj;

            return equalsWithNulls(this.operation, toBeCompared.operation)
                    && equalsWithNulls(this.data, toBeCompared.data)
                    && equalsWithNulls(this.userID, toBeCompared.userID);
        }
    }

    /**
     * Convert the current message object into JSON string
     *
     * @return the JSON string
     */
    public String toJsonString() {
        return createGson().toJson(this);
    }

    /**
     * Common logic to write the string into the output stream
     *
     * @param outputStream is the output stream to write the json string into
     * @throws IOException
     */
    public void writeToOutputStream(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeUTF(toJsonString());
        outputStream.flush();
    }

}
