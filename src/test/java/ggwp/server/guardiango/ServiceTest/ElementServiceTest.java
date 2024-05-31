package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.Element;
import ggwp.server.guardiango.repository.ElementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElementServiceTest {
    @Autowired
    private ElementRepository elementService;

    @Test
    public void getElementTest() throws Exception {
        Element element = elementService.getElement();
        System.out.println("cctv : " + element.getCctvs().getFirst().getLatitude()
        + ", schoolZones : " + element.getSchoolZones().getFirst().getLatitude()
        + ", crime : " + element.getCrimes().getFirst().get경도()
        + ", emergencyBells : " + element.getEmergencyBells().getFirst().getLatitude()
        + ", convenienceStores : " + element.getConvenienceStores().getFirst().getLatitude());
    }
}