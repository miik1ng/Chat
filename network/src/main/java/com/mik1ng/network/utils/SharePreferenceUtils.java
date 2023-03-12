package com.mik1ng.network.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtils {
    private static SharedPreferences mSharedPreferences;
    public static final String SP_NAME = "sp_name";
    public static final String SP_DOMAIN = "sp_domain";
    /**视频动态暂存标识*/
    public static final String VIDEO_DYNAMIC = "video_dynamic";
    public static final String SIGN_POSITION = "sign_position";
    public static final String CUR_CDN_DOMAIN = "cur_cdn_domain";
    public static final String SP_WORLD_MAP_DATA = "sp_world_map_data";

    /**
     * 清除 shareprefrence
     */
    public static void clearShareprefrence(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().clear().apply();
    }

    /**
     * 将对象储存到 sharepreference
     *
     * @param key
     * @param device
     * @param <T>
     */
    public static <T> boolean saveObject(Context context, String key, T device) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        String oAuth_Base64 = ConvertUtils.object2Base64Str(device);
        if (oAuth_Base64 == null) {
            return false;
        }
        mSharedPreferences.edit().putString(key, oAuth_Base64).apply();
        return true;
    }

    /**
     * 将对象从 shareprerence 中取出来
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObject(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        String productBase64 = mSharedPreferences.getString(key, null);
        return ConvertUtils.base64Str2Object(productBase64);
    }

}
