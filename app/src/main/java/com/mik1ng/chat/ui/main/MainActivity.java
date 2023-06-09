package com.mik1ng.chat.ui.main;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mik1ng.chat.R;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityMainBinding;
import com.mik1ng.chat.entity.ChatMessageJsonEntity;
import com.mik1ng.chat.event.CreateNewChatEvent;
import com.mik1ng.chat.event.OpenChatEvent;
import com.mik1ng.chat.event.ReceiveChatMessageEvent;
import com.mik1ng.chat.event.ReceiveNewFriendMessageEvent;
import com.mik1ng.chat.event.RefreshFriendsCountEvent;
import com.mik1ng.chat.event.RefreshMessageCountEvent;
import com.mik1ng.chat.network.ConnectStatus;
import com.mik1ng.chat.network.MyWebSocket;
import com.mik1ng.chat.network.WebSocketCallback;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.FixFragmentNavigator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private NavController navController;
    private FragmentManager fragmentManager;

    private MyWebSocket webSocket;

    private final String TAG = "MainActivity------";

    @Override
    public ActivityMainBinding getViewBind() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initNavigator();
        viewBind.navigation.setItemIconTintList(null);
        viewBind.navigation.setOnItemSelectedListener(item -> {
            navController.navigate(item.getItemId());
            return true;
        });

        initMessageTab();
        fragmentManager = getSupportFragmentManager();

        webSocket = MyWebSocket.getInstance(this, "ws://" + Constant.RELEASE_IP + "/fit/websocket/" + Constant.USER_ID);
        webSocket.addWebSocketCallback(callback);
        if (webSocket.getStatus() != ConnectStatus.Open) {
            webSocket.connect();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean useEvent() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocket.removeWebSocketCallback(callback);
        webSocket.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private int friendCount = 0;

    public int getFriendCount() {
        return friendCount;
    }

    private WebSocketCallback callback = new WebSocketCallback() {
        @Override
        public void onOpen() {
            Log.i(TAG, webSocket.getStatus().name());
        }

        @Override
        public void onMessage(String text) {
            ChatMessageJsonEntity chatMessageEntity = new Gson().fromJson(text, new TypeToken<ChatMessageJsonEntity>() {
            }.getType());
            switch (chatMessageEntity.getType()) {
                case Constant.MESSAGE_CHAT:
                    EventBus.getDefault().post(new ReceiveChatMessageEvent(chatMessageEntity.getContent()));
                    break;
                case Constant.MESSAGE_NEW_FRIEND:
                    friendCount++;
                    EventBus.getDefault().post(new ReceiveNewFriendMessageEvent(friendCount));
                    EventBus.getDefault().post(new RefreshFriendsCountEvent(friendCount));
                    break;
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

    private void initNavigator() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);
        assert fragment != null;
        navController = NavHostFragment.findNavController(fragment);
        NavigatorProvider provider = navController.getNavigatorProvider();
        //设置自定义的navigator
        FixFragmentNavigator fixFragmentNavigator = new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId());
        provider.addNavigator(fixFragmentNavigator);

        NavGraph navDestinations = initNavGraph(provider, fixFragmentNavigator);
        navController.setGraph(navDestinations);

    }

    private NavGraph initNavGraph(NavigatorProvider provider, FixFragmentNavigator fragmentNavigator) {
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        FragmentNavigator.Destination destination1 = fragmentNavigator.createDestination();
        destination1.setId(R.id.navigation_message);
        destination1.setClassName(MessageFragment.class.getCanonicalName());
        navGraph.addDestination(destination1);

        FragmentNavigator.Destination destination2 = fragmentNavigator.createDestination();
        destination2.setId(R.id.navigation_friends);
        destination2.setClassName(FriendsFragment.class.getCanonicalName());
        navGraph.addDestination(destination2);

        FragmentNavigator.Destination destination3 = fragmentNavigator.createDestination();
        destination3.setId(R.id.navigation_mine);
        destination3.setClassName(MineFragment.class.getCanonicalName());
        navGraph.addDestination(destination3);

        navGraph.setStartDestination(destination1.getId());
        return navGraph;
    }

    private TextView tvMessageCount;
    private TextView tvFriendsCount;

    private void initMessageTab() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) viewBind.navigation.getChildAt(0);

        BottomNavigationItemView messageTab = (BottomNavigationItemView) menuView.getChildAt(0);
        ConstraintLayout messageLayout = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.dot_red, menuView, false);
        tvMessageCount = messageLayout.findViewById(R.id.tv_count);
        messageTab.addView(messageLayout);

        BottomNavigationItemView friendsTab = (BottomNavigationItemView) menuView.getChildAt(1);
        ConstraintLayout friendsLayout = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.dot_red, menuView, false);
        tvFriendsCount = friendsLayout.findViewById(R.id.tv_count);
        friendsTab.addView(friendsLayout);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setMessagecount(RefreshMessageCountEvent event) {
        if (event.getCount() > 0) {
            if (event.getCount() > 99) {
                tvMessageCount.setText("···");
            } else {
                tvMessageCount.setText(String.valueOf(event.getCount()));
            }
            tvMessageCount.setVisibility(View.VISIBLE);
        } else {
            tvMessageCount.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setFriendsCount(RefreshFriendsCountEvent event) {
        friendCount = event.getCount();
        if (event.getCount() > 0) {
            if (event.getCount() > 99) {
                tvFriendsCount.setText("···");
            } else {
                tvFriendsCount.setText(String.valueOf(event.getCount()));
            }
            tvFriendsCount.setVisibility(View.VISIBLE);
        } else {
            tvFriendsCount.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createNewChat(CreateNewChatEvent event) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewBind.navigation.setSelectedItemId(R.id.navigation_message);
                EventBus.getDefault().post(new OpenChatEvent(event.getUserID(), event.getName(), event.getAvatar()));
            }
        }, 100);
    }
}