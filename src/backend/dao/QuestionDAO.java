package backend.dao;

import common.Utils;
import common.model.Answer;
import common.model.AnswerDistribution;
import common.model.Question;
import common.model.UserAnswer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * QuestionDAO is the Data Access Object for Question model in relation to the database table "questions".
 *
 * @author Daohan Chong
 * @version 2018-02-26
 */
public class QuestionDAO extends AbstractDAO {

    /**
     * Construct the DAO instance with a database connection
     *
     * @param connection is a database connection
     */
    public QuestionDAO(Connection connection) {
        super(connection);
    }

    /**
     * Get all questions from the database
     *
     * @return a List of questions
     */
    public ArrayList<Question> getQuestions() {
        try (PreparedStatement statement
                     = getConnection().prepareStatement("SELECT * FROM questions ORDER BY qid ASC;");
             ResultSet resultSet = statement.executeQuery()
        ) {
            Utils.logSQL(statement);
            return extractQuestionsFromResultSet(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    /**
     * Get all questions from the database
     *
     * @return a List of questions
     */
    public ArrayList<Question> getAllQuestionsWithAnswers() {
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM questions " +
                        "FULL OUTER JOIN answers ON answers.qid = questions.qid ORDER BY questions.qid ASC, answers.aid ASC;"
        );
             ResultSet resultSet = statement.executeQuery()
        ) {
            Utils.logSQL(statement);
            return extractQuestionsAndAnswers(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    /**
     * Get all questions with 4 answers from the database
     *
     * @return a List of questions
     */
    public ArrayList<Question> getAllQuestionsWith4Answers() {
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM questions q " +
                        "FULL OUTER JOIN answers a ON a.qid = q.qid WHERE q.qid IN " +
                        "(SELECT qid FROM answers GROUP BY qid HAVING count(*)=4) " +
                        "ORDER BY q.qid ASC, a.aid ASC;"
        );
             ResultSet resultSet = statement.executeQuery()
        ) {
            Utils.logSQL(statement);
            return extractQuestionsAndAnswers(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    /**
     * Retrieve the first question with the following conditions (ordered by id ascending):
     * 1. Have 4 answers
     * 2. Not presented in the user_answers table for the specified user with `uid`
     *
     * @param uid is the user id
     * @return a Question object
     */
    // [Demo] Step 2.1: Add the related DAO method
    public Question getTheFirstQuestionForUserToAnswer(int uid) {
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM questions RIGHT JOIN answers ON questions.qid = answers.qid " +
                        "WHERE questions.qid IN (SELECT qid FROM answers GROUP BY answers.qid HAVING COUNT(*) = 4)" +
                        "AND questions.qid NOT IN (SELECT qid FROM user_answers WHERE user_answers.uid = ?) ORDER BY questions.qid ASC LIMIT 4;"
        )) {
            statement.setInt(1, uid);
            ResultSet resultSet = statement.executeQuery();
            Utils.logSQL(statement);
            return extractQuestionWithAnsers(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    /**
     * Get all questions with 4 answers which have not been answered by an exact frontend.student from the database
     *
     * @param uid uid of the frontend.student
     * @return a List of questions
     */
    public ArrayList<Question> getAllQuestionsWith4Answers(Integer uid) {
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM questions q " +
                        "FULL OUTER JOIN answers a ON a.qid = q.qid WHERE q.qid IN " +
                        "(SELECT qid FROM answers GROUP BY qid HAVING count(*)=4) " +
                        "AND q.qid NOT IN (SELECT qid FROM user_answers WHERE uid=?)" +
                        "ORDER BY q.qid ASC, a.aid ASC;"
        )) {
            statement.setInt(1, uid);
            ResultSet resultSet = statement.executeQuery();
            Utils.logSQL(statement);
            return extractQuestionsAndAnswers(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    public ArrayList<Question> getAllQuestionsWith4Answers2(Integer uid) {
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM questions " +
                        "RIGHT JOIN answers ON questions.qid = answers.qid WHERE questions.qid NOT IN " +
                        "(SELECT qid FROM user_answers WHERE user_answers.uid = ?);"
        )) {
            statement.setInt(1, uid);
            ResultSet resultSet = statement.executeQuery();
            Utils.logSQL(statement);
            return extractQuestionsAndAnswers(resultSet);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    public AnswerDistribution getUserAnswerDistribution(Question question) {
        try (PreparedStatement statisticsStatement = getConnection().prepareStatement(
                "SELECT *, (SELECT COUNT(*) FROM user_answers WHERE user_answers.aid = answers.aid) AS user_answer_count FROM answers WHERE answers.qid = ?;"
        );
             PreparedStatement resultStatement = getConnection().prepareStatement(
                     "SELECT ua.*,u.username,a.answer_text FROM user_answers ua JOIN users u ON ua.uid=u.uid JOIN answers a ON ua.aid=a.aid WHERE ua.qid=?;"
             )) {
            statisticsStatement.setInt(1, question.getQid());
            resultStatement.setInt(1, question.getQid());
            ResultSet statistics = statisticsStatement.executeQuery();
            ResultSet result = resultStatement.executeQuery();
            Utils.logSQL(statisticsStatement);
            Utils.logSQL(resultStatement);
            return extractAnswerDistribution(statistics, result);
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return null;
    }

    private AnswerDistribution extractAnswerDistribution(ResultSet statistics, ResultSet result) throws SQLException {
        ArrayList<Answer> answers = new ArrayList<>();
        ArrayList<Integer> counts = new ArrayList<>();
        ArrayList<UserAnswer> userAnswers = new ArrayList<>();

        while (statistics.next()) {
            Answer answer = new Answer();
            answer.setAid(statistics.getInt("aid"));
            answer.setQid(statistics.getInt("qid"));
            answer.setAnswerText(statistics.getString("answer_text"));
            answer.setCorrect(statistics.getBoolean("is_correct"));

            Integer count = statistics.getInt("user_answer_count");

            answers.add(answer);
            counts.add(count);
        }

        while (result.next()) {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setUid(result.getInt("uid"));
            userAnswer.setUsername(result.getString("username"));
            userAnswer.setAid(result.getInt("aid"));
            userAnswer.setAnswer(result.getString("answer_text"));
            userAnswer.setCorrect(result.getBoolean("is_correct"));
            userAnswers.add(userAnswer);
        }

        return new AnswerDistribution(answers, counts, userAnswers);
    }

    /**
     * Answer a question
     *
     * @param userAnswer is the qid, uid and aid for this answer. Note that we cannot trust
     *                   the is_correct and mark field since they are from the client so that
     *                   SQL queries are used for retrieve the is_correct and mark field.
     * @return a boolean value indicating whether the operation is successful. If it is false,
     * the user might have answered this question
     */
    public boolean answerQuestion(UserAnswer userAnswer) {
        try (
                PreparedStatement statement = getConnection().prepareStatement(
                        "INSERT INTO user_answers (uid, qid, aid, is_correct, mark) " +
                                "VALUES (?, ?, ?, " +
                                "(SELECT is_correct FROM answers WHERE answers.aid = ?), " +
                                "(SELECT mark FROM questions WHERE questions.qid = ?));"
                )
        ) {
            int uid = userAnswer.getUid();
            int qid = userAnswer.getQid();
            int aid = userAnswer.getAid();
            statement.setInt(1, uid);
            statement.setInt(2, qid);
            statement.setInt(3, aid);
            statement.setInt(4, aid);
            statement.setInt(5, qid);
            Utils.logSQL(statement);

            boolean ret = statement.executeUpdate() == 1;

            try (
                    PreparedStatement updateTotalMarkStmt = getConnection().prepareStatement(
                            "UPDATE users SET mark = (SELECT SUM(mark) FROM user_answers WHERE user_answers.uid = ? AND is_correct = TRUE) WHERE users.uid = ?")
            ) {
                updateTotalMarkStmt.setInt(1, uid);
                updateTotalMarkStmt.setInt(2, uid);
                Utils.logSQL(updateTotalMarkStmt);
                updateTotalMarkStmt.executeUpdate();
            }

            return ret;
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return false;
    }

    /**
     * Create a new question in the database
     *
     * @param question is the data of question to be created
     * @return a boolean value indicating whether the creation operation is successful
     */
    public boolean createNewQuestion(Question question) {
        try (
                PreparedStatement statement = getConnection().prepareStatement(
                        "INSERT INTO questions (question_text, mark) VALUES (?, ?)")
        ) {
            statement.setString(1, question.getQuestionText());
            statement.setInt(2, question.getMark());
            Utils.logSQL(statement);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return false;
    }

    /**
     * Update a question in the database
     * Precondition: the question object must have an id
     *
     * @param question is the data of question to be updated
     * @return a boolean value indicating whether the updating operation is successful
     */
    public boolean updateQuestion(Question question) {
        try (
                PreparedStatement statement = getConnection().prepareStatement(
                        "UPDATE questions SET question_text = ? , mark = ? WHERE qid = ?")
        ) {
            statement.setString(1, question.getQuestionText());
            statement.setInt(2, question.getMark());
            statement.setInt(3, question.getQid());
            Utils.logSQL(statement);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return false;
    }

    /**
     * Delete a question in the database
     * Precondition: the question object must have an id
     *
     * @param question is the data of question to be updated
     * @return a boolean value indicating whether the updating operation is successful
     */
    public boolean deleteQuestion(Question question) {
        try (
                PreparedStatement answerDeletionStatement = getConnection().prepareStatement(
                        "DELETE FROM answers WHERE qid = ?;");
                PreparedStatement questionDeletionStatement = getConnection().prepareStatement(
                        "DELETE FROM questions WHERE qid = ?;")

        ) {
            answerDeletionStatement.setInt(1, question.getQid());
            questionDeletionStatement.setInt(1, question.getQid());

            Utils.logSQL(answerDeletionStatement);
            Utils.logSQL(questionDeletionStatement);
            if (answerDeletionStatement.executeUpdate() >= 0) {
                return questionDeletionStatement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            Utils.logSQLException(e);
        }
        return false;
    }


    /**
     * The helper to extract Question information from result set
     *
     * @param resultSet is the result set to be processed
     * @return a list of questions
     * @throws SQLException if the ResultSet is not valid to read question information
     */
    private ArrayList<Question> extractQuestionsFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Question> questions = new ArrayList<>();

        while (resultSet.next()) {
            Question question = new Question();
            question.setQid(resultSet.getInt("qid"));
            question.setQuestionText(resultSet.getString("question_text"));
            question.setMark(resultSet.getInt("mark"));
            questions.add(question);
        }

        return questions;
    }

    /**
     * The helper to extract Question information from result set with answer data
     * Precondition: the resultSet should contain the answer data as well as the question data
     *
     * @param resultSet is the result set to be processed
     * @return a list of questions
     * @throws SQLException if the ResultSet is not valid to read question information
     */
    private ArrayList<Question> extractQuestionsAndAnswers(ResultSet resultSet) throws SQLException {
        SortedMap<Integer, Question> questionMap = new TreeMap<>();

        while (resultSet.next()) {
            int tempQuestionID = resultSet.getInt("qid");

            Question question = questionMap.get(tempQuestionID);

            if (question == null) {
                question = new Question();
                question.setQid(tempQuestionID);
                question.setQuestionText(resultSet.getString("question_text"));
                question.setMark(resultSet.getInt("mark"));

                questionMap.put(tempQuestionID, question);
            }

            Answer answer = new Answer();
            answer.setAid(resultSet.getInt("aid"));
            answer.setAnswerText(resultSet.getString("answer_text"));
            answer.setCorrect(resultSet.getBoolean("is_correct"));
            answer.setQid(resultSet.getInt("qid"));

            if (answer.getAid() != 0) {
                question.addAnswer(answer);
            }
        }

        return new ArrayList<>(questionMap.values());
    }

    // [Demo] Step 3: write corresponding helper method to extract the Question date from resultSet
    // If there is no data for the Question, return null
    private Question extractQuestionWithAnsers(ResultSet resultSet) throws SQLException {
        ArrayList<Question> questions = extractQuestionsAndAnswers(resultSet);
        if (questions.size() == 1) {
            return questions.get(0);
        } else {
            return null;
        }
    }


}
