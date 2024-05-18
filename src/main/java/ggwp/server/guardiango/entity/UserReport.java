package ggwp.server.guardiango.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UserReport {
    private String reportName;
    private String groupKey;
    private ArrayList<Map<String, Object>> report = new ArrayList<>();

    public UserReport() {

    }
    public UserReport(String reportName, String groupKey) {
        this.reportName = reportName + " 그룹의 신고 목록";
        this.groupKey = groupKey;
    }

    public UserReport(String reportName, String groupKey, Report report) {
        this.reportName = reportName + " 그룹의 신고 목록";
        this.groupKey = groupKey;
        Map<String, Object> newreport = new HashMap<>();
        newreport.put("reporterName", reportName);
        newreport.put("latitude", report.getLatitude());
        newreport.put("longitude", report.getLongitude());
        newreport.put("image", null);
        // 현재 시간을 타임스탬프로 추가
        Date now = new Date();
        newreport.put("timestamp", now);
        this.report.add(newreport);
    }
}