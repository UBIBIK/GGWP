package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.CCTV;
import ggwp.server.guardiango.entity.Element;
import ggwp.server.guardiango.service.ElementSerivce;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ElementServiceTest {
    @Autowired
    private ElementSerivce elementService;

    @Test
    public void getElementTest() throws Exception {
        Element element = elementService.getElement();
        List<CCTV> cctvList = element.getCctvs();
        System.out.println(cctvList.getFirst().getAddress());
    }
}