package org.quizproject.quizproject.Models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuizProgress {
    private final Map<String, Integer> playerQuestionProgress;
    private final Map<String, Integer> playerScores;
    private final int totalQuestions;

    public QuizProgress(int totalQuestions) {
        this.playerQuestionProgress = new ConcurrentHashMap<>();
        this.playerScores = new ConcurrentHashMap<>();
        this.totalQuestions = totalQuestions;
    }

    public void updateProgress(String userId, int questionNumber) {
        playerQuestionProgress.put(userId, questionNumber);
    }

    public void updateScore(String userId, int score) {
        playerScores.put(userId, score);
    }

    public int getProgress(String userId) {
        return playerQuestionProgress.getOrDefault(userId, 0);
    }

    public int getScore(String userId) {
        return playerScores.getOrDefault(userId, 0);
    }

    public Map<String, Integer> getAllProgress() {
        return new ConcurrentHashMap<>(playerQuestionProgress);
    }

    public boolean hasCompleted(String userId) {
        return getProgress(userId) == totalQuestions;
    }

    public void removePlayer(String userId) {
        playerQuestionProgress.remove(userId);
        playerScores.remove(userId);
    }
}
