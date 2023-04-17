package com.mik1ng.chat.network;

public interface WebSocketCallback {
    void onOpen();

    void onMessage(String text);

    void onClose();

    void onClosing();

    void onConnectError(Throwable throwable);
}
