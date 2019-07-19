package backend.dao;

import common.Utils;
import common.model.StudentAnswer;
import common.model.User;
import common.model.UserAnswer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * UserAnswerDAO is the Data Access Object for Question model in relation to the database table "user_answers".
 *
 * @author Daohan Chong
 * @version 2018-03-07
 */
public class UserAnswerDAO extends AbstractDAO {
    /**
     * Construct the DAO instance with a database connection
     *
     * @param connection is a database connection
     */
    public UserAnswerDAO(Connection connection) {
        super(connection);
    }

    /**
     * Get all UserAnswers for a related user
     *
     * @param user is the related user
     * @return an array list of UserAnswer or null if SQLException caught
     */
    public ArrayList<UserAnswer> getUserAnswersForUser(User user) {

        try (
                PreparedStatement statement = getConnection()
                        .prepareStatement("SELECT * FROM user_answers WHERE uid = ? ORDER BY qid ASC;")
        ) {
            statement.setInt(1, user.getUid());
            ResultSet resultSet = statement.executeQuery();

            Utils.logSQL(statement);

            return extractResultSetToUserAnswers(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
            return null;
        }
    }

    /**
     * Extract the result set into an ArrayList of UserAnswer model
     *
     * @param rs is the ResultSet
     * @return an ArrayList of UserAnswer model
     * @throws SQLException if SQLException caught
     */
    private ArrayList<UserAnswer> extractResultSetToUserAnswers(ResultSet rs) throws SQLException {
        ArrayList<UserAnswer> userAnswers = new ArrayList<>();

        while (rs.next()) {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setId(rs.getInt("id"));
            userAnswer.setUid(rs.getInt("uid"));
            userAnswer.setQid(rs.getInt("qid"));
            userAnswer.setAid(rs.getInt("aid"));
            userAnswer.setCorrect(rs.getBoolean("is_correct"));
            userAnswer.setMark(rs.getInt("mark"));

            userAnswers.add(userAnswer);
        }

        return userAnswers;
    }

    /**
     * Insert the student's answers for a given question into the database.
     *
     * @param userAnswer A userAnser object containing the students Answers to a given question
     * @return a boolean value indicating whether this operation is successfully executed. It will return false while
     * this insertion operation fails due to reasons such as constraint conflicts.
     */
    public boolean createNewUserAnswer(UserAnswer userAnswer) {
        try (PreparedStatement statement = getConnection().prepareStatement(
                "INSERT INTO user_answers (uid, qid, aid, is_correct, mark) VALUES (?, ?, ?, ?, ?)"
        )) {
            statement.setInt(1, userAnswer.getUid());
            statement.setInt(2, userAnswer.getQid());
            statement.setInt(3, userAnswer.getAid());
            statement.setBoolean(4, userAnswer.isCorrect());
            statement.setInt(5, userAnswer.getMark());

            Utils.logSQL(statement);

            return statement.executeUpdate() >= 1;
        } catch (SQLException e) {
            Utils.logSQLException(e);
            return false;
        }
    }

    public ArrayList<StudentAnswer> getStudentAnswerList(int uid) {
        try (PreparedStatement performanceStatement = getConnection().prepareStatement(
                "SELECT *, (SELECT answer_text AS correct_answer_text FROM answers " +
                        " WHERE answers.qid = user_answers.qid AND answers.is_correct = TRUE) FROM user_answers " +
                        " LEFT JOIN answers ON user_answers.aid = answers.aid LEFT JOIN questions " +
                        " ON user_answers.qid = questions.qid WHERE user_answers.uid = ?;")) {

            performanceStatement.setInt(1, uid);
            ResultSet resultSet = performanceStatement.executeQuery();
            Utils.logSQL(performanceStatement);
            return extractStudentsPerformance(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);

        }
        return null;
    }

    private ArrayList<StudentAnswer> extractStudentsPerformance(ResultSet rs) throws SQLException {
        ArrayList<StudentAnswer> studentAnswers = new ArrayList<>();

        while (rs.next()) {
            StudentAnswer studentAnswer = new StudentAnswer();
            UserAnswer userAnswer = new UserAnswer();

            userAnswer.setAid(rs.getInt("aid"));
            userAnswer.setCorrect(rs.getBoolean("is_correct"));
            userAnswer.setUid(rs.getInt("uid"));
            userAnswer.setQid(rs.getInt("qid"));
            userAnswer.setMark(rs.getInt("mark"));

            studentAnswer.setUserAnswer(userAnswer);

            studentAnswer.setUserAnswerText(rs.getString("answer_text"));
            studentAnswer.setCorrectAnswerText(rs.getString("correct_answer_text"));
            studentAnswer.setQuestionText(rs.getString("question_text"));

            studentAnswers.add(studentAnswer);
        }

        return studentAnswers;
    }


}



