package com.mik1ng.chat.entity;

public class LoginEntity {

    /**
     * code : 200
     * data : {"user":{"id":9,"username":"string","password":"string","sex":null,"avatar":null,"nickname":"string","createTime":"2023-03-12 09:05","updateTime":null},"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5IiwiZXhwIjoxNjc4Njk4NDc4fQ.KFr7Jrrc40RTkpgA5LYkraHUThAejRkeRrictEnpu9o"}
     * message : 操作成功
     */

    private int code;
    /**
     * user : {"id":9,"username":"string","password":"string","sex":null,"avatar":null,"nickname":"string","createTime":"2023-03-12 09:05","updateTime":null}
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5IiwiZXhwIjoxNjc4Njk4NDc4fQ.KFr7Jrrc40RTkpgA5LYkraHUThAejRkeRrictEnpu9o
     */

    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * id : 9
         * username : string
         * password : string
         * sex : null
         * avatar : null
         * nickname : string
         * createTime : 2023-03-12 09:05
         * updateTime : null
         */

        private UserBean user;
        private String token;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class UserBean {
            private int id;
            private String username;
            private String password;
            private String sex;
            private String avatar;
            private String nickname;
            private String createTime;
            private String updateTime;

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

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
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

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
