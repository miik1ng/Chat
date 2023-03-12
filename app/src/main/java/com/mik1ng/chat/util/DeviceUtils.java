package com.mik1ng.chat.util;

import android.content.Context;
import android.content.pm.PackageManager;

public class DeviceUtils {
    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version;
        try {
            version = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            version = "";
        }
        return version;
    }
}
