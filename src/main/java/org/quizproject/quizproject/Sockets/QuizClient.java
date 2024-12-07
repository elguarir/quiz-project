package org.quizproject.quizproject.Sockets;


import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class QuizClient {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final String userId;
    private final MessageHandler messageHandler;

    public QuizClient(String userId, MessageHandler handler) {
        this.userId = userId;
        this.messageHandler = handler;
    }

    public void connect(String host, int port, int roomId) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            sendMessage(new SocketMessage(
                SocketMessage.MessageType.JOIN_ROOM,
                null,
                userId
            ));

            // Start listening for messages
            new Thread(this::listenForMessages).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            while (true) {
                SocketMessage message = (SocketMessage) in.readObject();
                messageHandler.handleMessage(message);
            }
        } catch (Exception e) {
            handleDisconnect();
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
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}