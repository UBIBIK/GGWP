package ggwp.server.guardiango.repository;

import ggwp.server.guardiango.entity.EmergencyBell;

import java.util.List;

public interface EmergencyBellRepository {
    List<EmergencyBell> getEmergencyBells() throws Exception; // 모든 비상벨(위도, 경도, 주소) 데이터 가져오기
}