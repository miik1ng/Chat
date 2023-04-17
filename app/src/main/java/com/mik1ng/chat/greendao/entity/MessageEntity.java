package com.mik1ng.chat.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MessageEntity {
    @Id(autoincrement = true)
    private Long id;

    private int myID;
    private int userID;
    private String avatar;
    private String name;
    private String date;
    private String content;
    private int count;
    private long timestamp;
    @Generated(hash = 1692774981)
    public MessageEntity(Long id, int myID, int userID, String avatar, String name,
            String date, String content, int count, long timestamp) {
        this.id = id;
        this.myID = myID;
        this.userID = userID;
        this.avatar = avatar;
        this.name = name;
        this.date = date;
        this.content = content;
        this.count = count;
        this.timestamp = timestamp;
    }
    @Generated(hash = 1797882234)
    public MessageEntity() {
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
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
