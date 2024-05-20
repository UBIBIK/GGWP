package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Report {
    private double latitude; // 위도
    private double longitude; // 경도
    private String image;
    private Date time;

    public Report(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        // 현재 시간을 타임스탬프로 추가
        this.time = new Date();
    }

    public Report(double latitude, double longitude, String image) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        // 현재 시간을 타임스탬프로 추가
        this.time = new Date();
    }

    public Report() {

    }
}
