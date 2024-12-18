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

    // Add method to load questions only when needed
    public void loadQuestionsForCategory(Category category) {
        if (!category.hasQuestionsLoaded()) {
            category.setQuestions(questionDao.getQuestionsByCategory(category.getId()));
        }
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
                    return category;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getQuestionCount(long categoryId) {
        String query = "SELECT COUNT(*) FROM questions WHERE category_id = ?";
        try (Connection conn = dbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
