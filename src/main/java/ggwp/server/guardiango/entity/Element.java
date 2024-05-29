package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Element {
    List<CCTV> cctvs = new ArrayList<>();
    List<SchoolZone> schoolZones = new ArrayList<>();
    List<Crime> crimes = new ArrayList<>();
    List<EmergencyBell> emergencyBells = new ArrayList<>();
    List<ConvenienceStore> convenienceStores = new ArrayList<>();
}
