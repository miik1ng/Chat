package com.mik1ng.chat.event;

public class ReceiveNewFriendMessageEvent {
    private String content;
    private int count;

    public ReceiveNewFriendMessageEvent() {
    }

    public ReceiveNewFriendMessageEvent(String content) {
        this.content = content;
    }

    public ReceiveNewFriendMessageEvent(int count) {
        this.count = count;
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
}
