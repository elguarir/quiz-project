
package org.quizproject.quizproject.Models;

import java.util.List;

import org.quizproject.quizproject.Dao.DBconnection;
import java.util.Random;

public class Room {
    private long id;
    private String code;
    private boolean isPrivate;
    private String hostIp;
    private String status; // WAITING, PLAYING, FINISHED
    private long hostId;
    private String createdAt;
    private List<RoomQuestion> questions;
    private List<RoomParticipant> participants;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getHostIp() {
        return this.hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getHostId() {
        return this.hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String generateCode() {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            codeBuilder.append(digit);
        }

        return codeBuilder.toString();
    }

}