package com.mik1ng.chat.ui.newfriend;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivitySearchFriendBinding;
import com.mik1ng.chat.entity.SearchUserEntity;
import com.mik1ng.chat.network.Api;
import com.mik1ng.network.observer.BaseObserver;

public class SearchFriendActivity extends BaseActivity<ActivitySearchFriendBinding> {
    @Override
    public ActivitySearchFriendBinding getViewBind() {
        return ActivitySearchFriendBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.etKey.setOnEditorActionListener(onEditorActionListener);
        viewBind.layoutBack.setOnClickListener(backListener);
    }

    @Override
    public void initData() {

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
            searchUser(viewBind.etKey.getText().toString());
            return false;
        }
    };

    private void searchUser(String phone) {
        Api.searchUser(phone).subscribe(new BaseObserver<SearchUserEntity>() {
            @Override
            public void onSuccess(SearchUserEntity searchUserEntity) {
                if (searchUserEntity == null) return;
                if (searchUserEntity.getCode() == 200) {
                    viewBind.layoutFriend.setVisibility(View.VISIBLE);
                    Glide.with(SearchFriendActivity.this)
                            .load(searchUserEntity.getData().getAvatar())
                            .into(viewBind.ivAvatar);
                    viewBind.tvName.setText(searchUserEntity.getData().getNickname());
                    viewBind.tvPhone.setText(searchUserEntity.getData().getUsername());
                } else {
                    viewBind.layoutFriend.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
