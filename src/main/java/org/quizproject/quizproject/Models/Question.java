package org.quizproject.quizproject.Models;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private long id;
    private long categoryId;
    private String difficulty; // EASY, MEDIUM, HARD
    private String content;
    private String createdAt;
    private List<Option> options;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    // methods
    public List<Question> getRandomQuestionsByCategory(long categoryId, int limit) {
        // TODO: query database to get random questions by category

        return null;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", difficulty='" + difficulty + '\'' +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", options=" + options +
                '}';
    }


    public boolean validateAnswer(long optionId) {
        return options.stream()
            .filter(opt -> opt.getId() == optionId)
            .findFirst()
            .map(Option::isCorrect)
            .orElse(false);
    }
    
}