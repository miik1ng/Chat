package com.mik1ng.chat.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mik1ng.chat.adapter.FriendsAdapter;
import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentFriendsBinding;
import com.mik1ng.chat.entity.FriendEntity;
import com.mik1ng.chat.entity.GetFriendListEntity;
import com.mik1ng.chat.network.Api;
import com.mik1ng.chat.ui.newfriend.FriendActivity;
import com.mik1ng.chat.ui.newfriend.SearchFriendActivity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.network.observer.BaseObserver;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends BaseFragment<FragmentFriendsBinding> {

    private List<FriendEntity> entities = new ArrayList<>();
    private FriendsAdapter adapter;

    @Override
    public FragmentFriendsBinding getViewBind() {
        return FragmentFriendsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.listFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FriendsAdapter(getContext());
        viewBind.listFriends.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);
        viewBind.layoutAdd.setOnClickListener(addListener);
    }

    @Override
    public void initData() {
        getData();
    }

    /**
     * 新朋友
     */
    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getContext(), SearchFriendActivity.class));
        }
    };

    /**
     * 列表item点击
     */
    FriendsAdapter.OnItemClickListener onItemClickListener = new FriendsAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            Intent intent = new Intent(getContext(), FriendActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.BUNDLE_ID, entities.get(position).getId());
            bundle.putInt(Constant.BUNDLE_FRIEND_STATE, Constant.FRIEND_STATE_ADD);
            bundle.putString(Constant.BUNDLE_NAME, entities.get(position).getNickName());
            bundle.putString(Constant.BUNDLE_AVATAR, entities.get(position).getAvatar());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private void getData() {
        Api.getFriends().subscribe(new BaseObserver<GetFriendListEntity>() {
            @Override
            public void onSuccess(GetFriendListEntity getFriendListEntity) {
                if (getFriendListEntity == null) return;
                if (getFriendListEntity.getCode() == 200) {
                    entities.clear();
                    for (GetFriendListEntity.DataBean dataBean : getFriendListEntity.getData()) {
                        entities.add(new FriendEntity(dataBean.getId(), dataBean.getRemark(), dataBean.getNickname(), dataBean.getAvatar()));
                    }
                    adapter.setData(entities);
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
