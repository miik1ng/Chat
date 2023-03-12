package com.mik1ng.network.bean;

import java.io.Serializable;

public class AuthBean implements Serializable {
    private String token;
    private String token_ba;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_ba() {
        return token_ba;
    }

    public void setToken_ba(String token_ba) {
        this.token_ba = token_ba;
    }
}
