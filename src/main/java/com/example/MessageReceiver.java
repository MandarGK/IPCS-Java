package com.example;

public class MessageReceiver implements Runnable {
    private Player player;

    public MessageReceiver(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        player.processMessages();
    }
}
