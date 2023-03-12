package com.mik1ng.chat.ui.main;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.mik1ng.chat.R;
import com.mik1ng.chat.base.BaseActivity;
import com.mik1ng.chat.databinding.ActivityMainBinding;
import com.mik1ng.chat.ui.main.FriendsFragment;
import com.mik1ng.chat.ui.main.MessageFragment;
import com.mik1ng.chat.ui.main.MineFragment;
import com.mik1ng.chat.util.FixFragmentNavigator;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private NavController navController;

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
    }

    @Override
    public void initData() {

    }

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
}