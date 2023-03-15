package com.mik1ng.chat.ui.main;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mik1ng.chat.adapter.MessageAdapter;
import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentMessageBinding;
import com.mik1ng.chat.entity.MessageBean;
import com.mik1ng.chat.event.RefreshMessageCountEvent;
import com.mik1ng.chat.interfaces.Observe;
import com.mik1ng.chat.interfaces.OnItemClickListener;
import com.mik1ng.chat.interfaces.OnItemLongClickListener;
import com.mik1ng.chat.observable.ArrayListObservable;
import com.mik1ng.chat.observable.IntegerObservable;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

public class MessageFragment extends BaseFragment<FragmentMessageBinding> {

    private ArrayListObservable<MessageBean> list = new ArrayListObservable<>();
    private MessageAdapter adapter;

    private IntegerObservable count = new IntegerObservable(0);

    @Override
    public FragmentMessageBinding getViewBind() {
        return FragmentMessageBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.listChatRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageAdapter(getContext(), list.get());
        adapter.setOnItemClickListener(itemClickListener);
        adapter.setOnItemLongClickListener(itemLongClickListener);
        viewBind.listChatRecord.setAdapter(adapter);
        viewBind.ivAdd.setOnClickListener(addListener);
        viewBind.layoutSearch.setOnClickListener(etListener);
    }

    @Override
    public void initData() {
        count.addObserve(countObserve);
        list.addObserve(listObserve);
        getData();
    }

    int add = 6;

    /**
     * 添加按钮
     */
    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            list.add(0, new MessageBean("" + add, "", "新添加" + add, "上午11:58", "新添加" + add, 1));
            adapter.notifyItemInserted(0);
            adapter.notifyItemRangeChanged(1, list.size());
            count.set(count.get() + 1);

            add++;
        }
    };

    /**
     * 输入框点击
     */
    View.OnClickListener etListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Random random = new Random();
            int id = random.nextInt(list.size());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(String.valueOf(id))) {
                    list.get(i).setCount(list.get(i).getCount() + 1);
                    count.set(count.get() + 1);
                    adapter.notifyItemChanged(i, Constant.ADAPTER_PAYLOADS_0);
                    list.add(0, list.remove(i));

                    adapter.notifyItemMoved(i, 0);
                    adapter.notifyItemRangeChanged(0, list.size());
                }
            }
        }
    };


    /**
     * 列表项点击
     */
    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onClick(int position) {
            if (list.get(position).getCount() != 0) {
                count.set(count.get() - list.get(position).getCount());

                list.get(position).setCount(0);
                adapter.notifyItemChanged(position, Constant.ADAPTER_PAYLOADS_0);
            }
            //todo 跳转到聊天页面
            ToastUtils.showToast(getContext(), "" + list.get(position).getName());
        }
    };

    /**
     * 列表项长按
     */
    OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onLongClick(int position) {
            return true;
        }
    };

    /**
     * 监听count变量
     */
    Observe<Integer> countObserve = new Observe<Integer>() {
        @Override
        public void update(Integer integer) {
            EventBus.getDefault().post(new RefreshMessageCountEvent(integer));
        }
    };

    /**
     * 监听list变量
     */
    Observe<ArrayList<MessageBean>> listObserve = new Observe<ArrayList<MessageBean>>() {
        @Override
        public void update(ArrayList<MessageBean> messageBeans) {
            if (messageBeans.size() > 0) {
                viewBind.layoutNoData.setVisibility(View.GONE);
                viewBind.listChatRecord.setVisibility(View.VISIBLE);
            } else {
                viewBind.layoutNoData.setVisibility(View.VISIBLE);
                viewBind.listChatRecord.setVisibility(View.GONE);
            }
        }
    };

    private void getData() {
        list.add(new MessageBean("0","", "A随风奔跑的蒲公英", "上午11:58", "好的，暂时就这样",2));
        list.add(new MessageBean("1","", "A开花的仙人掌", "上午09:23", "明天上午九点见",1));
        list.add(new MessageBean("2","", "A东西南北", "上午07:51", "没得问题，具体的问题再详细讨论",2));
        list.add(new MessageBean("3","", "A室友", "昨天", "今天中午吃啥饭？面条还是米饭？",0));
        list.add(new MessageBean("4","", "B张大蓝", "昨天", "临时通知，明天上午10点的高数课改时间啊啊啊啊啊啊啊啊啊啊啊",0));
        list.add(new MessageBean("5","", "B学姐王时宜", "昨天", "去年好像是7月2日，今年具体得等通知了",0));

        for (MessageBean bean : list.get()) {
            count.set(count.get() + bean.getCount());
        }
        adapter.notifyDataSetChanged();
    }
}
