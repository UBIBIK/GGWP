package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.CCTV;
import ggwp.server.guardiango.service.CCTVService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CCTVServiceTest {
    @Autowired
    private CCTVService CCTVservice;

    @Test
    void getCCTVs() throws Exception {
        List<CCTV> cctvs = CCTVservice.getCCTVs();
        cctvs.forEach(cctv -> System.out.println("주소 : " + cctv.getAddress()
                + ", 위도 : " + cctv.getLongitude() + ", 경도 : " + cctv.getLatitude()));
    }
}