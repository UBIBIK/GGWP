package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Report {
    private String reporterName; // 신고자 이름
    private double latitude; // 위도
    private double longitude; // 경도
    private String image; // 파이어베이스 스토리지 이미지
    private Date time; // 신고 시간
    private String description; // 신고 설명

    public Report() {}

    public Report(double latitude, double longitude, String image) { // UserReportTest를 위한 생성자
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        // 현재 시간을 타임스탬프로 추가
        this.time = new Date();
    }

    public Report(double latitude, double longitude, String image, String description) { // UserReportTest를 위한 생성자
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.time = new Date();
        this.description = description;
    }
}