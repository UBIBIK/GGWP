package ggwp.server.guardiango.service;

import ggwp.server.guardiango.entity.LocationData;
import ggwp.server.guardiango.entity.Report;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.entity.UserReport;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserReportService {
    String insertUserReport(UserInfo user) throws ExecutionException, InterruptedException; // 사용자 신고 목록 추가

    UserReport addReport(Report report, UserInfo user) throws Exception; // 사용자 신고 정보 추가

    void updateUserReport(UserReport userReport) throws Exception; // 사용자 신고 목록 수정

    UserReport deleteReport(Report report, UserInfo user) throws Exception; // 사용자 신고 정보 삭제

    boolean deleteUserReport(UserInfo user) throws Exception; // 사용자 신고 목록 수정

    List<Report> getReportsLocationByGroupKey(String groupKey) throws Exception; //

    Report getReportByLocation(LocationData reportLocation, UserInfo user) throws Exception; // 위치 정보로 사용자 신고 정보 조회
}