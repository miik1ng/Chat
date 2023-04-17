package com.mik1ng.chat.event;

public class SendChatMessageEvent {
    private int userID;
    private String content;

    public SendChatMessageEvent() {
    }

    public SendChatMessageEvent(int userID, String content) {
        this.userID = userID;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
