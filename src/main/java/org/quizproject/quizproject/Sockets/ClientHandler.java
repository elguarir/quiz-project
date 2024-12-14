package org.quizproject.quizproject.Sockets;

import java.io.*;
import java.net.Socket;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final BiConsumer<SocketMessage, ClientHandler> messageHandler;
    private final Consumer<String> onDisconnect;
    private volatile boolean running = true;

    public ClientHandler(Socket socket, BiConsumer<SocketMessage, ClientHandler> messageHandler, Consumer<String> onDisconnect) {
        this.socket = socket;
        this.messageHandler = messageHandler;
        this.onDisconnect = onDisconnect;
        try {
            this.out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.flush();
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running && !socket.isClosed()) {
            try {
                Object received = in.readObject();
                if (received instanceof SocketMessage) {
                    messageHandler.accept((SocketMessage) received, this);
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
        close();
    }

    public synchronized void sendMessage(SocketMessage message) {
        try {
            if (out != null && !socket.isClosed()) {
                out.writeObject(message);
                out.flush();
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        running = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}