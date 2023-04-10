package com.mik1ng.chat.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.mik1ng.chat.R;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityMainBinding;
import com.mik1ng.chat.event.CloseChatFragmentEvent;
import com.mik1ng.chat.event.OpenChatFragmentEvent;
import com.mik1ng.chat.event.RefreshFriendsCountEvent;
import com.mik1ng.chat.event.RefreshMessageCountEvent;
import com.mik1ng.chat.event.SendChatRecordEvent;
import com.mik1ng.chat.network.MyWebSocket;
import com.mik1ng.chat.network.WebSocketCallback;
import com.mik1ng.chat.ui.chat.ChatFragment;
import com.mik1ng.chat.ui.main.FriendsFragment;
import com.mik1ng.chat.ui.main.MessageFragment;
import com.mik1ng.chat.ui.main.MineFragment;
import com.mik1ng.chat.util.FixFragmentNavigator;

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


//        webSocket = MyWebSocket.getInstance("wss://demo.piesocket.com/v3/channel_1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self");
        webSocket = MyWebSocket.getInstance("ws://123.249.43.238:8080/fit/websocket/aa");
        webSocket.setWebSocketCallback(callback);
        webSocket.connect();
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
//        webSocket.cancel();
        webSocket.close();
    }

    private WebSocketCallback callback = new WebSocketCallback() {
        @Override
        public void onOpen() {
            Log.i(TAG, webSocket.getStatus().name());
        }

        @Override
        public void onMessage(String text) {
            Log.i(TAG, "收到：" + text);
        }

        @Override
        public void onClose() {
            Log.i(TAG, webSocket.getStatus().name());
        }

        @Override
        public void onConnectError(Throwable throwable) {
            Log.i(TAG, webSocket.getStatus().name());
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
    public void openChatFragment(OpenChatFragmentEvent event) {
        Bundle bundle = event.getBundle();
        viewBind.fragment.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment, ChatFragment.class, bundle)
                .commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeChatFragment(CloseChatFragmentEvent event) {
        fragmentManager.popBackStack();
        viewBind.fragment.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendChatRecordEvent(SendChatRecordEvent event) {
        webSocket.send(event.getRecord());
    }
}