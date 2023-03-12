package com.mik1ng.chat.ui.main;

import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentMineBinding;
import com.mik1ng.chat.entity.GetUsersEntity;
import com.mik1ng.chat.entity.RegisterEntity;
import com.mik1ng.chat.network.Api;
import com.mik1ng.network.observer.BaseObserver;

import java.util.HashMap;

public class MineFragment extends BaseFragment<FragmentMineBinding> {
    @Override
    public FragmentMineBinding getViewBind() {
        return FragmentMineBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        //getData();
        HashMap<String, String> map = new HashMap<>();
        map.put("username", "miguangning");
        map.put("password", "123456");
        register(map);
    }

    private void getData() {
        Api.getUsers().subscribe(new BaseObserver<GetUsersEntity>() {
            @Override
            public void onSuccess(GetUsersEntity getUsersEntity) {
                int i = 0;
            }

            @Override
            public void onFailure(Throwable e) {
                int i = 0;
            }
        });
    }

    private void register(HashMap<String,String> map) {
        Api.register(map).subscribe(new BaseObserver<RegisterEntity>() {
            @Override
            public void onSuccess(RegisterEntity registerEntity) {

            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
