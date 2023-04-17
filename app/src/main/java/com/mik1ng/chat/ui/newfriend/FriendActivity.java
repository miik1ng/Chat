package com.mik1ng.chat.ui.newfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityFriendBinding;
import com.mik1ng.chat.event.CreateNewChatEvent;
import com.mik1ng.chat.util.Constant;

import org.greenrobot.eventbus.EventBus;

public class FriendActivity extends BaseActivity<ActivityFriendBinding> {

    private int id = -1;
    private String name;
    private String avatar;
    private int state = -1;

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

        }
    };

    /**
     * 同意
     */
    View.OnClickListener agreeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    /**
     * 发消息
     */
    View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            EventBus.getDefault().post(new CreateNewChatEvent(id, name, avatar));
        }
    };
}
