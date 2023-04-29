package com.mik1ng.chat.ui.newfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mik1ng.chat.R;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityFriendBinding;
import com.mik1ng.chat.entity.AgreeEntity;
import com.mik1ng.chat.entity.ChatMessageJsonEntity;
import com.mik1ng.chat.event.RefreshFriendApplyEvent;
import com.mik1ng.chat.event.CreateNewChatEvent;
import com.mik1ng.chat.event.RefreshFriendListEvent;
import com.mik1ng.chat.network.Api;
import com.mik1ng.chat.network.ConnectStatus;
import com.mik1ng.chat.network.MyWebSocket;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.ToastUtils;
import com.mik1ng.network.observer.BaseObserver;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class FriendActivity extends BaseActivity<ActivityFriendBinding> {

    private int id = -1;
    private String name;
    private String avatar;
    private int state = -1;
    private MyWebSocket webSocket;

    @Override
    public ActivityFriendBinding getViewBind() {
        return ActivityFriendBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.tvAddFriend.setOnClickListener(addListener);
        viewBind.tvAgree.setOnClickListener(agreeListener);
        viewBind.tvSendMessage.setOnClickListener(sendListener);
        viewBind.layoutBack.setOnClickListener(backListener);

        webSocket = MyWebSocket.getInstance(this, "ws://" + Constant.RELEASE_IP + "/fit/websocket/" + Constant.USER_ID);
        if (webSocket.getStatus() != ConnectStatus.Open) {
            webSocket.connect();
        }
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            id = bundle.getInt(Constant.BUNDLE_ID);
            name = bundle.getString(Constant.BUNDLE_NAME);
            viewBind.tvName.setText(name);
            avatar = bundle.getString(Constant.BUNDLE_AVATAR);
            Glide.with(this)
                    .load(avatar)
                    .into(viewBind.ivAvatar);

            if (id != Constant.USER_ID || id != -1) {
                state = bundle.getInt(Constant.BUNDLE_FRIEND_STATE);

                switch (state) {
                    case Constant.FRIEND_STATE_ADD:
                        viewBind.tvSendMessage.setVisibility(View.VISIBLE);
                        break;
                    case Constant.FRIEND_STATE_NOT_ADD:
                        viewBind.tvAddFriend.setVisibility(View.VISIBLE);
                        break;
                    case Constant.FRIEND_STATE_ALREADY:
                        viewBind.tvAgree.setVisibility(View.VISIBLE);
                        break;
                    case Constant.FRIEND_STATE_UNKNOW:
                        break;
                }
            }
        }
    }

    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    /**
     * 添加好友
     */
    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ChatMessageJsonEntity chatMessageJsonEntity = new ChatMessageJsonEntity();
            chatMessageJsonEntity.setFromUserId(Constant.USER_ID);
            chatMessageJsonEntity.setToUserId(id);
            chatMessageJsonEntity.setType(Constant.MESSAGE_NEW_FRIEND);
            String json = new Gson().toJson(chatMessageJsonEntity);
            boolean b = webSocket.send(json);
            if (b) {
                finish();
                ToastUtils.showToast(FriendActivity.this, R.string.friend_toast_apply);
            }
        }
    };

    /**
     * 同意
     */
    View.OnClickListener agreeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            HashMap<String, String> map = new HashMap<>();
            map.put("fromUserId", String.valueOf(id));
            agree(map);
        }
    };

    /**
     * 发消息
     */
    View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
//            EventBus.getDefault().post(new CreateNewChatEvent(Constant.USER_ID, Constant.USER_NAME, Constant.MY_AVATAR));
            EventBus.getDefault().post(new CreateNewChatEvent(id, name, avatar));
        }
    };

    private void agree(HashMap<String, String> map) {
        Api.agree(map).subscribe(new BaseObserver<AgreeEntity>() {
            @Override
            public void onSuccess(AgreeEntity agreeEntity) {
                if (agreeEntity != null) {
                    if (agreeEntity.getCode() == 200) {
                        finish();
                        EventBus.getDefault().post(new RefreshFriendApplyEvent());
                        EventBus.getDefault().post(new RefreshFriendListEvent());
                    }
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
