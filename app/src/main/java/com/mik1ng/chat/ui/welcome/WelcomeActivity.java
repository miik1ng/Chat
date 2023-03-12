package com.mik1ng.chat.ui.welcome;

import android.content.Intent;

import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityWelcomeBinding;
import com.mik1ng.chat.ui.chat.ChatActivity;
import com.mik1ng.chat.ui.login.LoginActivity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.DeviceUtils;
import com.mik1ng.chat.util.SharedPreferenceUtils;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {
    @Override
    public ActivityWelcomeBinding getViewBind() {
        return ActivityWelcomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        if (!SharedPreferenceUtils.getBoolean(this, Constant.SP_LOGIN)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void initData() {

    }
}
