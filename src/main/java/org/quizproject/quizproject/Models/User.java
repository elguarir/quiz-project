package org.quizproject.quizproject.Models;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
            this.avatar = "https://api.dicebear.com/9.x/notionists/png?backgroundColor=b6e3f4,c0aede,d1d4f9&seed="
                    + this.name;
        }
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.generateAvatar();
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static String getUserIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
