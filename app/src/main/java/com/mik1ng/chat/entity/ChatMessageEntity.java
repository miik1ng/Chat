package com.mik1ng.chat.entity;

public class ChatMessageEntity {
    private int type;               //消息类型

    private int fromUser;           //消息发送人
    private String fromUserName;    //发送人昵称
    private String fromAvatar;      //发送人头像

    private String text;            //文本内容

    private String picBase64;       //图片base64编码
    private float picWidth;         //图片宽
    private float picHeight;        //图片高

    private String voiceBase64;     //语音base64编码
    private int second;             //语音时长

    private String locationName;    //定位名
    private String locationAddress; //定位地址名
    private double locationLat;     //定位经纬度
    private double locationLog;     //定位经纬度

    public ChatMessageEntity() {
    }

    public ChatMessageEntity(int type, int fromUser, String fromUserName, String fromAvatar, String text) {
        this.type = type;
        this.fromUser = fromUser;
        this.fromUserName = fromUserName;
        this.fromAvatar = fromAvatar;
        this.text = text;
    }

    public ChatMessageEntity(int type, int fromUser, String fromUserName, String fromAvatar, String picBase64, float picWidth, float picHeight) {
        this.type = type;
        this.fromUser = fromUser;
        this.fromUserName = fromUserName;
        this.fromAvatar = fromAvatar;
        this.picBase64 = picBase64;
        this.picWidth = picWidth;
        this.picHeight = picHeight;
    }

    public ChatMessageEntity(int type, int fromUser, String fromUserName, String fromAvatar, String voiceBase64, int second) {
        this.type = type;
        this.fromUser = fromUser;
        this.fromUserName = fromUserName;
        this.fromAvatar = fromAvatar;
        this.voiceBase64 = voiceBase64;
        this.second = second;
    }

    public ChatMessageEntity(int type, int fromUser, String fromUserName, String fromAvatar, String locationName, String locationAddress, double locationLat, double locationLog) {
        this.type = type;
        this.fromUser = fromUser;
        this.fromUserName = fromUserName;
        this.fromAvatar = fromAvatar;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationLat = locationLat;
        this.locationLog = locationLog;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFromUser() {
        return fromUser;
    }

    public void setFromUser(int fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicBase64() {
        return picBase64;
    }

    public void setPicBase64(String picBase64) {
        this.picBase64 = picBase64;
    }

    public float getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(float picWidth) {
        this.picWidth = picWidth;
    }

    public float getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(float picHeight) {
        this.picHeight = picHeight;
    }

    public String getVoiceBase64() {
        return voiceBase64;
    }

    public void setVoiceBase64(String voiceBase64) {
        this.voiceBase64 = voiceBase64;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLog() {
        return locationLog;
    }

    public void setLocationLog(double locationLog) {
        this.locationLog = locationLog;
    }
}
