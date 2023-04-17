package com.mik1ng.chat.entity;

public class NewFriendEntity {
    public int userID;
    public String avatar;
    public String name;
    public String remark;
    public int state;

    public NewFriendEntity(int userID, String avatar, String name, String remark, int state) {
        this.userID = userID;
        this.avatar = avatar;
        this.name = name;
        this.remark = remark;
        this.state = state;
    }
}
