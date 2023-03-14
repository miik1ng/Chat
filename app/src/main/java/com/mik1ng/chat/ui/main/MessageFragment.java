package com.mik1ng.chat.ui.main;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mik1ng.chat.adapter.MessageAdapter;
import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentMessageBinding;
import com.mik1ng.chat.entity.MessageBean;
import com.mik1ng.chat.entity.SearchUserEntity;
import com.mik1ng.chat.network.Api;
import com.mik1ng.network.observer.BaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageFragment extends BaseFragment<FragmentMessageBinding> {

    private List<MessageBean> list = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    public FragmentMessageBinding getViewBind() {
        return FragmentMessageBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.listChatRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageAdapter(getContext(), list);
        viewBind.listChatRecord.setAdapter(adapter);
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        list.add(new MessageBean("", "A随风奔跑的蒲公英", "上午11:58", "好的，暂时就这样"));
        list.add(new MessageBean("", "A开花的仙人掌", "上午09:23", "明天上午九点见"));
        list.add(new MessageBean("", "A东西南北", "上午07:51", "没得问题，具体的问题再详细讨论"));
        list.add(new MessageBean("", "A室友", "昨天", "今天中午吃啥饭？面条还是米饭？"));
        list.add(new MessageBean("", "B张大蓝", "昨天", "临时通知，明天上午10点的高数课改时间啊啊啊啊啊啊啊啊啊啊啊"));
        list.add(new MessageBean("", "B学姐王时宜", "昨天", "去年好像是7月2日，今年具体得等通知了"));
        if (list.size() > 0) {
            viewBind.layoutNoData.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }
}
