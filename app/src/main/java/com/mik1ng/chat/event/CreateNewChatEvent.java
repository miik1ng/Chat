package com.mik1ng.chat.event;

public class CreateNewChatEvent {
    private int userID;
    private String name;
    private String avatar;
    private int count;

    public CreateNewChatEvent() {
    }

    public CreateNewChatEvent(int userID, String name, String avatar) {
        this.userID = userID;
        this.name = name;
        this.avatar = avatar;
    }

    public CreateNewChatEvent(int userID, String name, String avatar, int count) {
        this.userID = userID;
        this.name = name;
        this.avatar = avatar;
        this.count = count;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
