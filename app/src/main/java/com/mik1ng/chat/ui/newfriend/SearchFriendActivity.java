package com.mik1ng.chat.ui.newfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.mik1ng.chat.adapter.NewFriendAdapter;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivitySearchFriendBinding;
import com.mik1ng.chat.entity.NewFriendEntity;
import com.mik1ng.chat.entity.SearchUserEntity;
import com.mik1ng.chat.network.Api;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.network.observer.BaseObserver;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendActivity extends BaseActivity<ActivitySearchFriendBinding> {

    private List<NewFriendEntity> list = new ArrayList<>();
    private NewFriendAdapter adapter;

    private int userID = -1;
    private String name;
    private String avatar;

    @Override
    public ActivitySearchFriendBinding getViewBind() {
        return ActivitySearchFriendBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.etKey.setOnEditorActionListener(onEditorActionListener);
        viewBind.layoutBack.setOnClickListener(backListener);
        viewBind.layoutFriend.setOnClickListener(resultListener);

        viewBind.listNewFriend.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewFriendAdapter(this, list);
        adapter.setOnClickListener(onClickListener);
        viewBind.listNewFriend.setAdapter(adapter);
    }

    @Override
    public void initData() {
        getData();
    }

    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    /**
     * 搜索按钮
     */
    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchUser(viewBind.etKey.getText().toString());
            }
            return false;
        }
    };

    /**
     * 点击搜索结果
     */
    View.OnClickListener resultListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SearchFriendActivity.this, FriendActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.BUNDLE_ID, userID);
            bundle.putInt(Constant.BUNDLE_FRIEND_STATE, Constant.FRIEND_STATE_UNKNOW);
            bundle.putString(Constant.BUNDLE_NAME, name);
            bundle.putString(Constant.BUNDLE_AVATAR, avatar);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    /**
     * 列表项按钮
     */
    NewFriendAdapter.OnClickListener onClickListener = new NewFriendAdapter.OnClickListener() {
        @Override
        public void onBtnClick(int position) {

        }

        @Override
        public void onItemClick(int position) {
            Intent intent = new Intent(SearchFriendActivity.this, FriendActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.BUNDLE_ID, list.get(position).userID);
            bundle.putInt(Constant.BUNDLE_FRIEND_STATE, list.get(position).state);
            bundle.putString(Constant.BUNDLE_NAME, list.get(position).name);
            bundle.putString(Constant.BUNDLE_AVATAR, list.get(position).avatar);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private void getData() {
        list.add(new NewFriendEntity(1, "https://scpic.chinaz.net/files/pic/pic7/xpic944.jpg", "11111", "同意一下", Constant.FRIEND_STATE_ADD));
        list.add(new NewFriendEntity(1, "https://scpic.chinaz.net/files/pic/pic7/xpic944.jpg", "22222", "婷婷呀", Constant.FRIEND_STATE_ADD));
        list.add(new NewFriendEntity(1, "https://scpic.chinaz.net/files/pic/pic7/xpic944.jpg", "33333", "哈喽", Constant.FRIEND_STATE_ADD));
        list.add(new NewFriendEntity(1, "https://scpic.chinaz.net/files/pic/pic7/xpic944.jpg", "唉", "唉", Constant.FRIEND_STATE_ALREADY));
        adapter.notifyItemRangeChanged(0, list.size());
    }

    private void searchUser(String phone) {
        Api.searchUser(phone).subscribe(new BaseObserver<SearchUserEntity>() {
            @Override
            public void onSuccess(SearchUserEntity searchUserEntity) {
                if (searchUserEntity == null) return;
                if (searchUserEntity.getCode() == 200) {
                    viewBind.layoutFriend.setVisibility(View.VISIBLE);
                    viewBind.tvNoFriend.setVisibility(View.GONE);

                    userID = searchUserEntity.getData().getId();
                    name = searchUserEntity.getData().getNickname();
                    avatar = searchUserEntity.getData().getAvatar();

                    Glide.with(SearchFriendActivity.this)
                            .load(searchUserEntity.getData().getAvatar())
                            .into(viewBind.ivAvatar);
                    viewBind.tvName.setText(searchUserEntity.getData().getNickname());
                    viewBind.tvPhone.setText(searchUserEntity.getData().getUsername());
                } else {
                    viewBind.layoutFriend.setVisibility(View.GONE);
                    viewBind.tvNoFriend.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
