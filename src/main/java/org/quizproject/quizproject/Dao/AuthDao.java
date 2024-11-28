package org.quizproject.quizproject.Dao;

import java.sql.*;
import java.time.LocalDateTime;

import org.quizproject.quizproject.Models.User;

public class AuthDao implements AuthDaoInterface {
    private final DBconnection dbConnection = DBconnection.getInstance();

    @Override
    public void signUp(User user) {
        String query = "INSERT INTO users (name, email, password, avatar, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = dbConnection.getCon();
                PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getAvatar());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            pstmt.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès :)");

        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    @Override
    public boolean Login(User user) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection con = dbConnection.getCon();
                PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setAvatar(rs.getString("avatar"));
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

        return false;
    }
}
