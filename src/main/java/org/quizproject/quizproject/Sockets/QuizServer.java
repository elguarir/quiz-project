package org.quizproject.quizproject.Sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class QuizServer {
    private ServerSocket serverSocket;
    private final int port;
    private final String roomId;
    private final Map<String, ClientHandler> clients;
    private boolean isRunning;
    private boolean isQuizStarted = false;
    private Map<String, Integer> playerProgress = new ConcurrentHashMap<>();

    public QuizServer(String roomId, int port) {
        this.roomId = roomId;
        this.port = port;
        this.clients = new ConcurrentHashMap<>();
    }

    public void startQuiz() {
        isQuizStarted = true;
        broadcast(new SocketMessage(SocketMessage.MessageType.START_QUIZ, roomId, null));
    }

    public void updateProgress(String userId, int questionNumber) {
        playerProgress.put(userId, questionNumber);
        broadcast(new SocketMessage(
                SocketMessage.MessageType.PROGRESS_UPDATE,
                roomId,
                playerProgress));
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        isRunning = true;
        try {
            serverSocket = new ServerSocket(port);
            new Thread(() -> {
                while (isRunning) {
                    try {
                        Socket socket = serverSocket.accept();
                        ClientHandler handler = new ClientHandler(socket, this);
                        handler.start();
                    } catch (IOException e) {
                        if (isRunning) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(SocketMessage message) {
        clients.values().forEach(client -> {
            client.sendMessage(message);
        });
    }

    public void addClient(String userId, ClientHandler handler) {
        clients.put(userId, handler);
        broadcastPlayerList();
    }

    public void removeClient(String userId) {
        clients.remove(userId);
        playerProgress.remove(userId);
        broadcastPlayerList();
    }

    private void broadcastPlayerList() {
        List<String> playerList = new ArrayList<>(clients.keySet());
        broadcast(new SocketMessage(
                SocketMessage.MessageType.PLAYER_LIST,
                roomId,
                playerList));
    }
}