package com.mik1ng.chat.event;

public class RefreshFriendsCountEvent {
    private int count;

    public RefreshFriendsCountEvent() {
    }

    public RefreshFriendsCountEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
