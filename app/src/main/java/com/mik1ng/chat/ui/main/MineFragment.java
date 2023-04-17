package com.mik1ng.chat.ui.main;

import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentMineBinding;
import com.mik1ng.chat.ui.login.LoginActivity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.SharedPreferenceUtils;

public class MineFragment extends BaseFragment<FragmentMineBinding> {
    @Override
    public FragmentMineBinding getViewBind() {
        return FragmentMineBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.tvName.setText(Constant.USER_NAME);
        Glide.with(getContext())
                .load(Constant.MY_AVATAR)
                .into(viewBind.ivAvatar);

        viewBind.layoutSetting.setOnClickListener(settingListener);
        viewBind.layoutQuit.setOnClickListener(quitListener);
        viewBind.layoutLogout.setOnClickListener(logoutListener);
    }

    @Override
    public void initData() {

    }

    /**
     * 设置
     */
    View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    /**
     * 退出登录
     */
    View.OnClickListener quitListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().finish();
            SharedPreferenceUtils.clearShareprefrence(getContext());
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    };

    /**
     * 注销账号
     */
    View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}
