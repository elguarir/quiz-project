package org.quizproject.quizproject.Model;

public class Option {
    private int id, idQuestion;
    private boolean isTime;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public boolean isTime() {
        return isTime;
    }

    public void setTime(boolean isTime) {
        this.isTime = isTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}