package com.mik1ng.chat.event;

public class RefreshMessageCountEvent {
    private int count;

    public RefreshMessageCountEvent() {
    }

    public RefreshMessageCountEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
