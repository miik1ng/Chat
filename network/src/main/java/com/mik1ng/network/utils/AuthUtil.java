package com.mik1ng.network.utils;

import android.content.Context;
import com.mik1ng.network.bean.AuthBean;

public class AuthUtil {
    public static boolean saveAuthBean(Context context, AuthBean authBean) {
        return SharePreferenceUtils.saveObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN, authBean);
    }

    public static AuthBean getAuthBean(Context context) {
        return SharePreferenceUtils.getObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN);
    }

    public static void clearAuthBean(Context context) {
        SharePreferenceUtils.clearShareprefrence(context);
    }
}
