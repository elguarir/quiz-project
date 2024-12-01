package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Model.Utilisateur;

import java.sql.*;


public class AuthDao implements AuthDaoInterface{
    DBconnection c = new DBconnection();

    @Override
    public void signUp(Utilisateur user) {
        String query = "INSERT INTO utilisateur (fullname, email, password) VALUES (?, ?, ?)";
        try (Connection con = c.getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, user.getFullname());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());

            pstmt.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès :)");

        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    @Override
    public boolean Login(Utilisateur user) {
        String query = "SELECT * FROM utilisateur WHERE email = ? AND password = ?";
        try (Connection con = c.getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());


            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

        return false;
    }
}
