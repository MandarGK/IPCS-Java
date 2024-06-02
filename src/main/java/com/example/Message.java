package com.example;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String senderId;
    private String content;

    public Message(String senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderId='" + senderId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
