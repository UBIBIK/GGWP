package ggwp.server.guardiango.repository;

import ggwp.server.guardiango.entity.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserReportRepository {
    String insertUserReport(UserInfo user) throws ExecutionException, InterruptedException; // 사용자 신고 목록 추가

    UserReport addReport(PostData postData) throws Exception; // 사용자 신고 정보 추가

    void updateUserReport(UserReport userReport) throws Exception; // 사용자 신고 목록 수정

    UserReport deleteReport(PostData postData) throws Exception; // 사용자 신고 정보 삭제

    boolean deleteUserReport(UserInfo user) throws Exception; // 사용자 신고 목록 수정

    List<Report> getUserReport(UserInfo user) throws Exception; // 그룹별 사용자 신고 조회

    Report getReportByLocation(PostData postData) throws Exception; // 위치 정보로 단일 사용자 신고 정보 조회
}