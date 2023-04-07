package com.mik1ng.chat.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(
                    view.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(view,
                InputMethodManager.SHOW_FORCED);
    }
}
