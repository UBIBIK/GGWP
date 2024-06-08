package ggwp.server.guardiango.RepositoryTest;

import ggwp.server.guardiango.entity.*;
import ggwp.server.guardiango.repository.UserReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
public class UserReportRepositoryTest {
    private static final String TEST_GROUP_MEMBER_NAME = "test2";
    private static final String TEST_GROUP_KEY = "8et62mcnqqp5qk66";

    @Autowired
    private UserReportRepository userReportRepository;

    @Test
    public void insertUserReportTest() throws ExecutionException, InterruptedException {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        userReportRepository.insertUserReport(testUserInfo);
    }

    @Test
    public void addReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        PostData postData = new PostData(testUserInfo, "신호등 고장", "example.com:8080/uploads/example.txt", 34.793692, 126.3679059);
        userReportRepository.addReport(postData);
    }

    @Test
    public void deleteReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        PostData postData = new PostData(testUserInfo, "신호등 고장", "example.com:8080/uploads/example.txt", 34.793692, 126.3679059);

        userReportRepository.deleteReport(postData);
    }

    @Test
    public void deleteUserReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        userReportRepository.deleteUserReport(testUserInfo);
    }

    @Test
    public void getUserReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        List<Report> findreport = userReportRepository.getUserReport(testUserInfo);
        for (Report report : findreport) {
            double lat = report.getLatitude();
            double lon = report.getLongitude();
            System.out.println(lat + "," + lon);
        }
    }

    @Test
    public void getReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        PostData postData = new PostData(testUserInfo, "신호등 고장", "example.com:8080/uploads/example.txt", 34.793692, 126.3679059);
        Report report = userReportRepository.getReportByLocation(postData);

        System.out.println("신고자 이름 : " + report.getReporterName()
                + ", 신고 이미지 : " + report.getUuid() + ", 위도 : " + report.getLatitude()
                + ", 경도 : " + report.getLongitude() + ", 신고 시간 : " + report.getTime()
                + ", 신고 설명 : " + report.getContent());
    }
}