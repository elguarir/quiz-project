package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private final DBconnection dbConnection = DBconnection.getInstance();

    public List<Category> getAllCategories() {
        String query = "SELECT * FROM categories";
        List<Category> categories = new ArrayList<>();
        
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Category category = new Category(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Category getCategoryById(long id) {
        String query = "SELECT * FROM categories WHERE id = ?";
        
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Category createCategory(String name, String description) {
        String query = "INSERT INTO categories (name, description) VALUES (?, ?)";
        
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, name);
            stmt.setString(2, description);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Category(
                        generatedKeys.getLong(1),
                        name,
                        description
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
