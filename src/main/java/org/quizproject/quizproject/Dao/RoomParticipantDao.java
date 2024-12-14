package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.RoomParticipant;
import org.quizproject.quizproject.Models.User;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomParticipantDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    public boolean addParticipant(long roomId, long userId, String ipAdresse) {
        String query = "INSERT INTO room_participants (room_id, user_id, ip_address, joined_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = dbConnection.getCon();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, roomId);
            stmt.setLong(2, userId);
            stmt.setString(3, ipAdresse);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserInRoom(long roomId, long userId) {
        String query = "SELECT COUNT(*) FROM room_participants WHERE room_id = ? AND user_id = ?";
        try (Connection conn = dbConnection.getCon();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, roomId);
            stmt.setLong(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getParticipantCount(long roomId) {
        String query = "SELECT COUNT(*) FROM room_participants WHERE room_id = ?";
        try (Connection conn = dbConnection.getCon();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getRoomParticipants(long roomId) {
        String query = "SELECT u.* FROM users u " +
                "JOIN room_participants rp ON u.id = rp.user_id " +
                "WHERE rp.room_id = ?";
        List<User> participants = new ArrayList<>();

        try (Connection conn = dbConnection.getCon();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, roomId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                participants.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    public void removeParticipant(long roomId, long userId) {
        String query = "DELETE FROM room_participants WHERE room_id = ? AND user_id = ?";
        try (Connection conn = dbConnection.getCon();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, roomId);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}