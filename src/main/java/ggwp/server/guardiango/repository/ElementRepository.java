package ggwp.server.guardiango.repository;

import ggwp.server.guardiango.entity.Element;
import ggwp.server.guardiango.entity.UserInfo;

public interface ElementRepository {
    Element getElement(UserInfo user) throws Exception; // 모든 요소 정보 조회
}
