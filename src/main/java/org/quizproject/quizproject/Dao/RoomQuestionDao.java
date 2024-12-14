package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.Option;
import org.quizproject.quizproject.Models.Question;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomQuestionDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    public void saveRoomQuestions(long roomId, List<Long> questionIds) {
        String query = "INSERT INTO room_questions (room_id, question_id, `order`) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            for (int i = 0; i < questionIds.size(); i++) {
                stmt.setLong(1, roomId);
                stmt.setLong(2, questionIds.get(i));
                stmt.setInt(3, i + 1);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Question> getRoomQuestionsInOrder(long roomId) {
        List<Question> questions = new ArrayList<>();
        Map<Long, Question> questionMap = new HashMap<>();
        
        // First query: Get all questions for the room
        String questionQuery = "SELECT q.*, rq.`order` FROM questions q " +
                             "INNER JOIN room_questions rq ON q.id = rq.question_id " +
                             "WHERE rq.room_id = ? " +
                             "ORDER BY rq.`order` ASC";
        
        // Second query: Get all options for these questions
        String optionQuery = "SELECT o.* FROM options o " +
                           "INNER JOIN questions q ON o.question_id = q.id " +
                           "INNER JOIN room_questions rq ON q.id = rq.question_id " +
                           "WHERE rq.room_id = ? " +
                           "ORDER BY o.question_id, o.id ASC";
        
        try (Connection conn = dbConnection.getCon()) {
            // Load questions first
            try (PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
                stmt.setLong(1, roomId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Question question = new Question();
                    question.setId(rs.getLong("id"));
                    question.setContent(rs.getString("content"));
                    question.setCategoryId(rs.getLong("category_id"));
                    question.setDifficulty(rs.getString("difficulty"));
                    question.setOptions(new ArrayList<>());
                    
                    questionMap.put(question.getId(), question);
                    questions.add(question);
                    
                    System.out.println("Loaded question: " + question.getContent());
                }
            }
            
            // Then load all options in a single query
            try (PreparedStatement stmt = conn.prepareStatement(optionQuery)) {
                stmt.setLong(1, roomId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    Option option = new Option();
                    option.setId(rs.getLong("id"));
                    option.setContent(rs.getString("content"));
                    option.setCorrect(rs.getBoolean("is_correct"));
                    option.setQuestionId(rs.getLong("question_id"));
                    
                    Question question = questionMap.get(option.getQuestionId());
                    if (question != null && question.getOptions() != null) {
                        question.getOptions().add(option);
                        System.out.println("Added option: " + option.getContent() + 
                                         " to question: " + question.getContent());
                    }
                }
            }
            
            // Verify loaded data
            System.out.println("Total questions loaded: " + questions.size());
            for (Question q : questions) {
                System.out.println("Question '" + q.getContent() + "' has " + 
                                 (q.getOptions() != null ? q.getOptions().size() : 0) + " options");
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading questions: " + e.getMessage());
            e.printStackTrace();
        }
        
        return questions;
    }

    public List<Question> getRoomQuestionsInOrder(long roomId, int offset, int limit) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT q.* FROM questions q " +
                      "JOIN room_questions rq ON q.id = rq.question_id " +
                      "WHERE rq.room_id = ? " +
                      "ORDER BY rq.order LIMIT ? OFFSET ?";

        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setLong(1, roomId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setId(rs.getLong("id"));
                    question.setCategoryId(rs.getLong("category_id"));
                    question.setContent(rs.getString("content"));
                    question.setDifficulty(rs.getString("difficulty"));
                    questions.add(question);
                }
            }

            // Load options only for these questions
            for (Question question : questions) {
                question.setOptions(new QuestionDao().getOptionsByQuestionId(question.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public int getRoomQuestionCount(long roomId) {
        String query = "SELECT COUNT(*) FROM room_questions WHERE room_id = ?";
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}