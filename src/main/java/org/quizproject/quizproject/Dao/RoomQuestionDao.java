package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.RoomQuestion;
import java.util.List;

public class RoomQuestionDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    public void saveRoomQuestions(long roomId, List<Long> questionIds) {
        String query = "INSERT INTO room_questions (room_id, question_id, `order`) VALUES (?, ?, ?)";
        // Implementation
    }

    public List<RoomQuestion> getRoomQuestions(long roomId) {
        String query = "SELECT * FROM room_questions WHERE room_id = ? ORDER BY `order`";
        // Implementation
    }
}