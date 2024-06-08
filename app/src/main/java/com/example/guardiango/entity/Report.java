package com.example.guardiango.entity;

import java.util.Date;

public class Report {
    private String reporterName; // 신고자 이름
    private double latitude; // 위도
    private double longitude; // 경도
    private String UUID; // 파이어베이스 스토리지 이미지
    private Date time; // 신고 시간
    private String content; // 신고 설명

    public Report() {}

    public Report(double latitude, double longitude, String UUID) { // UserReportTest를 위한 생성자
        this.latitude = latitude;
        this.longitude = longitude;
        this.UUID = UUID;
        // 현재 시간을 타임스탬프로 추가
        this.time = new Date();
    }

    public Report(double latitude, double longitude, String UUID, String description) { // UserReportTest를 위한 생성자
        this.latitude = latitude;
        this.longitude = longitude;
        this.UUID = UUID;
        this.time = new Date();
        this.content = description;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
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

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}