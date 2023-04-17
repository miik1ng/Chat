package com.mik1ng.chat.event;

public class ReceiveChatMessageEvent {
    private String content;

    public ReceiveChatMessageEvent() {
    }

    public ReceiveChatMessageEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
