package com.mik1ng.chat.ui.welcome;

import android.content.Intent;
import android.text.TextUtils;

import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityWelcomeBinding;
import com.mik1ng.chat.ui.login.LoginActivity;
import com.mik1ng.chat.ui.main.MainActivity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.SharedPreferenceUtils;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {
    @Override
    public ActivityWelcomeBinding getViewBind() {
        return ActivityWelcomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        if (TextUtils.isEmpty(SharedPreferenceUtils.getString(this, Constant.SP_TOKEN))
                || SharedPreferenceUtils.getInt(this, Constant.SP_USERID) == -1) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
            Constant.USER_ID = SharedPreferenceUtils.getInt(this, Constant.SP_USERID);
            Constant.MY_AVATAR = SharedPreferenceUtils.getString(this, Constant.SP_AVATAR);
            Constant.USER_NAME = SharedPreferenceUtils.getString(this, Constant.SP_USERNAME);
        }
        finish();
    }

    @Override
    public void initData() {

    }
}
