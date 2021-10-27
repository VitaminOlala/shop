package com.shopme.common.entity;

import java.util.Date;

public class LoginResponse {
    private boolean success;
    private String jwtToken;
    private String message;
    private long ts;

    public LoginResponse(String jwtToken, String message, boolean success) {
        this.success = success;
        this.jwtToken = jwtToken;
        this.message = message;
        this.ts = new Date().getTime();
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
