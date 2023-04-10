package com.mik1ng.chat.entity;

public class FriendEntity {
    private int id;
    private String remark;
    private String nickName;
    private String avatar;
    private String firstChar;
    private int type;
    private int position;

    public FriendEntity() {
    }

    public FriendEntity(String firstChar, int type) {
        this.firstChar = firstChar;
        this.type = type;
    }

    public FriendEntity(int id, String remark, String nickName, String avatar) {
        this.id = id;
        this.remark = remark;
        this.nickName = nickName;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
