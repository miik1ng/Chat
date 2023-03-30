package com.mik1ng.chat.ui.chat;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mik1ng.chat.adapter.ChatAdapter;
import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentChatBinding;
import com.mik1ng.chat.entity.ChatRecordEntity;
import com.mik1ng.chat.event.CloseChatFragmentEvent;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends BaseFragment<FragmentChatBinding> {

    private List<ChatRecordEntity> list = new ArrayList<>();
    private ChatAdapter adapter;
    private String avatar;

    @Override
    public FragmentChatBinding getViewBind() {
        return FragmentChatBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.layoutBack.setOnClickListener(backListener);

        viewBind.listChatRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatAdapter(getContext(), list);
        viewBind.listChatRecord.setAdapter(adapter);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString(Constant.BUNDLE_NAME);
            viewBind.tvName.setText(name);
            avatar = bundle.getString(Constant.BUNDLE_AVATAR);
        }

        getData();
    }

    /**
     * 返回
     */
    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new CloseChatFragmentEvent());
        }
    };

    int count = 1;

    private void getData() {
        for (int i = 0; i < 20; i++) {
            ChatRecordEntity entity = new ChatRecordEntity();
            switch (i % 9) {
                case 0:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_TEXT);
                    entity.setAvatar(avatar);
                    entity.setText("接收文本" + count);
                    count++;
                    break;
                case 1:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_IMAGE);
                    entity.setAvatar(avatar);
                    entity.setImage(Constant.MY_AVATAR);
                    break;
                case 2:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_TEXT);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setText("发送文本" + count);
                    count++;
                    break;
                case 3:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_VOICE);
                    entity.setAvatar(avatar);
                    entity.setSecond(5);
                    break;
                case 4:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_VOICE);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setSecond(7);
                    break;
                case 5:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_LOCATION);
                    entity.setAvatar(avatar);
                    entity.setLocationName("重庆师范大学");
                    entity.setLocationAddress("重庆市沙坪坝区重庆师范大学");
                    break;
                case 6:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_IMAGE);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setImage(Constant.MY_AVATAR);
                    break;
                case 7:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_LOCATION);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setLocationName("重庆师范大学");
                    entity.setLocationAddress("重庆市沙坪坝区重庆师范大学");
                    break;
                case 8:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_TIME);
                    entity.setTimestamp(DateUtils.getTimeStamp());
                    break;
            }
            list.add(entity);
        }
        adapter.notifyItemRangeChanged(0, list.size());
        viewBind.listChatRecord.scrollToPosition(list.size()-1);
    }
}
