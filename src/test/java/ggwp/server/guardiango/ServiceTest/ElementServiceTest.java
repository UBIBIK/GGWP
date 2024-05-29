package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.Element;
import ggwp.server.guardiango.service.ElementSerivce;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElementServiceTest {
    @Autowired
    private ElementSerivce elementService;

    @Test
    public void getElementTest() throws Exception {
        Element element = elementService.getElement();
        System.out.println("cctv : " + element.getCctvs().getFirst().getAddress()
        + ", schoolZones : " + element.getSchoolZones().getFirst().getAddress()
        + ", crime : " + element.getCrimes().getFirst().get거주지()
        + ", emergencyBells : " + element.getEmergencyBells().getFirst().getAddress()
        + ", ConvenienceStores : " + element.getConvenienceStores().getFirst().getAddress());

    }
}