package com.example.guardiango.entity;

public class Crime {
    private double 위도; // 위도
    private double 경도; // 경도
    private String 거주지; // 거주지

    public double get위도() {
        return 위도;
    }

    public void set위도(double 위도) {
        this.위도 = 위도;
    }

    public double get경도() {
        return 경도;
    }

    public void set경도(double 경도) {
        this.경도 = 경도;
    }

    public String get거주지() {
        return 거주지;
    }

    public void set거주지(String 거주지) {
        this.거주지 = 거주지;
    }
}

