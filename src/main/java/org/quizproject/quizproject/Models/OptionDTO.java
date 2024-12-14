package org.quizproject.quizproject.Models;

import java.io.Serializable;

public class OptionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String content;
    private boolean correct;
    private long questionId;

    public OptionDTO() {
    }

    public static OptionDTO fromOption(Option option) {
        OptionDTO dto = new OptionDTO();
        dto.setId(option.getId());
        dto.setContent(option.getContent());
        dto.setCorrect(option.isCorrect());
        dto.setQuestionId(option.getQuestionId());
        return dto;
    }

    public Option toOption() {
        Option option = new Option();
        option.setId(this.id);
        option.setContent(this.content);
        option.setCorrect(this.correct);
        option.setQuestionId(this.questionId);
        return option;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return "OptionDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", correct=" + correct +
                ", questionId=" + questionId +
                '}';
    }
}
