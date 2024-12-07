package org.quizproject.quizproject.Sockets;

@FunctionalInterface
public interface MessageHandler {
    void handleMessage(SocketMessage message);
}