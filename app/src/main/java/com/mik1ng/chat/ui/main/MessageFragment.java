package com.mik1ng.chat.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mik1ng.chat.adapter.MessageAdapter;
import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentMessageBinding;
import com.mik1ng.chat.entity.ChatMessageEntity;
import com.mik1ng.chat.entity.ChatRecordEntity;
import com.mik1ng.chat.entity.FriendEntity;
import com.mik1ng.chat.entity.GetFriendListEntity;
import com.mik1ng.chat.entity.MessageEntity;
import com.mik1ng.chat.entity.SearchUserEntity;
import com.mik1ng.chat.event.OpenChatEvent;
import com.mik1ng.chat.event.ReceiveChatMessageEvent;
import com.mik1ng.chat.event.RefreshMessageCountEvent;
import com.mik1ng.chat.event.SendChatMessageEvent;
import com.mik1ng.chat.greendao.MessageEntityDao;
import com.mik1ng.chat.greendao.manager.DaoManager;
import com.mik1ng.chat.interfaces.Observe;
import com.mik1ng.chat.interfaces.OnItemClickListener;
import com.mik1ng.chat.interfaces.OnItemLongClickListener;
import com.mik1ng.chat.network.Api;
import com.mik1ng.chat.observable.ArrayListObservable;
import com.mik1ng.chat.observable.IntegerObservable;
import com.mik1ng.chat.ui.chat.ChatActivity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.FileUtils;
import com.mik1ng.network.observer.BaseObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MessageFragment extends BaseFragment<FragmentMessageBinding> {

    private ArrayListObservable<MessageEntity> list = new ArrayListObservable<>();
    private List<FriendEntity> entities = new ArrayList<>();        //好友列表
    private MessageAdapter adapter;

    private DaoManager daoManager;
    private MessageEntityDao dao;

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
    }

    @Override
    public void initData() {
        daoManager = DaoManager.getInstance();
        daoManager.init(getContext());
        dao = daoManager.getDaoSession().getMessageEntityDao();

        count.addObserve(countObserve);
        list.addObserve(listObserve);

        //getData();
        getFriendData();
        getDBData();
    }

    @Override
    public boolean useEvent() {
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        updateDBData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.BUNDLE_ID, list.get(position).getUserID());
            bundle.putString(Constant.BUNDLE_NAME, list.get(position).getName());
            bundle.putString(Constant.BUNDLE_AVATAR, list.get(position).getAvatar());
            bundle.putInt(Constant.BUNDLE_MESSAGE_COUNT, count.get());
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
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
    Observe<ArrayList<MessageEntity>> listObserve = new Observe<ArrayList<MessageEntity>>() {
        @Override
        public void update(ArrayList<MessageEntity> messageEntities) {
            if (messageEntities.size() > 0) {
                viewBind.layoutNoData.setVisibility(View.GONE);
                viewBind.listChatRecord.setVisibility(View.VISIBLE);
            } else {
                viewBind.layoutNoData.setVisibility(View.VISIBLE);
                viewBind.listChatRecord.setVisibility(View.GONE);
            }
        }
    };

    private void getData() {
        list.add(new MessageEntity(0, "https://scpic.chinaz.net/files/pic/pic9/201910/zzpic20534.jpg", "A随风奔跑的蒲公英", "上午11:58", "好的，暂时就这样", 2));
        list.add(new MessageEntity(1, "https://scpic.chinaz.net/files/pic/pic9/201903/zzpic16869.jpg", "A开花的仙人掌", "上午09:23", "明天上午九点见", 1));
        list.add(new MessageEntity(2, "https://scpic.chinaz.net/files/pic/pic9/201901/zzpic16133.jpg", "A东西南北", "上午07:51", "没得问题，具体的问题再详细讨论", 2));
        list.add(new MessageEntity(3, "https://scpic.chinaz.net/files/pic/pic9/201608/fpic6657.jpg", "A室友", "昨天", "今天中午吃啥饭？面条还是米饭？", 0));
        list.add(new MessageEntity(4, "https://scpic.chinaz.net/files/pic/pic9/202008/bpic20969.jpg", "17333781851", "昨天", "临时通知，明天上午10点的高数课改时间啊啊啊啊啊啊啊啊啊啊啊", 0));
        list.add(new MessageEntity(Constant.USER_ID, "https://scpic.chinaz.net/files/pic/pic7/xpic944.jpg", "17333781852", "昨天", "去年好像是7月2日，今年具体得等通知了", 0));

        for (MessageEntity bean : list.get()) {
            count.set(count.get() + bean.getCount());
        }
        adapter.notifyDataSetChanged();
    }

    private void getFriendData() {
        Api.getFriends().subscribe(new BaseObserver<GetFriendListEntity>() {
            @Override
            public void onSuccess(GetFriendListEntity getFriendListEntity) {
                if (getFriendListEntity == null) return;
                if (getFriendListEntity.getCode() == 200) {
                    entities.clear();
                    for (GetFriendListEntity.DataBean dataBean : getFriendListEntity.getData()) {
                        entities.add(new FriendEntity(dataBean.getId(), dataBean.getRemark(), dataBean.getNickname(), dataBean.getAvatar()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    /**
     * 获取本地数据库数据
     */
    private void getDBData() {
        List<com.mik1ng.chat.greendao.entity.MessageEntity> dbList = dao.queryBuilder().where(MessageEntityDao.Properties.MyID.eq(Constant.USER_ID)).list();
        if (dbList != null) {
            list.clear();
            for (com.mik1ng.chat.greendao.entity.MessageEntity entity : dbList) {
                list.add(new MessageEntity(entity.getUserID(), entity.getAvatar(), entity.getName(), entity.getDate(), entity.getContent(), entity.getCount()));
            }
            for (MessageEntity bean : list.get()) {
                count.set(count.get() + bean.getCount());
            }
            adapter.notifyItemRangeChanged(0, list.size());
        }
    }

    private void updateDBData() {
        QueryBuilder<com.mik1ng.chat.greendao.entity.MessageEntity> queryBuilder = dao.queryBuilder();
        queryBuilder.where(MessageEntityDao.Properties.MyID.eq(Constant.USER_ID));
        queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();

        for (MessageEntity entity : list.get()) {
            com.mik1ng.chat.greendao.entity.MessageEntity messageEntity = new com.mik1ng.chat.greendao.entity.MessageEntity();
            messageEntity.setMyID(Constant.USER_ID);
            messageEntity.setUserID(entity.getUserID());
            messageEntity.setAvatar(entity.getAvatar());
            messageEntity.setName(entity.getName());
            messageEntity.setDate(entity.getDate());
            messageEntity.setContent(entity.getContent());
            messageEntity.setCount(entity.getCount());
            messageEntity.setTimestamp(entity.getTimestamp());
            dao.insert(messageEntity);
        }
    }

    private void insertChatRecord(ChatMessageEntity chatMessageEntity) {
        ChatRecordEntity entity = new ChatRecordEntity();
        entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
        entity.setRecordType(chatMessageEntity.getType());
        entity.setText(chatMessageEntity.getText());
        entity.setImage(FileUtils.createFileWithBase64(getContext(), chatMessageEntity.getPicBase64(), Constant.FILE_TYPE_PNG));
        entity.setImgWidth(chatMessageEntity.getPicWidth());
        entity.setImgHeight(chatMessageEntity.getPicHeight());
        entity.setMediaPath(FileUtils.createFileWithBase64(getContext(),chatMessageEntity.getVoiceBase64(),Constant.FILE_TYPE_MP4));
        entity.setSecond(chatMessageEntity.getSecond());
        entity.setLocationName(chatMessageEntity.getLocationName());
        entity.setLocationAddress(chatMessageEntity.getLocationAddress());
        entity.setLatitude(chatMessageEntity.getLocationLat());
        entity.setLongitude(chatMessageEntity.getLocationLog());

        com.mik1ng.chat.greendao.entity.ChatRecordEntity chatRecordEntity = new com.mik1ng.chat.greendao.entity.ChatRecordEntity();
        chatRecordEntity.setMyID(Constant.USER_ID);
        chatRecordEntity.setUserID(chatMessageEntity.getFromUser());
        chatRecordEntity.setRecordMode(entity.getRecordMode());
        chatRecordEntity.setRecordType(entity.getRecordType());
        chatRecordEntity.setText(entity.getText());
        chatRecordEntity.setImage(entity.getImage());
        chatRecordEntity.setImgWidth(entity.getImgWidth());
        chatRecordEntity.setImgHeight(entity.getImgHeight());
        chatRecordEntity.setSecond(entity.getSecond());
        chatRecordEntity.setMediaPath(entity.getMediaPath());
        chatRecordEntity.setLocationName(entity.getLocationName());
        chatRecordEntity.setLocationAddress(entity.getLocationAddress());
        chatRecordEntity.setLongitude(entity.getLongitude());
        chatRecordEntity.setLatitude(entity.getLatitude());
        chatRecordEntity.setTimestamp(entity.getTimestamp());
        daoManager.getDaoSession().getChatRecordEntityDao().insert(chatRecordEntity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendMessage(SendChatMessageEvent event) {
        if (event != null) {
            ChatMessageEntity chatMessageEntity = new Gson().fromJson(event.getContent(), new TypeToken<ChatMessageEntity>() {
            }.getType());

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUserID() == event.getUserID()) {
//                    if (Constant.CHAT_USER_ID == event.getUserID()) {
//                        list.get(i).setCount(0);
//                    }
                    switch (chatMessageEntity.getType()) {
                        case Constant.CHAT_RECORD_TYPE_TEXT:
                            list.get(i).setContent(chatMessageEntity.getText());
                            break;
                        case Constant.CHAT_RECORD_TYPE_IMAGE:
                            list.get(i).setContent("[图片]");
                            break;
                        case Constant.CHAT_RECORD_TYPE_VOICE:
                            list.get(i).setContent("[语音]");
                            break;
                        case Constant.CHAT_RECORD_TYPE_LOCATION:
                            list.get(i).setContent("[位置]");
                            break;
                    }
                    adapter.notifyItemChanged(i,Constant.ADAPTER_PAYLOADS_2);
                    list.add(0, list.remove(i));
                    adapter.notifyItemMoved(i, 0);
                    break;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(ReceiveChatMessageEvent event) {
        if (event != null) {
            ChatMessageEntity chatMessageEntity = new Gson().fromJson(event.getContent(), new TypeToken<ChatMessageEntity>() {
            }.getType());

            insertChatRecord(chatMessageEntity);

            boolean b = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUserID() == chatMessageEntity.getFromUser()) {
                    if (Constant.CHAT_USER_ID != chatMessageEntity.getFromUser()) {
                        list.get(i).setCount(list.get(i).getCount() + 1);
                        count.set(count.get() + 1);
                    } else {
                        list.get(i).setCount(0);
                    }
                    switch (chatMessageEntity.getType()) {
                        case Constant.CHAT_RECORD_TYPE_TEXT:
                            list.get(i).setContent(chatMessageEntity.getText());
                            break;
                        case Constant.CHAT_RECORD_TYPE_IMAGE:
                            list.get(i).setContent("[图片]");
                            break;
                        case Constant.CHAT_RECORD_TYPE_VOICE:
                            list.get(i).setContent("[语音]");
                            break;
                        case Constant.CHAT_RECORD_TYPE_LOCATION:
                            list.get(i).setContent("[位置]");
                            break;
                    }
                    adapter.notifyItemChanged(i, Constant.ADAPTER_PAYLOADS_0);
                    adapter.notifyItemChanged(i,Constant.ADAPTER_PAYLOADS_2);
                    list.add(0, list.remove(i));
                    adapter.notifyItemMoved(i, 0);

                    b = true;
                    break;
                }
            }

            if (!b) {
                //新添加聊天
                String name = "";
                String avatar = "";
                for (FriendEntity friendEntity : entities) {
                    if (friendEntity.getId() == chatMessageEntity.getFromUser()) {
                        name = friendEntity.getNickName();
                        avatar = friendEntity.getAvatar();
                        break;
                    }
                }

                list.add(0, new MessageEntity(chatMessageEntity.getFromUser(), avatar, name, "", chatMessageEntity.getText(), 1));
                adapter.notifyItemInserted(0);
                count.set(count.get() + 1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openChat(OpenChatEvent event) {
        boolean b = false;
        for (int i = 0; i < list.size(); i++) {
            if (event.getUserID() == list.get(i).getUserID()) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.BUNDLE_ID, list.get(i).getUserID());
                bundle.putString(Constant.BUNDLE_NAME, list.get(i).getName());
                bundle.putString(Constant.BUNDLE_AVATAR, list.get(i).getAvatar());
                bundle.putInt(Constant.BUNDLE_MESSAGE_COUNT, count.get());
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtras(bundle);

                count.set(count.get() - list.get(i).getCount());
                list.get(i).setCount(0);
                list.add(0, list.remove(i));
                adapter.notifyItemMoved(i, 0);
                adapter.notifyItemChanged(0, Constant.ADAPTER_PAYLOADS_0);

                startActivity(intent);

                b = true;
                break;
            }
        }

        if (!b) {
            list.add(0, new MessageEntity(event.getUserID(), event.getAvatar(), event.getName(), "", "", 0));
            adapter.notifyItemInserted(0);

            Bundle bundle = new Bundle();
            bundle.putInt(Constant.BUNDLE_ID, event.getUserID());
            bundle.putString(Constant.BUNDLE_NAME, event.getName());
            bundle.putString(Constant.BUNDLE_AVATAR, event.getAvatar());
            bundle.putInt(Constant.BUNDLE_MESSAGE_COUNT, count.get());
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
