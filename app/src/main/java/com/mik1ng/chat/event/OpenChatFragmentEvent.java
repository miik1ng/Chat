package com.mik1ng.chat.event;

import android.os.Bundle;

public class OpenChatFragmentEvent {
    private Bundle bundle;

    public OpenChatFragmentEvent() {
    }

    public OpenChatFragmentEvent(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
