package org.quizproject.quizproject.Sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.quizproject.quizproject.Models.Question;
import org.quizproject.quizproject.Models.QuestionDTO;

public class QuizServer {
    private final String roomId;
    private final int port;
    private final List<Question> questions;
    private ServerSocket serverSocket;
    private final Map<String, ClientHandler> clients;
    private volatile boolean running = true;
    private final Map<String, Integer> playerProgress;

    public QuizServer(String roomId, int port, List<Question> questions) {
        this.roomId = roomId;
        this.port = port;
        this.questions = questions;
        this.clients = new ConcurrentHashMap<>();
        this.playerProgress = new ConcurrentHashMap<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            Thread acceptThread = new Thread(this::acceptClients);
            acceptThread.setDaemon(true);
            acceptThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptClients() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this::handleMessage, this::removeClient);
                Thread clientThread = new Thread(handler);
                clientThread.setDaemon(true);
                clientThread.start();
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void handleMessage(SocketMessage message, ClientHandler handler) {
        switch (message.getType()) {
            case JOIN_ROOM:
                String clientId = message.getSenderId() != null ? 
                    message.getSenderId() : 
                    message.getPayload().toString();
                clients.put(clientId, handler);
                playerProgress.put(clientId, 0);
                broadcastPlayerList();
                break;
            case LEAVE_ROOM:
                removeClient(message.getSenderId() != null ? 
                    message.getSenderId() : 
                    message.getPayload().toString());
                broadcastPlayerList();
                break;
            case ANSWER_SUBMITTED:
                if (message.getPayload() instanceof Integer) {
                    updatePlayerProgress(
                        message.getSenderId() != null ? 
                            message.getSenderId() : 
                            String.valueOf(message.getPayload()),
                        (Integer) message.getPayload()
                    );
                }
                break;
            case PROGRESS_UPDATE:
                // Broadcast progress updates to all clients
                broadcast(message);
                break;
        }
    }

    private void updatePlayerProgress(String playerId, int questionNumber) {
        playerProgress.put(playerId, questionNumber);
        broadcast(new SocketMessage(
            SocketMessage.MessageType.PROGRESS_UPDATE,
            roomId,
            new java.util.HashMap<>(playerProgress)
        ));
    }

    private void removeClient(String clientId) {
        ClientHandler handler = clients.remove(clientId);
        playerProgress.remove(clientId);
        if (handler != null) {
            handler.close();
        }
        broadcastPlayerList();
    }

    public void startQuiz() {
        List<QuestionDTO> questionDTOs = questions.stream()
                .map(QuestionDTO::fromQuestion)
                .collect(Collectors.toList());
                
        broadcast(new SocketMessage(
            SocketMessage.MessageType.START_QUIZ,
            roomId,
            questionDTOs
        ));
    }

    private void broadcastPlayerList() {
        broadcast(new SocketMessage(
            SocketMessage.MessageType.PLAYER_LIST,
            roomId,
            clients.keySet().stream().collect(Collectors.toList())
        ));
    }

    private synchronized void broadcast(SocketMessage message) {
        clients.values().forEach(client -> client.sendMessage(message));
    }

    public void stop() {
        running = false;
        clients.values().forEach(ClientHandler::close);
        clients.clear();
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}