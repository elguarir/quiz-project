package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.Question;
import java.util.List;

public class QuestionDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    // public List<Question> getRandomQuestionsByCategory(long categoryId, int limit) {
    //     String query = """
    //         SELECT q.*, o.id as option_id, o.content as option_content, o.is_correct 
    //         FROM questions q 
    //         LEFT JOIN options o ON q.id = o.question_id 
    //         WHERE q.category_id = ? 
    //         ORDER BY RAND() 
    //         LIMIT ?
    //     """;
    //     // Implementation
    // }

    // public void assignQuestionsToRoom(long roomId, List<Question> questions) {
    //     String query = "INSERT INTO room_questions (room_id, question_id, `order`) VALUES (?, ?, ?)";
    //     // Implementation
    // }

    // public List<Question> getRoomQuestions(long roomId) {
    //     String query = """
    //         SELECT q.*, o.* 
    //         FROM room_questions rq 
    //         JOIN questions q ON rq.question_id = q.id 
    //         JOIN options o ON q.id = o.question_id 
    //         WHERE rq.room_id = ? 
    //         ORDER BY rq.order
    //     """;
    //     // Implementation
    // }
}