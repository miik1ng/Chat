package com.mik1ng.chat.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ChatRecordEntity {
    @Id(autoincrement = true)
    private Long id;

    private int myID;
    private int userID;
    private String userName;

    private int recordMode;             //聊天记录模式
    private int recordType;             //聊天记录类型
    private String text;                //聊天记录文本内容
    private String image;               //聊天记录图片
    private float imgWidth;             //图片宽度
    private float imgHeight;            //图片高度
    private int second;                 //语音时长 单位：秒
    private String mediaPath;           //录音路径
    private String locationName;        //定位的地点名称
    private String locationAddress;     //定位的地址
    private double longitude;             //定位经度
    private double latitude;            //定位纬度
    private long timestamp;             //记录时间戳
    @Generated(hash = 1224313612)
    public ChatRecordEntity(Long id, int myID, int userID, String userName,
            int recordMode, int recordType, String text, String image,
            float imgWidth, float imgHeight, int second, String mediaPath,
            String locationName, String locationAddress, double longitude,
            double latitude, long timestamp) {
        this.id = id;
        this.myID = myID;
        this.userID = userID;
        this.userName = userName;
        this.recordMode = recordMode;
        this.recordType = recordType;
        this.text = text;
        this.image = image;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.second = second;
        this.mediaPath = mediaPath;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }
    @Generated(hash = 88641200)
    public ChatRecordEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getMyID() {
        return this.myID;
    }
    public void setMyID(int myID) {
        this.myID = myID;
    }
    public int getUserID() {
        return this.userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getRecordMode() {
        return this.recordMode;
    }
    public void setRecordMode(int recordMode) {
        this.recordMode = recordMode;
    }
    public int getRecordType() {
        return this.recordType;
    }
    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public float getImgWidth() {
        return this.imgWidth;
    }
    public void setImgWidth(float imgWidth) {
        this.imgWidth = imgWidth;
    }
    public float getImgHeight() {
        return this.imgHeight;
    }
    public void setImgHeight(float imgHeight) {
        this.imgHeight = imgHeight;
    }
    public int getSecond() {
        return this.second;
    }
    public void setSecond(int second) {
        this.second = second;
    }
    public String getMediaPath() {
        return this.mediaPath;
    }
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
    public String getLocationName() {
        return this.locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public String getLocationAddress() {
        return this.locationAddress;
    }
    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
