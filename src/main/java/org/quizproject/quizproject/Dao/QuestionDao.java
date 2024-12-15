package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.Question;
import org.quizproject.quizproject.Models.Option;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    // Add method to get question count
    public int getQuestionCount(long categoryId) {
        String query = "SELECT COUNT(*) FROM questions WHERE category_id = ?";
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Add paginated question loading
    public List<Question> getQuestionsByCategory(long categoryId, int offset, int limit) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE category_id = ? LIMIT ? OFFSET ?";

        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, categoryId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            
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

            // Load options only for these questions
            for (Question question : questions) {
                question.setOptions(getOptionsByQuestionId(question.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public List<Question> getRandomQuestionsWithOptions(long categoryId, int limit) {
        List<Question> questions = new ArrayList<>();
        String query = """
            SELECT q.*, o.id as option_id, o.content as option_content, o.is_correct 
            FROM (
                SELECT * FROM questions 
                WHERE category_id = ? 
                ORDER BY RAND() 
                LIMIT ?
            ) q
            JOIN options o ON q.id = o.question_id
            ORDER BY q.id, o.id
        """;

        try (Connection conn = dbConnection.getCon()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                Map<Long, Question> questionMap = new HashMap<>();
                
                stmt.setLong(1, categoryId);
                stmt.setInt(2, limit);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        long questionId = rs.getLong("id");
                        Question question = questionMap.computeIfAbsent(questionId, k -> {
                            Question q = new Question();
                            try {
                                q.setId(questionId);
                                q.setCategoryId(rs.getLong("category_id"));
                                q.setDifficulty(rs.getString("difficulty"));
                                q.setContent(rs.getString("content"));
                                q.setOptions(new ArrayList<>());
                            } catch (SQLException e) {
                                throw new RuntimeException("Error creating question object", e);
                            }
                            return q;
                        });

                        Option option = new Option();
                        option.setId(rs.getLong("option_id"));
                        option.setContent(rs.getString("option_content"));
                        option.setCorrect(rs.getBoolean("is_correct"));
                        option.setQuestionId(questionId);
                        question.getOptions().add(option);
                    }
                }
                
                questions.addAll(questionMap.values());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching questions with options", e);
        }

        return questions;
    }
}
