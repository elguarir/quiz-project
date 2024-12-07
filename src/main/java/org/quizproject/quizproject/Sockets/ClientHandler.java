package org.quizproject.quizproject.Sockets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final QuizServer server;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String userId;
    private int currentQuestion = 0;

    public ClientHandler(Socket socket, QuizServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            SocketMessage joinMessage = (SocketMessage) in.readObject();
            if (joinMessage.getType() == SocketMessage.MessageType.JOIN_ROOM) {
                this.userId = (String) joinMessage.getPayload();
                server.addClient(userId, this);
            }

            while (true) {
                SocketMessage message = (SocketMessage) in.readObject();
                handleMessage(message);
            }
        } catch (Exception e) {
            handleDisconnect();
        }
    }

    private void handleMessage(SocketMessage message) {
        try {
            switch (message.getType()) {
                case ANSWER_SUBMITTED:
                    currentQuestion++;
                    server.updateProgress(userId, currentQuestion);
                    break;
                case QUIZ_COMPLETED:
                    server.updateProgress(userId, -1); // -1 means quiz finished
                    server.broadcast(message);
                    break;
                case LEAVE_ROOM:
                    handleDisconnect();
                    break;
                default:
                    server.broadcast(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(SocketMessage message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDisconnect() {
        server.removeClient(userId);
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}