package org.quizproject.quizproject.Models;

import javafx.scene.paint.Color;

public class Category {
    private long id;
    private String name;
    private String description;
    private String createdAt;
    private Color backgroundColor;
    
    
    public long getId() {
        return this.id;
    }

    public Category(long id, String name, Color backgroundColor) {
        this.id = id;
        this.name = name;
        this.backgroundColor = backgroundColor;
    }

    public Category(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.backgroundColor = Color.WHITE; // Default color
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

     public Color getBackgroundColor() { return backgroundColor; }
}