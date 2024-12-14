package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.User;
import java.sql.*;

public class UserDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    public User getUserById(long userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setAvatar(rs.getString("avatar")); // Add this line to get the avatar URL
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
