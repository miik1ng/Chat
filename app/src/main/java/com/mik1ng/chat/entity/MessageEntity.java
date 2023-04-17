package com.mik1ng.chat.entity;

public class MessageEntity {
    private int userID;
    private String avatar;
    private String name;
    private String date;
    private String content;
    private int count;
    private long timestamp;

    public MessageEntity(int userID, String avatar, String name, String date, String content, int count) {
        this.userID = userID;
        this.avatar = avatar;
        this.name = name;
        this.date = date;
        this.content = content;
        this.count = count;
    }

    public MessageEntity(int userID, String avatar, String name, String date, String content, int count, long timestamp) {
        this.userID = userID;
        this.avatar = avatar;
        this.name = name;
        this.date = date;
        this.content = content;
        this.count = count;
        this.timestamp = timestamp;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
