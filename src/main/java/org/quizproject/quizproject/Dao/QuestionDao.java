package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.Question;
import org.quizproject.quizproject.Models.Option;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    public List<Question> getQuestionsByCategory(long categoryId) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE category_id = ?";

        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                // First, collect all questions
                while (rs.next()) {
                    Question question = new Question();
                    question.setId(rs.getLong("id"));
                    question.setCategoryId(rs.getLong("category_id"));
                    question.setDifficulty(rs.getString("difficulty"));
                    question.setContent(rs.getString("content"));
                    question.setCreatedAt(rs.getString("created_at"));
                    questions.add(question);
                }
            }

            // Then, fetch options for each question after ResultSet is processed
            for (Question question : questions) {
                question.setOptions(getOptionsByQuestionId(question.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public List<Question> getRandomQuestionsByCategory(long categoryId, int limit) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE category_id = ? ORDER BY RAND() LIMIT ?";

        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, categoryId);
            stmt.setInt(2, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setId(rs.getLong("id"));
                    question.setCategoryId(rs.getLong("category_id"));
                    question.setDifficulty(rs.getString("difficulty"));
                    question.setContent(rs.getString("content"));
                    question.setCreatedAt(rs.getString("created_at"));
                    questions.add(question);
                }
            }

            for (Question question : questions) {
                question.setOptions(getOptionsByQuestionId(question.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public List<Option> getOptionsByQuestionId(long questionId) {
        List<Option> options = new ArrayList<>();
        String query = "SELECT * FROM options WHERE question_id = ?";

        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, questionId);
            ResultSet rs = stmt.executeQuery();

            // Process the result set for options
            while (rs.next()) {
                Option option = new Option();
                option.setId(rs.getLong("id"));
                option.setContent(rs.getString("content"));
                option.setCorrect(rs.getBoolean("is_correct"));
                option.setQuestionId(rs.getLong("question_id"));
                options.add(option);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return options;
    }

}
