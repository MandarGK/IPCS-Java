package com.example;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerServer {
    private static Player receiver;
    private static AtomicBoolean running;

    public static void main(String[] args) {
        running = new AtomicBoolean(true);
        receiver = new Player("Player2", running);

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started, waiting for connection...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                while (running.get()) {
                    try {
                        Message message = (Message) in.readObject();
                        if (message != null) {

                            receiver.receiveMessage(message);
                            receiver.processMessage();

                            String newContent = message.getContent() + " " + receiver.getMessageCounter();
                            Message response = new Message(receiver.getId(), newContent);
                            if (response.getContent() != null) {
                                System.out.println(receiver.getId() + " responds with: " + response);
                                out.writeObject(response);
                                out.flush();// Ensure the message is sent immediately
                            } else {
                                System.out.println("Failed to create a valid response message.");
                            }
                        } else {
                            System.out.println("Received null message.");
                        }
                    } catch (EOFException e) {
                        System.out.println("Client has closed the connection.");
                        running.set(false);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        running.set(false);
    }
}
