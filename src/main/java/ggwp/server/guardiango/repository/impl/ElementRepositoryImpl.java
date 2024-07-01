package ggwp.server.guardiango.repository.impl;

import ggwp.server.guardiango.entity.Element;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElementRepositoryImpl implements ElementRepository {
    private final CCTVRepository cctvRepository;
    private final SchoolZoneRepository schoolZoneRepository;
    private final EmergencyBellRepository emergencyBellRepository;
    private final CrimeRepository crimeRepository;
    private final ConvenienceStoreRepository convenienceStoreRepository;
    private final UserReportRepository userReportRepository;

    @Autowired
    public ElementRepositoryImpl(CCTVRepository cctvRepository, SchoolZoneRepository schoolZoneRepository, EmergencyBellRepository emergencyBellRepository, CrimeRepository criticeService, ConvenienceStoreRepository convenienceStoreRepository, UserReportRepository userReportRepository) {
        this.cctvRepository = cctvRepository;
        this.schoolZoneRepository = schoolZoneRepository;
        this.emergencyBellRepository = emergencyBellRepository;
        this.crimeRepository = criticeService;
        this.convenienceStoreRepository = convenienceStoreRepository;
        this.userReportRepository = userReportRepository;
    }

    @Override
    public Element getElement(UserInfo user) throws Exception {
        Element element = new Element();
        element.setCctvs(cctvRepository.getCCTVs()); // CCTV 정보 가져오기
        element.setSchoolZones(schoolZoneRepository.getLocations()); // 스쿨존 정보 가져오기
        element.setEmergencyBells(emergencyBellRepository.getEmergencyBells()); // 비상벨 정보 가져오기
        element.setCrimes(crimeRepository.getCrimes()); // 성범죄자 정보 가져오기
        element.setConvenienceStores(convenienceStoreRepository.getConvenienceStores()); // 편의점 정보 가져오기
        element.setReports(userReportRepository.getUserReport(user)); // 그룹별 사용자 신고 정보 가져오기

        return element;
    }
}