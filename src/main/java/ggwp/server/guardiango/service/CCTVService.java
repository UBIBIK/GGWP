package ggwp.server.guardiango.service;

import ggwp.server.guardiango.entity.CCTV;

import java.util.List;

public interface CCTVService {
    List<CCTV> getCCTVs() throws Exception; // 모든 CCTV 정보(주소, 위도, 경도) 가져오기
}