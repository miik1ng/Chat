package com.mik1ng.chat.base;

import android.app.Application;

import com.mik1ng.chat.network.NetworkRequiredInfo;
import com.mik1ng.network.NetworkApi;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        NetworkApi.init(this, new NetworkRequiredInfo(this));
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
