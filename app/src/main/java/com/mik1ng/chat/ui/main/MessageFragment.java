package com.mik1ng.chat.ui.main;

import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentMessageBinding;
import com.mik1ng.chat.entity.SearchUserEntity;
import com.mik1ng.chat.network.Api;
import com.mik1ng.network.observer.BaseObserver;

import java.util.HashMap;

public class MessageFragment extends BaseFragment<FragmentMessageBinding> {
    @Override
    public FragmentMessageBinding getViewBind() {
        return FragmentMessageBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        //searchUser("17333781851");
        getMyInfo();
    }

    private void searchUser(String username) {
        Api.searchUser(username).subscribe(new BaseObserver<SearchUserEntity>() {
            @Override
            public void onSuccess(SearchUserEntity searchUserEntity) {

            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    private void getMyInfo() {
        Api.getMyInfo().subscribe(new BaseObserver<SearchUserEntity>() {
            @Override
            public void onSuccess(SearchUserEntity searchUserEntity) {

            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
