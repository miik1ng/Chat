package com.mik1ng.chat.ui.chat;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mik1ng.chat.adapter.ChatAdapter;
import com.mik1ng.chat.base.BaseFragment;
import com.mik1ng.chat.databinding.FragmentChatBinding;
import com.mik1ng.chat.entity.ChatRecordEntity;
import com.mik1ng.chat.event.CloseChatFragmentEvent;
import com.mik1ng.chat.event.SendChatRecordEvent;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends BaseFragment<FragmentChatBinding> {

    private List<ChatRecordEntity> list = new ArrayList<>();
    private ChatAdapter adapter;
    private String avatar;
    private int windowHeight = 0;

    @Override
    public FragmentChatBinding getViewBind() {
        return FragmentChatBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewBind.layoutBack.setOnClickListener(backListener);

        viewBind.listChatRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatAdapter(getContext(), list);
        viewBind.listChatRecord.setAdapter(adapter);

        viewBind.listChatRecord.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!viewBind.listChatRecord.canScrollVertically(-1)) {
                    Log.i("mmmmmm", "到了");
                }
            }
        });

        viewBind.getRoot().setOnApplyWindowInsetsListener(applyWindowInsetsListener);

        viewBind.etInput.setOnEditorActionListener(onEditorActionListener);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString(Constant.BUNDLE_NAME);
            viewBind.tvName.setText(name);
            avatar = bundle.getString(Constant.BUNDLE_AVATAR);
        }

        getData();
    }

    /**
     * 返回
     */
    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new CloseChatFragmentEvent());
        }
    };

    /**
     * 发送
     */
    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEND) {
                EventBus.getDefault().post(new SendChatRecordEvent(viewBind.etInput.getText().toString()));
            }
            return false;
        }
    };

    private View.OnApplyWindowInsetsListener applyWindowInsetsListener = new View.OnApplyWindowInsetsListener() {
        @Override
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            Rect rect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int height = rect.height();
            if (windowHeight == 0) {
                windowHeight = height;
            } else {
                LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) viewBind.layoutInput.getLayoutParams());
                if (windowHeight != height) {
                    layoutParams.bottomMargin = windowHeight - height;
                    viewBind.layoutInput.setLayoutParams(layoutParams);
                } else {
                    layoutParams.bottomMargin = 0;
                    viewBind.layoutInput.setLayoutParams(layoutParams);
                }
                viewBind.listChatRecord.scrollToPosition(list.size() - 1);
            }

            return windowInsets;
        }
    };

    int count = 1;

    private void getData() {
        for (int i = 0; i < 20; i++) {
            ChatRecordEntity entity = new ChatRecordEntity();
            switch (i % 9) {
                case 0:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_TEXT);
                    entity.setAvatar(avatar);
                    entity.setText("唯美动漫 动漫 卡通 卡通人物 可爱动漫 图片大全 高清图片下载 分辨率:350DPI 尺寸：1920x1080编号为140122536022，图片格式为JPG文件，提供 1920x1080，1920x1280，900x383多种尺寸图片免费下载，支持电脑和手机图片软件编辑和修改。");
                    count++;
                    break;
                case 1:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_IMAGE);
                    entity.setAvatar(avatar);
                    entity.setImage("https://scpic.chinaz.net/files/pic/pic9/201401/apic3253.jpg");
                    entity.setImgWidth(650f);
                    entity.setImgHeight(365f);
                    break;
                case 2:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_TEXT);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setText("发送文本" + count);
                    count++;
                    break;
                case 3:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_VOICE);
                    entity.setAvatar(avatar);
                    entity.setSecond(5);
                    break;
                case 4:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_VOICE);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setSecond(7);
                    break;
                case 5:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_RECEIVE);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_LOCATION);
                    entity.setAvatar(avatar);
                    entity.setLocationName("重庆师范大学（大学城校区）");
                    entity.setLocationAddress("重庆市沙坪坝区大学城中路37号");
                    break;
                case 6:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_IMAGE);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setImage("https://scpic.chinaz.net/files/pic/pic9/201208/xpic6765.jpg");
                    entity.setImgWidth(650f);
                    entity.setImgHeight(987f);
                    break;
                case 7:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_SEND);
                    entity.setRecordType(Constant.CHAT_RECORD_TYPE_LOCATION);
                    entity.setAvatar(Constant.MY_AVATAR);
                    entity.setLocationName("重庆师范大学（大学城校区）");
                    entity.setLocationAddress("重庆市沙坪坝区大学城中路37号");
                    break;
                case 8:
                    entity.setRecordMode(Constant.CHAT_RECORD_MODE_TIME);
                    entity.setTimestamp(DateUtils.getTimeStamp());
                    break;
            }
            list.add(entity);
        }
        adapter.notifyItemRangeChanged(0, list.size());
        viewBind.listChatRecord.scrollToPosition(list.size()-1);
    }
}
