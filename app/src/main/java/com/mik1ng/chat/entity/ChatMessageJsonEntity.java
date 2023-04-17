package com.mik1ng.chat.entity;

public class ChatMessageJsonEntity {
    private int fromUserId;
    private int toUserId;
    private int type;
    private String content;

    public ChatMessageJsonEntity() {
    }

    public ChatMessageJsonEntity(int fromUserId, int toUserId, int type, String content) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.type = type;
        this.content = content;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
