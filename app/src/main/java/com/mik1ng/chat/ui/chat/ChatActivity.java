package com.mik1ng.chat.ui.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.mik1ng.chat.R;
import com.mik1ng.chat.adapter.ChatAdapter;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityChatBinding;
import com.mik1ng.chat.entity.ChatMessageEntity;
import com.mik1ng.chat.entity.ChatMessageJsonEntity;
import com.mik1ng.chat.entity.ChatRecordEntity;
import com.mik1ng.chat.event.SendChatMessageEvent;
import com.mik1ng.chat.event.SendLocationEvent;
import com.mik1ng.chat.greendao.ChatRecordEntityDao;
import com.mik1ng.chat.greendao.manager.DaoManager;
import com.mik1ng.chat.network.ConnectStatus;
import com.mik1ng.chat.network.MyWebSocket;
import com.mik1ng.chat.network.WebSocketCallback;
import com.mik1ng.chat.ui.map.MapActivity;
import com.mik1ng.chat.ui.map.ShowLocationMapActivity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.DateUtils;
import com.mik1ng.chat.util.DeviceUtils;
import com.mik1ng.chat.util.FileUtils;
import com.mik1ng.chat.util.GlideEngine;
import com.mik1ng.chat.util.MediaUtils;
import com.mik1ng.chat.weight.VoiceView;
import com.tbruyelle.rxpermissions3.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;

@SuppressLint("HandlerLeak")
public class ChatActivity extends BaseActivity<ActivityChatBinding> {

    private List<ChatRecordEntity> list = new ArrayList<>();
    private ChatAdapter adapter;
    private String avatar;
    private String nickName;
    private int id = -1;
    private int messageCount;
    private float keyBoardHeight = -999999;

    private MyWebSocket webSocket;
    private final String TAG = "ChatActivity------";

    private ChatRecordEntityDao dao;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String json = msg.obj.toString();
            ChatMessageEntity chatMessageEntity = new Gson().fromJson(json, new TypeToken<ChatMessageEntity>() {}.getType());
            if (chatMessageEntity != null) {
                if (chatMessageEntity.getFromUser() == id) {
                    switch (chatMessageEntity.getType()) {
                        case Constant.CHAT_RECORD_TYPE_TEXT:
                            receiveText(chatMessageEntity);
                            break;
                        case Constant.CHAT_RECORD_TYPE_IMAGE:
                            receiveImg(chatMessageEntity);
                            break;
                        case Constant.CHAT_RECORD_TYPE_VOICE:
                            receiveVoice(chatMessageEntity);
                            break;
                        case Constant.CHAT_RECORD_TYPE_LOCATION:
                            receiveLocation(chatMessageEntity);
                            break;
                    }
                } else {
                    messageCount = messageCount + 1;
                    updateCount(messageCount);
                }
            }
        }
    };

    @Override
    public ActivityChatBinding getViewBind() {
        return ActivityChatBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.layoutBack.setOnClickListener(backListener);
        viewBind.ivPicture.setOnClickListener(picListener);
        viewBind.ivVoice.setOnClickListener(voiceListener);
        viewBind.vvVoice.setMaxTime(60000);
        viewBind.vvVoice.setOnPressedListener(onPressedListener);

        viewBind.listChatRecord.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(this, list);
        viewBind.listChatRecord.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);

        viewBind.listChatRecord.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    DeviceUtils.hideSoftKeyboard(ChatActivity.this, viewBind.etInput);
                }
                if (!viewBind.listChatRecord.canScrollVertically(-1)) {
                    //Log.i("mmmmmm", "到了");
                }
            }
        });

        viewBind.getRoot().setOnApplyWindowInsetsListener(applyWindowInsetsListener);
        viewBind.etInput.setOnEditorActionListener(onEditorActionListener);
        viewBind.ivLocation.setOnClickListener(locationListener);

        viewBind.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyBoardHeight == -999999) {
                    keyBoardHeight = DeviceUtils.getKeyboardHeight(ChatActivity.this);
                }
            }
        });

        webSocket = MyWebSocket.getInstance(this, "ws://" + Constant.RELEASE_IP + "/fit/websocket/" + Constant.USER_ID);
        webSocket.addWebSocketCallback(callback);
        if (webSocket.getStatus() != ConnectStatus.Open) {
            webSocket.connect();
        }
    }

    @Override
    public void initData() {
        //初始化数据库
        DaoManager daoManager = DaoManager.getInstance();
        daoManager.init(this);
        dao = daoManager.getDaoSession().getChatRecordEntityDao();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nickName = bundle.getString(Constant.BUNDLE_NAME);
            id = bundle.getInt(Constant.BUNDLE_ID);
            Constant.CHAT_USER_ID = id;
            viewBind.tvName.setText(nickName);
            avatar = bundle.getString(Constant.BUNDLE_AVATAR);
            messageCount = bundle.getInt(Constant.BUNDLE_MESSAGE_COUNT);
            updateCount(messageCount);

            if (id != -1) {
                getDBData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //webSocket.close();
        webSocket.removeWebSocketCallback(callback);
        Constant.CHAT_USER_ID = -1;
    }

    @Override
    public boolean useEvent() {
        return true;
    }

    private WebSocketCallback callback = new WebSocketCallback() {
        @Override
        public void onOpen() {
            Log.i(TAG, webSocket.getStatus().name());
        }

        @Override
        public void onMessage(String text) {
            ChatMessageJsonEntity chatMessageEntity = new Gson().fromJson(text, new TypeToken<ChatMessageJsonEntity>() {}.getType());
            if (chatMessageEntity.getType() == Constant.MESSAGE_CHAT) {
                if (!TextUtils.isEmpty(chatMessageEntity.getContent())) {
                    Message msg = new Message();
                    msg.obj = chatMessageEntity.getContent();
                    handler.sendMessage(msg);
                }
            }
        }

        @Override
        public void onClosing() {

        }

        @Override
        public void onClose() {
            Log.i(TAG, webSocket.getStatus().name());
            //webSocket.reConnect();
        }

        @Override
        public void onConnectError(Throwable throwable) {
            Log.i(TAG, webSocket.getStatus().name());
            //webSocket.reConnect();
        }
    };

    /**
     * 返回
     */
    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    /**
     * 语音按钮
     */
    View.OnClickListener voiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DeviceUtils.hideSoftKeyboard(ChatActivity.this, viewBind.etInput);
            if (viewBind.layoutVoice.getVisibility() == View.GONE) {
                int height = DeviceUtils.getKeyboardHeight(ChatActivity.this);
                if (height <= keyBoardHeight) {
                    viewBind.layoutVoice.setVisibility(View.VISIBLE);
                }
            } else {
                viewBind.layoutVoice.setVisibility(View.GONE);
            }
            viewBind.listChatRecord.scrollToPosition(list.size() - 1);
        }
    };

    private float voiceTime = 0;
    private String voicePath;
    /**
     * 语音长按监听
     */
    VoiceView.OnPressedListener onPressedListener = new VoiceView.OnPressedListener() {
        @Override
        public boolean onStart() {
            voiceTime = 0;
            RxPermissions rxPermissions = new RxPermissions(ChatActivity.this);
            if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)) {
                viewBind.tvVoiceTip.setText(getString(R.string.chat_voice_second, String.valueOf(voiceTime)));
                voicePath = getFilesDir() + "/" + DateUtils.getTimeStamp() + Constant.FILE_TYPE_MP4;
                MediaUtils.startRecording(voicePath);
                return true;
            } else {
                rxPermissions.request(Manifest.permission.RECORD_AUDIO)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Throwable {

                            }
                        });
                return false;
            }
        }

        @Override
        public void onInterval(float time) {
            voiceTime = time;
            viewBind.tvVoiceTip.setText(getString(R.string.chat_voice_second, String.valueOf(time)));
        }

        @Override
        public void onFinished() {
            viewBind.tvVoiceTip.setText(R.string.chat_voice_tip);
            MediaUtils.stopRecording();

            if (voiceTime > 1 && !TextUtils.isEmpty(voicePath)) {
                String base64String = FileUtils.createBase64WithFile(voicePath);
                ChatMessageEntity chatMessageEntity = new ChatMessageEntity(Constant.CHAT_RECORD_TYPE_VOICE, Constant.USER_ID, Constant.USER_NAME, Constant.MY_AVATAR, base64String, (int) voiceTime);
                String contentJson = new Gson().toJson(chatMessageEntity);
                sendMessage(contentJson);

                ChatRecordEntity entity = new ChatRecordEntity();
                entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                entity.setRecordType(Constant.CHAT_RECORD_TYPE_VOICE);
                entity.setAvatar(Constant.MY_AVATAR);
                entity.setMediaPath(voicePath);
                entity.setSecond((int) voiceTime);
                updateRecord(entity);
            }
            voiceTime = 0;
            voicePath = null;
        }
    };

    /**
     * 定位
     */
    View.OnClickListener locationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RxPermissions rxPermissions = new RxPermissions(ChatActivity.this);
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Throwable {
                            if (aBoolean) {
                                startActivity(new Intent(ChatActivity.this, MapActivity.class));
                            }
                        }
                    });
        }
    };

    /**
     * 图片
     */
    View.OnClickListener picListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectPic();
        }
    };

    /**
     * 发送
     */
    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEND) {
                if (!TextUtils.isEmpty(viewBind.etInput.getText().toString())) {
                    //定义发送的消息内容
                    ChatMessageEntity chatMessageEntity = new ChatMessageEntity(Constant.CHAT_RECORD_TYPE_TEXT, Constant.USER_ID, Constant.USER_NAME, Constant.MY_AVATAR, viewBind.etInput.getText().toString());
                    String contentJson = new Gson().toJson(chatMessageEntity);
                    sendMessage(contentJson);

                    ChatRecordEntity entity = new ChatRecordEntity();
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_TEXT);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setText(viewBind.etInput.getText().toString());
                    updateRecord(entity);

                    viewBind.etInput.setText("");
                }
            }
            return true;
        }
    };

    /**
     * 点击聊天记录
     */
    ChatAdapter.OnItemClickListener onItemClickListener = new ChatAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            ChatRecordEntity entity = list.get(position);
            switch (entity.getRecordType()) {
                case Constant.CHAT_RECORD_TYPE_IMAGE:
                    break;
                case Constant.CHAT_RECORD_TYPE_VOICE:
                    MediaUtils.playRecording(list.get(position).getMediaPath());
                    break;
                case Constant.CHAT_RECORD_TYPE_LOCATION:
                    RxPermissions rxPermissions = new RxPermissions(ChatActivity.this);
                    rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_WIFI_STATE,
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.CHANGE_WIFI_STATE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Throwable {
                                    if (aBoolean) {
                                        Intent intent = new Intent(ChatActivity.this, ShowLocationMapActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putDouble(Constant.BUNDLE_LAT, entity.getLatitude());
                                        bundle.putDouble(Constant.BUNDLE_LOG, entity.getLongitude());
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            });
                    break;
            }
        }
    };

    private View.OnApplyWindowInsetsListener applyWindowInsetsListener = new View.OnApplyWindowInsetsListener() {
        @Override
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            int height = 0;
            if (keyBoardHeight != -999999) {
                height = (int) (DeviceUtils.getKeyboardHeight(ChatActivity.this) - keyBoardHeight);
            }
            if (height == 0) {
                viewBind.etInput.clearFocus();
            }
            viewBind.layoutVoice.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) viewBind.layoutInput.getLayoutParams());
            layoutParams.bottomMargin = height;
            viewBind.layoutInput.setLayoutParams(layoutParams);
            viewBind.listChatRecord.scrollToPosition(list.size() - 1);
            return windowInsets;
        }
    };

    private void getDBData() {
        List<com.mik1ng.chat.greendao.entity.ChatRecordEntity> dbList = dao.queryBuilder().where(ChatRecordEntityDao.Properties.MyID.eq(Constant.USER_ID)).where(ChatRecordEntityDao.Properties.UserID.eq(id)).list();
        if (dbList != null) {
            list.clear();
            for (com.mik1ng.chat.greendao.entity.ChatRecordEntity entity : dbList) {
                ChatRecordEntity chatRecordEntity = new ChatRecordEntity();
                chatRecordEntity.setRecordMode(entity.getRecordMode());
                chatRecordEntity.setRecordType(entity.getRecordType());
                if (entity.getRecordMode() == Constant.CHAT_RECORD_MODE_SEND) {
                    chatRecordEntity.setAvatar(Constant.MY_AVATAR);
                } else if (entity.getRecordMode() == Constant.CHAT_RECORD_MODE_RECEIVE) {
                    chatRecordEntity.setAvatar(avatar);
                }
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
                list.add(chatRecordEntity);
            }
            adapter.notifyItemRangeChanged(0, list.size());
            viewBind.listChatRecord.scrollToPosition(list.size() - 1);
        }
    }

    /**
     * 更新聊天记录界面
     * @param entity
     */
    private void updateRecord(ChatRecordEntity entity) {
        list.add(entity);
        adapter.notifyItemInserted(list.size());
        viewBind.listChatRecord.scrollToPosition(list.size() - 1);

        if (entity.getRecordMode() == Constant.CHAT_RECORD_MODE_SEND) {
            //聊天记录插入到数据库
            com.mik1ng.chat.greendao.entity.ChatRecordEntity chatRecordEntity = new com.mik1ng.chat.greendao.entity.ChatRecordEntity();
            chatRecordEntity.setMyID(Constant.USER_ID);
            chatRecordEntity.setUserID(id);
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
            dao.insert(chatRecordEntity);
        }
    }

    /**
     * 更新未读消息数量
     * @param messageCount
     */
    private void updateCount(int messageCount) {
        viewBind.tvMessageCount.setText(String.valueOf(messageCount));
        if (messageCount > 0) {
            viewBind.tvMessageCount.setVisibility(View.VISIBLE);
        } else {
            viewBind.tvMessageCount.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 发送消息体
     * @param contentJson
     */
    private void sendMessage(String contentJson) {
        ChatMessageJsonEntity chatMessageJsonEntity = new ChatMessageJsonEntity();
        chatMessageJsonEntity.setFromUserId(Constant.USER_ID);
        chatMessageJsonEntity.setToUserId(id);
        chatMessageJsonEntity.setType(Constant.MESSAGE_CHAT);
        chatMessageJsonEntity.setContent(contentJson);
        String json = new Gson().toJson(chatMessageJsonEntity);
        webSocket.send(json);
        EventBus.getDefault().post(new SendChatMessageEvent(id, contentJson));
    }

    /**
     * 接收文字消息
     * @param chatMessageEntity
     */
    private void receiveText(ChatMessageEntity chatMessageEntity) {
        ChatRecordEntity entity = new ChatRecordEntity();
        entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
        entity.setRecordType(Constant.CHAT_RECORD_TYPE_TEXT);
        entity.setAvatar(avatar);
        entity.setText(chatMessageEntity.getText());
        updateRecord(entity);
    }

    /**
     * 接收图片消息
     * @param chatMessageEntity
     */
    private void receiveImg(ChatMessageEntity chatMessageEntity) {
        ChatRecordEntity entity = new ChatRecordEntity();
        entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
        entity.setRecordType(Constant.CHAT_RECORD_TYPE_IMAGE);
        entity.setAvatar(avatar);
        entity.setImage(FileUtils.createFileWithBase64(this, chatMessageEntity.getPicBase64(), Constant.FILE_TYPE_PNG));
        entity.setImgWidth(chatMessageEntity.getPicWidth());
        entity.setImgHeight(chatMessageEntity.getPicHeight());
        updateRecord(entity);
    }

    /**
     * 接收语音消息
     * @param chatMessageEntity
     */
    private void receiveVoice(ChatMessageEntity chatMessageEntity) {
        ChatRecordEntity entity = new ChatRecordEntity();
        entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
        entity.setRecordType(Constant.CHAT_RECORD_TYPE_VOICE);
        entity.setAvatar(avatar);
        entity.setMediaPath(FileUtils.createFileWithBase64(this,chatMessageEntity.getVoiceBase64(),Constant.FILE_TYPE_MP4));
        entity.setSecond(chatMessageEntity.getSecond());
        updateRecord(entity);
    }

    /**
     * 接收定位消息
     * @param chatMessageEntity
     */
    private void receiveLocation(ChatMessageEntity chatMessageEntity) {
        ChatRecordEntity entity = new ChatRecordEntity();
        entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
        entity.setRecordType(Constant.CHAT_RECORD_TYPE_LOCATION);
        entity.setAvatar(avatar);
        entity.setLocationName(chatMessageEntity.getLocationName());
        entity.setLocationAddress(chatMessageEntity.getLocationAddress());
        entity.setLatitude(chatMessageEntity.getLocationLat());
        entity.setLongitude(chatMessageEntity.getLocationLog());
        updateRecord(entity);
    }

    /**
     * 选择图片
     */
    private void selectPic() {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(1)
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setImageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        if (result != null) {
                            if (result.size() > 0) {
                                LocalMedia localMedia = result.get(0);
                                String path = localMedia.getRealPath();
                                int picWidth = localMedia.getWidth();
                                int picHeight = localMedia.getHeight();

                                String base64String = FileUtils.createBase64WithFile(path);

                                //定义发送的消息内容
                                ChatMessageEntity chatMessageEntity = new ChatMessageEntity(Constant.CHAT_RECORD_TYPE_IMAGE, Constant.USER_ID, Constant.USER_NAME, Constant.MY_AVATAR, base64String, picWidth, picHeight);
                                String contentJson = new Gson().toJson(chatMessageEntity);
                                sendMessage(contentJson);

                                ChatRecordEntity entity = new ChatRecordEntity();
                                entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                                entity.setRecordType(Constant.CHAT_RECORD_TYPE_IMAGE);
                                entity.setAvatar(Constant.MY_AVATAR);
                                entity.setImage(path);
                                entity.setImgWidth(localMedia.getWidth());
                                entity.setImgHeight(localMedia.getHeight());
                                updateRecord(entity);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendLocation(SendLocationEvent event) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity(Constant.CHAT_RECORD_TYPE_LOCATION, Constant.USER_ID, Constant.USER_NAME, Constant.MY_AVATAR, String.valueOf(event.getResult()[0]), String.valueOf(event.getResult()[1]), (double) event.getResult()[2], (double) event.getResult()[3]);
        String contentJson = new Gson().toJson(chatMessageEntity);
        sendMessage(contentJson);

        ChatRecordEntity entity = new ChatRecordEntity();
        entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
        entity.setRecordType(Constant.CHAT_RECORD_TYPE_LOCATION);
        entity.setAvatar(Constant.MY_AVATAR);
        entity.setLocationName(String.valueOf(event.getResult()[0]));
        entity.setLocationAddress(String.valueOf(event.getResult()[1]));
        entity.setLatitude((double) event.getResult()[2]);
        entity.setLongitude((double) event.getResult()[3]);
        updateRecord(entity);
    }
}
