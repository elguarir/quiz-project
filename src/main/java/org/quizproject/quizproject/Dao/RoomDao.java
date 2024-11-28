package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.Room;
import org.quizproject.quizproject.Models.RoomParticipant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    public Room createRoom(String code, boolean isPrivate, String hostIp, long hostId) {
        String query = "INSERT INTO rooms (code, is_private, host_ip, host_id, status) VALUES (?, ?, ?, ?, 'WAITING')";
        // Implementation
    }

    public boolean addParticipant(long roomId, long userId, String ipAddress) {
        String query = "INSERT INTO room_participants (room_id, user_id, ip_address) VALUES (?, ?, ?)";
        // Implementation
    }

    public boolean removeParticipant(long roomId, long userId) {
        String query = "DELETE FROM room_participants WHERE room_id = ? AND user_id = ?";
        // Implementation
    }

    public List<Room> getPublicRooms() {
        String query = "SELECT * FROM rooms WHERE is_private = false AND status = 'WAITING'";
        // Implementation
    }

    public Room getRoomByCode(String code) {
        String query = "SELECT * FROM rooms WHERE code = ?";
        // Implementation
    }
}