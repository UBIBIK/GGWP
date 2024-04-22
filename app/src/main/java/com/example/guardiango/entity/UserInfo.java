package com.example.guardiango.entity;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    private String status;
    private String groupKey;
    private String userEmail;
    private String phoneNumber;
    private String userName;

    private Map<String, Object> locationInfo = new HashMap<>();

    public UserInfo(String groupKey, String userEmail, String phoneNumber, String userName) {
        this.groupKey = groupKey;
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public Map<String, Object> getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(Map<String, Object> locationInfo) {
        this.locationInfo = locationInfo;
    }
}