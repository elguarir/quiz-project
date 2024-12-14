package org.quizproject.quizproject.Models;

import java.util.List;
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
    private int maxPlayers;
    private int quizTime;
    private String hostName;

    public Room() {
    };

    public Room(long id, String code, boolean isPrivate, String hostIp, String status, long hostId, String createdAt,
            List<RoomQuestion> questions, List<RoomParticipant> participants, int maxPlayers, int quizTime) {
        this.id = id;
        this.code = code;
        this.isPrivate = isPrivate;
        this.hostIp = hostIp;
        this.status = status;
        this.hostId = hostId;
        this.createdAt = createdAt;
        this.questions = questions;
        this.participants = participants;
        this.maxPlayers = maxPlayers;
        this.quizTime = quizTime;
    }

    public Room(String code, boolean isPrivate, String hostIp, long hostId, int maxPlayers, int quizTime) {
        this.code = code;
        this.isPrivate = isPrivate;
        this.hostIp = hostIp;
        this.status = "WAITING";
        this.hostId = hostId;
        this.maxPlayers = maxPlayers;
        this.quizTime = quizTime;
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

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

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(int quizTime) {
        this.quizTime = quizTime;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public boolean canStart() {
        return participants != null &&
                participants.size() >= 2 &&
                participants.size() <= maxPlayers;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", isPrivate=" + isPrivate +
                ", hostIp='" + hostIp + '\'' +
                ", status='" + status + '\'' +
                ", hostId=" + hostId +
                ", createdAt='" + createdAt + '\'' +
                ", questions=" + questions +
                ", participants=" + participants +
                ", maxPlayers=" + maxPlayers +
                ", quizTime=" + quizTime +
                ", hostName='" + hostName + '\'' +
                '}';
    }

}