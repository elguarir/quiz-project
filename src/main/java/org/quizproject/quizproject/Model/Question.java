package org.quizproject.quizproject.Model;

public class Question {
    private int id, idGenre;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdGenre() {
        return idGenre;
    }

    public void setIdGenre(int idGenre) {
        this.idGenre = idGenre;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}