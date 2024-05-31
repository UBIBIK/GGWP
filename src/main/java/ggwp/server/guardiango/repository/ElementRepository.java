package ggwp.server.guardiango.repository;

import ggwp.server.guardiango.entity.Element;

public interface ElementRepository {
    Element getElement() throws Exception; // 모든 요소 정보 조회
}
