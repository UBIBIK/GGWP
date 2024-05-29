package ggwp.server.guardiango.service;

import ggwp.server.guardiango.entity.ConvenienceStore;

import java.util.List;

public interface ConvenienceStoreService {
    List<ConvenienceStore> getConvenienceStores() throws Exception; // 모든 편의점 정보(위도, 경도, 주소) 가져오기
}
