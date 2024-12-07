package org.quizproject.quizproject.Sockets;

import java.io.Serializable;

public class SocketMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        JOIN_ROOM,
        PLAYER_JOINED,
        PLAYER_LIST,
        START_QUIZ,
        ANSWER_SUBMITTED,
        PROGRESS_UPDATE,
        QUIZ_COMPLETED,
        LEAVE_ROOM
    }

    private MessageType type;
    private String roomId;
    private Object payload;

    public SocketMessage(MessageType type, String roomId, Object payload) {
        this.type = type;
        this.roomId = roomId;
        this.payload = payload;
    }

    public MessageType getType() {
        return type;
    }

    public String getRoomId() {
        return roomId;
    }

    public Object getPayload() {
        return payload;
    }
}