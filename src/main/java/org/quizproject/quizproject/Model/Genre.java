package org.quizproject.quizproject.Model;

public class Genre {
    private int id;
    private String time, genreValue, theme;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGenreValue() {
        return genreValue;
    }

    public void setGenreValue(String genreValue) {
        this.genreValue = genreValue;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}