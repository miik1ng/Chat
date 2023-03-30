package com.mik1ng.chat.entity;

public class ChatRecordEntity {
    private int recordMode;             //聊天记录模式
    private int recordType;             //聊天记录类型
    private String avatar;              //头像
    private String text;                //聊天记录文本内容
    private String image;               //聊天记录图片
    private int second;                 //语音时长 单位：秒
    private String locationName;        //定位的地点名称
    private String locationAddress;     //定位的地址
    private long longitude;             //定位经度
    private long latitude;              //定位纬度
    private long timestamp;             //记录时间戳

    public int getRecordMode() {
        return recordMode;
    }

    public void setRecordMode(int recordMode) {
        this.recordMode = recordMode;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
