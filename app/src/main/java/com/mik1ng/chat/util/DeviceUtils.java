package com.mik1ng.chat.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
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

    /**
     * 打开软键盘
     * @param context
     * @param view
     */
    public static void showSoftKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(view,
                InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获取软键盘的高度
     * @param activity
     * @return
     */
    public static int getKeyboardHeight(Activity activity) {
        Rect r = new Rect();
        View root = activity.getWindow().getDecorView();
        root.getWindowVisibleDisplayFrame(r);
        int screenHeight = root.getHeight();
        int keyboardHeight = screenHeight - r.bottom;
        return keyboardHeight;
    }
}
