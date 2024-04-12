package com.example.guardiango;

public class InviteRequest {
    private String userId;

    public InviteRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
