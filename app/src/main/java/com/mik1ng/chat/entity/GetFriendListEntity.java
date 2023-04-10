package com.mik1ng.chat.entity;

import java.util.List;

public class GetFriendListEntity {

    /**
     * code : 200
     * data : [{"id":2,"username":"zhangsan","sex":null,"avatar":null,"nickname":"zhangsan"},{"id":12,"username":"17333781851","sex":null,"avatar":null,"nickname":"17333781851"}]
     * message : null
     */

    private int code;
    private String message;
    /**
     * id : 2
     * username : zhangsan
     * sex : null
     * avatar : null
     * nickname : zhangsan
     */

    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String username;
        private String sex;
        private String avatar;
        private String nickname;
        private String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
