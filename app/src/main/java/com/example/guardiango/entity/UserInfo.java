package com.example.guardiango.entity;

public class UserInfo {
    private String status;
    private String groupKey;
    private String userId;
    private String phoneNumber;
    private String userName;
    //TODO: 유저정보 저장 필드 추가하기

    public UserInfo(String groupKey, String userId, String phoneNumber, String userName) {
        this.groupKey = groupKey;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}