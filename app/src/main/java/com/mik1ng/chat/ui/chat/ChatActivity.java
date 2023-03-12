package com.mik1ng.chat.ui.chat;

import android.util.Log;
import android.view.View;

import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityChatBinding;
import com.mik1ng.chat.network.MyWebSocket;
import com.mik1ng.chat.network.WebSocketCallback;

public class ChatActivity extends BaseActivity<ActivityChatBinding> {

    private MyWebSocket webSocket;

    private final String TAG = "ChatActivity------";

    @Override
    public ActivityChatBinding getViewBind() {
        return ActivityChatBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        webSocket = MyWebSocket.getInstance("wss://demo.piesocket.com/v3/channel_1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self");
        webSocket.setWebSocketCallback(callback);
        webSocket.connect();

        viewBind.button.setOnClickListener(listener);
    }

    @Override
    public void initData() {

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            webSocket.send("hhhhhhhhh");
        }
    };

    private WebSocketCallback callback = new WebSocketCallback() {
        @Override
        public void onOpen() {
            Log.i(TAG, webSocket.getStatus().name());
        }

        @Override
        public void onMessage(String text) {
            viewBind.tvMessage.setText(text);
        }

        @Override
        public void onClose() {
            Log.i(TAG, webSocket.getStatus().name());
        }

        @Override
        public void onConnectError(Throwable throwable) {
            Log.i(TAG, webSocket.getStatus().name());
        }
    };
}
