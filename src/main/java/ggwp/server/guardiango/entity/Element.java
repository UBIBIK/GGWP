package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Element {
    private List<CCTV> cctvs = new ArrayList<>();
    private List<SchoolZone> schoolZones = new ArrayList<>();
    private List<Crime> crimes = new ArrayList<>();
    private List<EmergencyBell> emergencyBells = new ArrayList<>();
    private List<ConvenienceStore> convenienceStores = new ArrayList<>();
    private List<Report> reports = new ArrayList<>();

    public Element() {}
}