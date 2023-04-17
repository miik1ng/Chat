package com.mik1ng.chat.event;

public class ReceiveNewFriendMessageEvent {
    private String content;

    public ReceiveNewFriendMessageEvent() {
    }

    public ReceiveNewFriendMessageEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
