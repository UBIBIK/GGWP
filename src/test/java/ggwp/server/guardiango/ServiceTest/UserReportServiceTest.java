package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.Report;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.service.UserReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

@SpringBootTest
public class UserReportServiceTest {
    private static final String TEST_GROUP_MEMBER_NAME = "test1";
    private static final String TEST_GROUP_KEY = "09bokfesd81vcc60";

    @Autowired
    private UserReportService userReportService;

    @Test
    public void insertUserReportTest() throws ExecutionException, InterruptedException {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        userReportService.insertUserReport(testUserInfo);
    }

    @Test
    public void addReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        Report report = new Report(34.793692, 126.3679059, "123");
        userReportService.addReport(report, testUserInfo);
    }

    @Test
    public void deleteReportTest() throws Exception {
        UserInfo testUserInfo = new UserInfo();
        testUserInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        testUserInfo.setGroupKey(TEST_GROUP_KEY);
        Report report = new Report(34.793692, 126.3679059, "123");
        userReportService.deleteReport(report, testUserInfo);
    }
}