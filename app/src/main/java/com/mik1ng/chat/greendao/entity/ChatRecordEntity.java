package com.mik1ng.chat.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ChatRecordEntity {
    @Id(autoincrement = true)
    private Long id;

    private String username;
    private String record;
    private String password;
    @Generated(hash = 1889943079)
    public ChatRecordEntity(Long id, String username, String record,
            String password) {
        this.id = id;
        this.username = username;
        this.record = record;
        this.password = password;
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
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRecord() {
        return this.record;
    }
    public void setRecord(String record) {
        this.record = record;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
