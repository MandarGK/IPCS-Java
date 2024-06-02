package com.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player {
    private String id;
    private int messageCounter;
    private BlockingQueue<Message> messageQueue;
    private AtomicBoolean running;

    public Player(String id, AtomicBoolean running) {
        this.id = id;
        this.messageCounter = 0;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.running = running;
    }

    public String getId() {
        return id;
    }

    public int getMessageCounter() {
        return messageCounter++;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    public void receiveMessage(Message message) {
        messageQueue.offer(message);
    }

    public void sendMessage(Player receiver, String content) {
        Message message = new Message(this.id, content);
        System.out.println(this.id + " sends: " + content);
        receiver.receiveMessage(message);
        messageCounter++;
    }

    public void processMessage() {
            try {
                Message received = messageQueue.poll();
                if (received != null) {
                    String newContent = received.getContent() + " " + messageCounter;
                    System.out.println(this.id + " received: " + received.getContent());
                }
            } catch (Exception e) {
                System.out.println("Exception -->" + e);
            }


    }

    public void processMessages() {
        while (running.get() || !messageQueue.isEmpty()) {
            try {
                Message received = messageQueue.poll();
                if (received != null) {
                    String newContent = received.getContent() + " Counter send by Player 2 : " + messageCounter++;
                    System.out.println(this.id + " received: " + received.getContent());
                    System.out.println(this.id + " responds with: " + newContent);
                    Message response = new Message(this.id, newContent);
                    PlayerMain.sendMessage(this, response);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(this.id + " has stopped processing messages.");
    }
}
