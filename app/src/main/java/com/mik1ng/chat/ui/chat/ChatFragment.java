package com.mik1ng.chat.ui.chat;

import android.os.Bundle;
import android.view.View;

import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentChatBinding;
import com.mik1ng.chat.event.CloseChatFragmentEvent;
import com.mik1ng.chat.util.Constant;

import org.greenrobot.eventbus.EventBus;

public class ChatFragment extends BaseFragment<FragmentChatBinding> {
    @Override
    public FragmentChatBinding getViewBind() {
        return FragmentChatBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.layoutBack.setOnClickListener(backListener);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString(Constant.BUNDLE_NAME);
            viewBind.tvName.setText(name);
        }
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
}
