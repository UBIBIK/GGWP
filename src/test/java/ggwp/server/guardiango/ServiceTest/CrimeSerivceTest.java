package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.Crime;
import ggwp.server.guardiango.service.CrimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class CrimeSerivceTest {
    @Autowired
    private CrimeService crimeService;
    @Test
    void getCrimes() throws Exception {
        List<Crime> crimes = crimeService.getCrimes();
        crimes.forEach(crime -> System.out.println("주소 : " + crime.get거주지()
                + ", 위도 : " + crime.get위도() + ", 경도 : " + crime.get경도()));
    }
}
