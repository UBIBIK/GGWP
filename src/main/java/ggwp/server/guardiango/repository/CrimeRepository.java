package ggwp.server.guardiango.repository;

import ggwp.server.guardiango.entity.Crime;

import java.util.List;

public interface CrimeRepository {
    List<Crime> getCrimes() throws Exception; // 모든 성범죄자 정보(위도, 경도, 거주지) 가져오기
}
