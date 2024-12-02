package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.RoomParticipant;
import java.util.List;

public class RoomParticipantDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    // public List<RoomParticipant> getRoomParticipants(long roomId) {
    //     String query = "SELECT * FROM room_participants WHERE room_id = ?";
    //     // Implementation
    // }

    // public boolean isParticipantInRoom(long roomId, long userId) {
    //     String query = "SELECT COUNT(*) FROM room_participants WHERE room_id = ? AND user_id = ?";
    //     // Implementation
    // }

    // public void removeParticipant(long roomId, long userId) {
    //     String query = "DELETE FROM room_participants WHERE room_id = ? AND user_id = ?";
    //     // Implementation
    // }
}