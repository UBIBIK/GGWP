package com.example.guardiango.entity;

import java.util.ArrayList;
import java.util.List;

public class UserReport {
    private String reportName;
    private String groupKey;
    private List<Report> report = new ArrayList<>();

    public UserReport() {} // UserReportService를 위한 기본 생성자
    
    public UserReport(String reportName, String groupKey) {
        this.reportName = reportName + " 그룹의 신고 목록";
        this.groupKey = groupKey;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public List<Report> getReport() {
        return report;
    }

    public void setReport(List<Report> report) {
        this.report = report;
    }
}