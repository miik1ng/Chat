package com.mik1ng.chat.ui.welcome;

import android.content.Intent;

import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityWelcomeBinding;
import com.mik1ng.chat.ui.chat.ChatActivity;
import com.mik1ng.chat.util.DeviceUtils;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {
    @Override
    public ActivityWelcomeBinding getViewBind() {
        return ActivityWelcomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.tvVersion.setText(DeviceUtils.getVersionName(this));
        startActivity(new Intent(this, ChatActivity.class));
    }

    @Override
    public void initData() {

    }
}
