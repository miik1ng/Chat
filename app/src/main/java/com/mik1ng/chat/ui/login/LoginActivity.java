package com.mik1ng.chat.ui.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.mik1ng.chat.R;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityLoginBinding;
import com.mik1ng.chat.entity.LoginEntity;
import com.mik1ng.chat.network.Api;
import com.mik1ng.chat.ui.main.MainActivity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.EditableUtils;
import com.mik1ng.chat.util.SharedPreferenceUtils;
import com.mik1ng.chat.util.ToastUtils;
import com.mik1ng.network.observer.BaseObserver;

import java.util.HashMap;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    @Override
    public ActivityLoginBinding getViewBind() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (viewBind.etUsername.length() > 0) {
                    viewBind.ivUsernameClean.setVisibility(View.VISIBLE);
                } else {
                    viewBind.ivUsernameClean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                EditableUtils.limitEdittext(editable, getString(R.string.login_phone_input_limit));
            }
        });

        viewBind.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (viewBind.etPassword.length() > 0) {
                    viewBind.ivPasswordClean.setVisibility(View.VISIBLE);
                } else {
                    viewBind.ivPasswordClean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        viewBind.ivUsernameClean.setOnClickListener(usernameCleanListener);
        viewBind.ivPasswordClean.setOnClickListener(passwordCleanListener);
        viewBind.tvLogin.setOnClickListener(loginListener);
        viewBind.tvRegister.setOnClickListener(registerListener);
    }

    @Override
    public void initData() {

    }

    /**
     * username清除按钮
     */
    private View.OnClickListener usernameCleanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewBind.etUsername.setText("");
        }
    };

    /**
     * password清除按钮
     */
    private View.OnClickListener passwordCleanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewBind.etPassword.setText("");
        }
    };

    /**
     * 登录按钮
     */
    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phone = viewBind.etUsername.getText().toString();
            String password = viewBind.etPassword.getText().toString();
            if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                ToastUtils.showToast(LoginActivity.this, R.string.toast_11_phone);
                return;
            }

            if (TextUtils.isEmpty(password)) {
                ToastUtils.showToast(LoginActivity.this, R.string.toast_password);
                return;
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("username", phone);
            map.put("password", password);
            login(map);
        }
    };

    /**
     * 注册按钮
     */
    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    };

    private void login(HashMap<String, String> map) {
        Api.login(map).subscribe(new BaseObserver<LoginEntity>() {
            @Override
            public void onSuccess(LoginEntity loginEntity) {
                if (loginEntity == null) return;
                if (loginEntity.getCode() == 200) {
                    // todo登录成功
                    SharedPreferenceUtils.saveString(LoginActivity.this, Constant.SP_TOKEN, loginEntity.getData().getToken());
                    SharedPreferenceUtils.saveInt(LoginActivity.this,Constant.SP_USERID,loginEntity.getData().getUser().getId());
                    SharedPreferenceUtils.saveString(LoginActivity.this, Constant.SP_AVATAR, loginEntity.getData().getUser().getAvatar());
                    SharedPreferenceUtils.saveString(LoginActivity.this, Constant.SP_USERNAME, loginEntity.getData().getUser().getNickname());
                    Constant.USER_ID = loginEntity.getData().getUser().getId();
                    Constant.MY_AVATAR = loginEntity.getData().getUser().getAvatar();
                    Constant.USER_NAME = loginEntity.getData().getUser().getNickname();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                ToastUtils.showToast(LoginActivity.this, loginEntity.getMessage());
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
