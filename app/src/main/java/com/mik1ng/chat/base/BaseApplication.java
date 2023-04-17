package com.mik1ng.chat.base;

import android.app.Application;

import com.amap.api.maps.MapsInitializer;
import com.mik1ng.chat.network.NetworkRequiredInfo;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.network.NetworkApi;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        NetworkApi.init(this, Constant.RELEASE_IP, Constant.DEBUG_IP, new NetworkRequiredInfo(this));

        MapsInitializer.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
