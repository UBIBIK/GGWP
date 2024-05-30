package com.example.guardiango.entity;

import java.util.ArrayList;
import java.util.List;

public class Element {
    private List<CCTV> cctvs = new ArrayList<>();

    public List<CCTV> getCctvs() {
        return cctvs;
    }

    public void setCctvs(List<CCTV> cctvs) {
        this.cctvs = cctvs;
    }
}