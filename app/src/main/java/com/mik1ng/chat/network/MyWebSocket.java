package com.mik1ng.chat.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocket extends WebSocketListener {

    private static final String TAG = "WebSocket";

    private String wsUrl;
    private WebSocket webSocket;
    private ConnectStatus status;

    private OkHttpClient client = new OkHttpClient.Builder().build();

    private MyWebSocket(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    private static MyWebSocket instance;

    public static MyWebSocket getInstance(String wsUrl) {
        if (instance == null) {
            synchronized (MyWebSocket.class) {
                instance = new MyWebSocket(wsUrl);
            }
        }

        return instance;
    }

    public ConnectStatus getStatus() {
        return status;
    }

    public void connect() {
        //构造request对象
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        webSocket = client.newWebSocket(request, this);
        status = ConnectStatus.Connecting;
    }

    public void reConnect() {
        if (webSocket != null) {
            webSocket = client.newWebSocket(webSocket.request(), this);
        }
    }

    public void send(String s) {
        if (webSocket != null) {
            webSocket.send(s);
        }
    }

    public void cancel() {
        if (webSocket != null) {
            webSocket.cancel();
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, null);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        this.status = ConnectStatus.Open;
        if (callback != null) {
            callback.onOpen();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        if (callback != null) {
            callback.onMessage(text);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        this.status = ConnectStatus.Closing;
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        this.status = ConnectStatus.Closed;
        if (callback != null) {
            callback.onClose();
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        t.printStackTrace();
        this.status = ConnectStatus.Canceled;
        if (callback != null) {
            callback.onConnectError(t);
        }
    }

    public WebSocketCallback callback;

    public void setWebSocketCallback(WebSocketCallback callback) {
        this.callback = callback;
    }

    public void removeSocketIOCallback() {
        callback = null;
    }
}
