package com.example;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerMain {
    private static Player initiator;
    private static Player receiver;
    private static AtomicBoolean running;

    public static void main(String[] args) {
        running = new AtomicBoolean(true);

        initiator = new Player("Player1", running);
        receiver = new Player("Player2", running);

        Thread receiverThread = new Thread(new MessageReceiver(receiver));
        receiverThread.start();

        for (int i = 0; i < 10; i++) {
            initiator.sendMessage(receiver, "Message " + (i + 1));
            try {
                // Sleep to ensure the receiver thread has time to process the message
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Indicate that communication is over
        running.set(false);

        try {
            receiverThread.join();
            System.out.println("Receiver thread has finished.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Communication is complete.");
    }

    public static void sendMessage(Player player, Message message) {
        if (player.getId().equals(initiator.getId())) {
            receiver.receiveMessage(message);
        } else {
            initiator.receiveMessage(message);
        }
    }
}
