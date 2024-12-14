package org.quizproject.quizproject.Dao;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.quizproject.quizproject.Models.Room;

public class RoomDao {
    private final DBconnection dbConnection = DBconnection.getInstance();
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    private String generateUniqueRoomCode() {
        String code;
        do {
            StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                codeBuilder.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
            }
            code = codeBuilder.toString();
        } while (getRoomByCode(code) != null);
        return code;
    }

    public Room createRoom(Room room) {
        if (room.isPrivate()) {
            room.setCode(generateUniqueRoomCode());
        }
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

    public Room getRoomByCode(String code) {
        String query = "SELECT r.*, u.name as host_name FROM rooms r " +
                      "JOIN users u ON r.host_id = u.id " +
                      "WHERE r.code = ?";
        
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setCode(rs.getString("code"));
                room.setHostId(rs.getInt("host_id"));
                room.setHostName(rs.getString("host_name"));
                room.setMaxPlayers(rs.getInt("max_players"));
                room.setQuizTime(rs.getInt("quiz_time"));
                room.setStatus(rs.getString("status"));
                return room;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Room> getPublicRooms() {
        String query = """
            SELECT r.*, u.name as host_name 
            FROM rooms r 
            LEFT JOIN users u ON r.host_id = u.id 
            WHERE r.is_private = false AND r.status = 'WAITING'
            """;
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = dbConnection.getCon();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Room room = mapResultSetToRoom(rs);
                room.setHostName(rs.getString("host_name"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rooms;
    }

    public boolean deleteRoom(long roomId) {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (Connection con = dbConnection.getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setLong(1, roomId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        
        return room;
    }
}