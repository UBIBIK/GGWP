package ggwp.server.guardiango.RepositoryTest;

import ggwp.server.guardiango.entity.Element;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.repository.ElementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElementRepositoryTest {
    private static final String TEST_GROUP_KEY = "8et62mcnqqp5qk66";

    @Autowired
    private ElementRepository elementService;

    @Test
    public void getElementTest() throws Exception {
        UserInfo testuser = new UserInfo();
        testuser.setGroupKey(TEST_GROUP_KEY);
        Element element = elementService.getElement(testuser);
        System.out.println("cctv : " + element.getCctvs().getFirst().getLatitude()
        + ", schoolZones : " + element.getSchoolZones().getFirst().getLatitude()
        + ", crime : " + element.getCrimes().getFirst().get경도()
        + ", emergencyBells : " + element.getEmergencyBells().getFirst().getLatitude()
        + ", convenienceStores : " + element.getConvenienceStores().getFirst().getLatitude()
        + ", repoet Latitude: " + element.getReports().getFirst().getLatitude()
        + ", repoet Longitude: " + element.getReports().getFirst().getLongitude());
    }
}