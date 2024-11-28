package org.quizproject.quizproject.Models;

import java.time.LocalDateTime;

public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private String avatar;
    private LocalDateTime createdAt;

    public User() {
        this.createdAt = LocalDateTime.now();
        this.generateAvatar();
    }

    private void generateAvatar() {
        if (this.name != null) {
            this.avatar = "https://api.dicebear.com/9.x/notionists/svg?backgroundColor=b6e3f4,c0aede,d1d4f9&seed="
                    + this.name;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.generateAvatar();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
