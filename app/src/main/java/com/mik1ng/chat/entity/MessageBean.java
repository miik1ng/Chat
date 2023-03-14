package com.mik1ng.chat.entity;

public class MessageBean {
    private String avatar;
    private String name;
    private String date;
    private String content;

    public MessageBean(String avatar, String name, String date, String content) {
        this.avatar = avatar;
        this.name = name;
        this.date = date;
        this.content = content;
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
}
