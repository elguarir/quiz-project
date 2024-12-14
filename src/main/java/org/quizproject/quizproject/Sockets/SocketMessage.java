package org.quizproject.quizproject.Sockets;

import java.io.Serializable;

public class SocketMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        JOIN_ROOM,
        LEAVE_ROOM,
        START_QUIZ,
        ANSWER_SUBMITTED,
        PROGRESS_UPDATE,
        QUIZ_COMPLETED,
        PLAYER_LIST,
        PLAYER_JOINED,
        PLAYER_LEFT
    }

    private final MessageType type;
    private final String roomCode;
    private final Object payload;
    private final String senderId;

    public SocketMessage(MessageType type, String roomCode, Object payload) {
        this.type = type;
        this.roomCode = roomCode;
        this.payload = payload;
        this.senderId = null;
    }

    public SocketMessage(MessageType type, String roomCode, String senderId, Object payload) {
        this.type = type;
        this.roomCode = roomCode;
        this.senderId = senderId;
        this.payload = payload;
    }

    public MessageType getType() {
        return type;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public Object getPayload() {
        return payload;
    }

    public String getSenderId() {
        return senderId;
    }
}