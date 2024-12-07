package org.quizproject.quizproject.Dao;

import org.quizproject.quizproject.Models.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private final DBconnection dbConnection = DBconnection.getInstance();
    private final QuestionDao questionDao = new QuestionDao();

    public List<Category> getAllCategories() {
        String query = "SELECT * FROM categories";
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnection.getCon();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Category category = new Category(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
                categories.add(category);
            }

            // Load questions after the ResultSet is closed
            for (Category category : categories) {
                category.setQuestions(questionDao.getQuestionsByCategory(category.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
                    Category category = new Category(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description")
                    );
                    // Fetch and set questions with their options
                    category.setQuestions(questionDao.getQuestionsByCategory(category.getId()));
                    return category;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
