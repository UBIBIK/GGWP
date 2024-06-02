package com.example.guardiango.entity;


public class ConvenienceStore {
    private double latitude; // 위도 추가 예정
    private double longitude; // 경도 추가 예정
    private String address; // 도로명 주소

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
