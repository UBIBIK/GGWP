package ggwp.server.guardiango.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserReport {
    private String reportName;
    private String groupKey;
    private List<Report> report = new ArrayList<>();

    public UserReport() {} // UserReportService를 위한 기본 생성자
    
    public UserReport(String reportName, String groupKey) {
        this.reportName = reportName + " 그룹의 신고 목록";
        this.groupKey = groupKey;
    }
}