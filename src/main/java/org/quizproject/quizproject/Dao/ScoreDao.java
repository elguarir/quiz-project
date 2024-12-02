package org.quizproject.quizproject.Dao;
import java.util.List;
import org.quizproject.quizproject.Models.Score;

public class ScoreDao {
    public void updateScore(long roomId, long userId, int points, int timeSpent) {
        String query = "UPDATE scores SET score = ?, time_spent = ? WHERE room_id = ? AND user_id = ?";
        // Implementation
    }
    
    public List<Score> getRoomLeaderboard(long roomId) {
        String query = "SELECT * FROM scores WHERE room_id = ? ORDER BY score DESC, time_spent ASC";
        // Implementation
    }
}