package com.example;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerClient {
    private static Player initiator;
    private static AtomicBoolean running;

    public static void main(String[] args) {
        running = new AtomicBoolean(true);
        initiator = new Player("Player1", running);
        int i = 1;
        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("Connected to server.");

            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                // Initial message to start the conversation
                Message initialMessage = new Message(initiator.getId(), "Message "+ i);
                System.out.println(initiator.getId() + " sends: " + initialMessage);
                out.writeObject(initialMessage);
                out.flush();  // Ensure the message is sent immediately


                for ( i=1 ; i < 10; i++) {

                    try {
                        Message response = (Message) in.readObject();
                        initiator.receiveMessage(response);
                        System.out.println(initiator.getId() + " received from Player 2 : " + response);
                        initiator.processMessage();

                        if (response != null) {
                            // Construct the next message
                            // String newContent = response.getContent() + " " + initiator.getMessageCounter();
                            Message nextMessage = new Message(initiator.getId(),  "Message " + (i + 1));
                            if (nextMessage.getContent() != null) {
                                out.writeObject(nextMessage);
                                out.flush();  // Ensure the message is sent immediately
                                System.out.println(initiator.getId() + " responded to Player 2 : " + nextMessage);


                            } else {
                                System.out.println("Failed to create a valid next message.");
                                running.set(false);
                            }


                        } else {
                            System.out.println("Received null response.");
                            running.set(false);
                        }
                    } catch (EOFException e) {
                        System.out.println("Server has closed the connection.");
                        running.set(false);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        running.set(false);
        System.out.println("Client stopped.");
    }
}
