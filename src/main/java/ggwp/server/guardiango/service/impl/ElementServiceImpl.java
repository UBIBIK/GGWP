package ggwp.server.guardiango.service.impl;

import ggwp.server.guardiango.entity.Element;
import ggwp.server.guardiango.service.CCTVService;
import ggwp.server.guardiango.service.ElementSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElementServiceImpl implements ElementSerivce {
    private final CCTVService cctvService;

    @Autowired
    public ElementServiceImpl(CCTVService cctvService) {
        this.cctvService = cctvService;
    }

    @Override
    public Element getElement() throws Exception {
        Element element = new Element();
        element.setCctvs(cctvService.getCCTVs()); // CCTV 정보 가져오기
        return element;
    }
}