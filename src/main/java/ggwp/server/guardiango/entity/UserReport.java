package ggwp.server.guardiango.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class UserReport {
    private String reportName;
    private String groupKey;
    private ArrayList<Report> report = new ArrayList<>();

    public UserReport(String reportName, String groupKey) {
        this.reportName = reportName + " 그룹의 신고 목록";
        this.groupKey = groupKey;
    }
}