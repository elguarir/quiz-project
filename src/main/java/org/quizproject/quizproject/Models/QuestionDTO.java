package org.quizproject.quizproject.Models;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private long categoryId;
    private String difficulty;
    private String content;
    private List<OptionDTO> options;

    public QuestionDTO() {
    }

    public static QuestionDTO fromQuestion(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setCategoryId(question.getCategoryId());
        dto.setContent(question.getContent());
        dto.setDifficulty(question.getDifficulty());
        if (question.getOptions() != null) {
            dto.setOptions(question.getOptions().stream()
                    .map(OptionDTO::fromOption)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setId(this.id);
        question.setCategoryId(this.categoryId);
        question.setContent(this.content);
        question.setDifficulty(this.difficulty);
        if (this.options != null) {
            question.setOptions(this.options.stream()
                    .map(OptionDTO::toOption)
                    .collect(Collectors.toList()));
        }
        return question;
    }

    // Getters and Setters
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

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", difficulty='" + difficulty + '\'' +
                ", content='" + content + '\'' +
                ", options=" + options +
                '}';
    }
}
