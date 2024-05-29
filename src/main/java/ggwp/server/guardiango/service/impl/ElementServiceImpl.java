package ggwp.server.guardiango.service.impl;

import ggwp.server.guardiango.entity.Element;
import ggwp.server.guardiango.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElementServiceImpl implements ElementSerivce {
    private final CCTVService cctvService;
    private final SchoolZoneService schoolZoneService;
    private final EmergencyBellService emergencyBellService;
    private final CrimeService crimeService;
    private final ConvenienceStoreService convenienceStoreService;

    @Autowired
    public ElementServiceImpl(CCTVService cctvService, SchoolZoneService schoolZoneService, EmergencyBellService emergencyBellService, CrimeService criticeService, ConvenienceStoreService convenienceStoreService) {
        this.cctvService = cctvService;
        this.schoolZoneService = schoolZoneService;
        this.emergencyBellService = emergencyBellService;
        this.crimeService = criticeService;
        this.convenienceStoreService = convenienceStoreService;
    }

    @Override
    public Element getElement() throws Exception {
        Element element = new Element();
        element.setCctvs(cctvService.getCCTVs()); // CCTV 정보 가져오기
        element.setSchoolZones(schoolZoneService.getLocations()); // 스쿨존 정보 가져오기
        element.setEmergencyBells(emergencyBellService.getEmergencyBells()); // 비상벨 정보 가져오기
        element.setCrimes(crimeService.getCrimes()); // 성범죄자 정보 가져오기
        element.setConvenienceStores(convenienceStoreService.getConvenienceStores());

        return element;
    }
}