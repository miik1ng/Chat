package com.mik1ng.chat.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mik1ng.chat.adapter.FriendsAdapter;
import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentFriendsBinding;
import com.mik1ng.chat.entity.FriendEntity;
import com.mik1ng.chat.entity.GetFriendListEntity;
import com.mik1ng.chat.event.ReceiveNewFriendMessageEvent;
import com.mik1ng.chat.event.RefreshFriendListEvent;
import com.mik1ng.chat.event.RefreshFriendsCountEvent;
import com.mik1ng.chat.interfaces.Observe;
import com.mik1ng.chat.network.Api;
import com.mik1ng.chat.observable.IntegerObservable;
import com.mik1ng.chat.ui.newfriend.FriendActivity;
import com.mik1ng.chat.ui.newfriend.SearchFriendActivity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.network.observer.BaseObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends BaseFragment<FragmentFriendsBinding> {

    private List<FriendEntity> entities = new ArrayList<>();
    private FriendsAdapter adapter;
    private IntegerObservable count = new IntegerObservable(0);

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
        viewBind.refresh.setOnRefreshListener(onRefreshListener);
    }

    @Override
    public void initData() {
        count.addObserve(countObserve);
        count.set(((MainActivity) getActivity()).getFriendCount());
        getData();
    }

    @Override
    public boolean useEvent() {
        return true;
    }

    /**
     * 监听count变量
     */
    Observe<Integer> countObserve = new Observe<Integer>() {
        @Override
        public void update(Integer integer) {
            if (integer > 0) {
                if (integer > 99) {
                    viewBind.tvCount.setText("···");
                } else {
                    viewBind.tvCount.setText(String.valueOf(integer));
                }
                viewBind.tvCount.setVisibility(View.VISIBLE);
            } else {
                viewBind.tvCount.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 新朋友
     */
    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getContext(), SearchFriendActivity.class));
            count.set(0);
            EventBus.getDefault().post(new RefreshFriendsCountEvent(0));
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

    /**
     * 刷新监听
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getData();
        }
    };

    private void getData() {
        Api.getFriends().subscribe(new BaseObserver<GetFriendListEntity>() {
            @Override
            public void onSuccess(GetFriendListEntity getFriendListEntity) {
                viewBind.refresh.setRefreshing(false);
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
                viewBind.refresh.setRefreshing(false);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCount(ReceiveNewFriendMessageEvent event) {
        count.set(event.getCount());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshFriendList(RefreshFriendListEvent event) {
        getData();
    }
}
