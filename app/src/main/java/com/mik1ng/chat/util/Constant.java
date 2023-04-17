package com.mik1ng.chat.util;

public class Constant {
    //IP
    public static final String RELEASE_IP = "123.249.43.238:8080";
    public static final String DEBUG_IP = "123.249.43.238:8080";
    //user
    public static int USER_ID = -1;
    public static int CHAT_USER_ID = -1;
    public static String MY_AVATAR = "";
    public static String USER_NAME = "";

    //sharepreference标记
    public static final String SP_NAME = "SP_NAME";
    public static final String SP_TOKEN = "SP_TOKEN";
    public static final String SP_USERID = "SP_USERID";
    public static final String SP_AVATAR = "SP_AVATAR";
    public static final String SP_USERNAME = "SP_USERNAME";

    //adapter局部刷新标记
    public static final int ADAPTER_PAYLOADS_0 = 0;
    public static final int ADAPTER_PAYLOADS_1 = 1;
    public static final int ADAPTER_PAYLOADS_2 = 2;

    //bundle容器标记
    public static final String BUNDLE_ID = "BUNDLE_ID";
    public static final String BUNDLE_NAME = "NAME";
    public static final String BUNDLE_AVATAR = "AVATAR";
    public static final String BUNDLE_LAT = "BUNDLE_LAT";
    public static final String BUNDLE_LOG = "BUNDLE_LOG";
    public static final String BUNDLE_MESSAGE_COUNT = "BUNDLE_MESSAGE_COUNT";
    public static final String BUNDLE_FRIEND_STATE = "BUNDLE_FRIEND_STATE";

    public static final int CHAT_RECORD_MODE_RECEIVE = 0;       //聊天记录模式：接收
    public static final int CHAT_RECORD_MODE_SEND = 1;          //聊天记录模式：发送
    public static final int CHAT_RECORD_MODE_TIME = 2;          //聊天记录模式：时间
    public static final int CHAT_RECORD_TYPE_TEXT = 0;          //聊天记录类型：文本
    public static final int CHAT_RECORD_TYPE_IMAGE = 1;         //聊天记录类型：图片
    public static final int CHAT_RECORD_TYPE_VOICE = 2;         //聊天记录类型：语音
    public static final int CHAT_RECORD_TYPE_LOCATION = 3;      //聊天记录类型：定位
    public static final int MESSAGE_NEW_FRIEND = 1;             //消息类型：添加好友
    public static final int MESSAGE_CHAT = 0;                   //消息类型：聊天
    public static final float CHAT_RECORD_MAX_WIDTH = 150f;     //聊天记录最大宽度 单位:dp
    public static final float CHAT_RECORD_MIN_WIDTH = 25f;      //聊天记录最小宽度 单位:dp

    public static final int FRIEND_STATE_UNKNOW = -1;           //未知
    public static final int FRIEND_STATE_NOT_ADD = 0;           //未添加
    public static final int FRIEND_STATE_ADD = 1;               //已添加
    public static final int FRIEND_STATE_ALREADY = 2;           //准备添加

    //文件类型
    public static final String FILE_TYPE_PNG = ".png";
    public static final String FILE_TYPE_MP4 = ".mp4";
}
