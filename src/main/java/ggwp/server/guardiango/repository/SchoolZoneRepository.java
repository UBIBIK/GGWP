package ggwp.server.guardiango.repository;

import ggwp.server.guardiango.entity.SchoolZone;

import java.util.List;

public interface SchoolZoneRepository {
    List<SchoolZone> getLocations() throws Exception; // 모든 스쿨존 정보(위도, 경도, 주소) 가져오기
}
