package com.mik1ng.chat.ui.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.mik1ng.chat.R;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityRegisterBinding;
import com.mik1ng.chat.entity.RegisterEntity;
import com.mik1ng.chat.network.Api;
import com.mik1ng.chat.util.EditableUtils;
import com.mik1ng.chat.util.ToastUtils;
import com.mik1ng.network.observer.BaseObserver;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    @Override
    public ActivityRegisterBinding getViewBind() {
        return ActivityRegisterBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                EditableUtils.limitEdittext(editable, getString(R.string.login_phone_input_limit));
            }
        });
        viewBind.tvRegister.setOnClickListener(registerListener);
    }

    @Override
    public void initData() {

    }

    /**
     * 注册按钮
     */
    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phone = viewBind.etUsername.getText().toString();
            String password = viewBind.etPassword.getText().toString();
            String pwAgain = viewBind.etPasswordAgain.getText().toString();

            if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                ToastUtils.showToast(RegisterActivity.this,R.string.toast_11_phone);
                return;
            }

            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(pwAgain)) {
                ToastUtils.showToast(RegisterActivity.this, R.string.toast_password);
                return;
            }

            if (!password.equals(pwAgain)) {
                ToastUtils.showToast(RegisterActivity.this, R.string.toast_password_no_same);
                return;
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("username", phone);
            map.put("password", password);
            register(map);
        }
    };

    private void register(HashMap<String, String> map) {
        Api.register(map).subscribe(new BaseObserver<RegisterEntity>() {
            @Override
            public void onSuccess(RegisterEntity registerEntity) {
                if (registerEntity == null) return;
                if (registerEntity.getCode() == 200) {
                    finish();
                }
                ToastUtils.showToast(RegisterActivity.this, registerEntity.getMessage());
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
