package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.RoomQuestion;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;

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

    // public List<RoomQuestion> getRoomQuestions(long roomId) {
    //     String query = "SELECT * FROM room_questions WHERE room_id = ? ORDER BY `order`";
    //     // Implementation
    // }
}