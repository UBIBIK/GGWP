package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.LocationData;
import ggwp.server.guardiango.entity.Report;
import ggwp.server.guardiango.entity.UserInfo;
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
        Report report = new Report(33.132, 123.341, "1234", "신호등 고장");
        userReportRepository.addReport(report, testUserInfo);
    }

    @Test
    public void deleteReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        Report report = new Report(34.793692, 126.3679059, "123");
        userReportRepository.deleteReport(report, testUserInfo);
    }

    @Test
    public void deleteUserReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        userReportRepository.deleteUserReport(testUserInfo);
    }

    @Test
    public void getReportsLocationByGroupKeyTest() throws Exception {
        List<Report> reports =  userReportRepository.getReportsLocationByGroupKey(TEST_GROUP_KEY);
        for(Report report : reports) {
            System.out.println("신고 위도 : " + report.getLatitude() + ", 경도 : " + report.getLongitude());
        }
    }

    @Test
    public void getReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        LocationData findLocationData = new LocationData();
        findLocationData.setLatitude(34.232);
        findLocationData.setLongitude(136.3123);
        Report report = userReportRepository.getReportByLocation(findLocationData, testUserInfo);
        System.out.println("신고자 이름 : " + report.getReporterName()
                + ", 신고 이미지 : " + report.getImage() + ", 위도 : " + report.getLatitude()
                + ", 경도 : " + report.getLongitude() + ", 신고 시간 : " + report.getTime()
                + ", 신고 설명 : " + report.getDescription());
    }
}