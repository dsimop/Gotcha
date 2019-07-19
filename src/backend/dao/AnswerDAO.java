package backend.dao;

import common.Utils;
import common.model.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnswerDAO extends AbstractDAO {
    /**
     * Construct the DAO instance with a database connection
     *
     * @param connection is a database connection
     */
    public AnswerDAO(Connection connection) {
        super(connection);
    }

    /**
     * Delete an answer in the database
     * Precondition: the answer object must have an id
     *
     * @param answer is the data of answer to be deleted
     * @return a boolean value indicating whether the updating operation is successful
     */
    public boolean deleteAnswer(Answer answer) {
        try (
                PreparedStatement answerDeletionStatement = getConnection().prepareStatement(
                        "DELETE FROM answers WHERE aid = ?;")
        ) {
            answerDeletionStatement.setInt(1, answer.getAid());

            Utils.logSQL(answerDeletionStatement);
            return answerDeletionStatement.executeUpdate() >= 0;
        } catch (SQLException e) {
            Utils.logException("cannot delete an answer:", e);
        }
        return false;
    }

    /**
     * Update an answer in the database
     * Precondition: the answer object must have an id
     *
     * @param answer is the data of answer to be updated
     * @return a boolean value indicating whether the updating operation is successful
     */
    public boolean updateAnswer(Answer answer) {
        try (
                PreparedStatement answerUpdatingStatement = getConnection().prepareStatement(
                        "UPDATE answers SET answer_text = ?, is_correct = ? WHERE aid = ?;")
        ) {
            answerUpdatingStatement.setString(1, answer.getAnswerText());
            answerUpdatingStatement.setBoolean(2, answer.getCorrect());
            answerUpdatingStatement.setInt(3, answer.getAid());

            Utils.logSQL(answerUpdatingStatement);
            return answerUpdatingStatement.executeUpdate() >= 0;
        } catch (SQLException e) {
            Utils.logException("cannot update an answer:", e);
        }
        return false;
    }


    /**
     * Create an new answer in the database
     * Preconditions:
     * 1. The Answer object must have a qid for related questions
     * 2. The Answer object must have the answerText
     * 3. The Answer object must have the boolean value for isCorrect
     *
     * @param answer is the Answer object
     * @return a boolean value indicating whether this operation is successfully performed
     */
    public boolean createNewAnswer(Answer answer) {
        try (
                PreparedStatement answerDeletionStatement = getConnection().prepareStatement(
                        "INSERT INTO answers (qid, answer_text, is_correct) VALUES (?, ?, ?);")
        ) {
            answerDeletionStatement.setInt(1, answer.getQid());
            answerDeletionStatement.setString(2, answer.getAnswerText());
            answerDeletionStatement.setBoolean(3, answer.getCorrect());

            Utils.logSQL(answerDeletionStatement);

            return answerDeletionStatement.executeUpdate() >= 1;
        } catch (SQLException e) {
            Utils.logException("cannot create new answer:", e);
        }
        return false;
    }
}
