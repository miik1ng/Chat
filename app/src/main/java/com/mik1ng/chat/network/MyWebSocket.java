package com.mik1ng.chat.network;

import android.content.Context;

import com.mik1ng.chat.R;
import com.mik1ng.chat.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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
    private Context context;

    private OkHttpClient client = new OkHttpClient.Builder().build();

    private MyWebSocket(Context context, String wsUrl) {
        this.context = context;
        this.wsUrl = wsUrl;
    }

    private static MyWebSocket instance;

    public static MyWebSocket getInstance(Context context, String wsUrl) {
        if (instance == null) {
            synchronized (MyWebSocket.class) {
                instance = new MyWebSocket(context, wsUrl);
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

    public boolean send(String s) {
        boolean b = false;
        if (webSocket != null) {
            b = webSocket.send(s);
        }
        if (!b || status != ConnectStatus.Open) {
            ToastUtils.showToast(context, R.string.server_fail);
        }
        return b;
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
        for (WebSocketCallback callback : callbacks) {
            if (callback != null) {
                callback.onOpen();
            }
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);

        for (WebSocketCallback callback : callbacks) {
            if (callback != null) {
                callback.onMessage(text);
            }
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
        for (WebSocketCallback callback : callbacks) {
            if (callback != null) {
                callback.onClosing();
            }
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        this.status = ConnectStatus.Closed;
        for (WebSocketCallback callback : callbacks) {
            if (callback != null) {
                callback.onClose();
            }
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        t.printStackTrace();
        this.status = ConnectStatus.Canceled;

        for (WebSocketCallback callback : callbacks) {
            if (callback != null) {
                callback.onConnectError(t);
            }
        }
    }

    //public WebSocketCallback callback;
    public List<WebSocketCallback> callbacks = new ArrayList<>();

//    public void setWebSocketCallback(WebSocketCallback callback) {
//        this.callback = callback;
//    }

    public void addWebSocketCallback(WebSocketCallback callback) {
        callbacks.add(callback);
    }

//    public void removeSocketIOCallback() {
//        callback = null;
//    }

    public void removeWebSocketCallback(WebSocketCallback callback) {
        if (callbacks.contains(callback)) {
            callbacks.remove(callback);
        }
    }
}
