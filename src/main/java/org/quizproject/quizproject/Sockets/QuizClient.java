package org.quizproject.quizproject.Sockets;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class QuizClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientId;
    private Consumer<SocketMessage> onMessageReceived;
    private volatile boolean running = true;

    public QuizClient(String clientId, Consumer<SocketMessage> onMessageReceived) {
        this.clientId = clientId;
        this.onMessageReceived = onMessageReceived;
    }

    public void connect(String host, int port, int roomId) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.flush();
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            // Start listening thread
            Thread listenThread = new Thread(this::listenForMessages);
            listenThread.setDaemon(true);
            listenThread.start();

            // Send initial join message
            sendMessage(new SocketMessage(
                SocketMessage.MessageType.JOIN_ROOM,
                String.valueOf(roomId),
                clientId
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        while (running) {
            try {
                Object received = in.readObject();
                if (received instanceof SocketMessage) {
                    onMessageReceived.accept((SocketMessage) received);
                }
            } catch (EOFException e) {
                // Connection closed normally
                break;
            } catch (IOException | ClassNotFoundException e) {
                if (running) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public synchronized void sendMessage(SocketMessage message) {
        try {
            if (out != null && socket != null && !socket.isClosed()) {
                out.writeObject(message);
                out.flush();
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        running = false;
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}