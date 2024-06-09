package com.example.guardiango.entity;

public class PostData {
    private UserInfo userInfo;
    private String postContent;
    private String UUID; // 이미지 URL
    private double latitude;
    private double longitude;

    public PostData() {
        // 기본 생성자 필요
    }

    public PostData(UserInfo userInfo, String postContent, String UUID) {
        this.userInfo = userInfo;
        this.postContent = postContent;
        this.UUID = UUID;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getPostContent() {
        return this.postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
