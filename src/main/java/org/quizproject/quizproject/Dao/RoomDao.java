package org.quizproject.quizproject.Dao;

import java.sql.*;
import org.quizproject.quizproject.Models.Room;

public class RoomDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    public Room createRoom(Room room) {
        String query = "INSERT INTO rooms (code, is_private, host_ip, host_id, max_players, quiz_time, status) VALUES (?, ?, ?, ?, ?, ?, 'WAITING')";
        try (Connection con = dbConnection.getCon();
                PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, room.getCode());
            pstmt.setBoolean(2, room.isPrivate());
            pstmt.setString(3, room.getHostIp());
            pstmt.setLong(4, room.getHostId());
            pstmt.setInt(5, room.getMaxPlayers());
            pstmt.setInt(6, room.getQuizTime());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    room.setId(rs.getLong(1));
                }
            }
            return room;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateRoomStatus(long roomId, String status) {
        String query = "UPDATE rooms SET status = ? WHERE id = ?";
        try (Connection con = dbConnection.getCon();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setLong(2, roomId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Room getRoomById(long roomId) {
        String query = "SELECT * FROM rooms WHERE id = ?";
        try (Connection con = dbConnection.getCon();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setLong(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setCode(rs.getString("code"));
        room.setPrivate(rs.getBoolean("is_private"));
        room.setHostIp(rs.getString("host_ip"));
        room.setStatus(rs.getString("status"));
        room.setHostId(rs.getLong("host_id"));
        room.setMaxPlayers(rs.getInt("max_players"));
        room.setQuizTime(rs.getInt("quiz_time"));
        room.setCreatedAt(rs.getString("created_at"));
        room.setLastHeartbeat(rs.getString("last_heartbeat"));
        return room;
    }
}