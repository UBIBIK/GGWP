package com.example.guardiango.entity;

import java.util.ArrayList;
import java.util.List;

public class Element {
    List<CCTV> cctvs = new ArrayList<>();
    List<SchoolZone> schoolZones = new ArrayList<>();
    List<Crime> crimes = new ArrayList<>();
    List<EmergencyBell> emergencyBells = new ArrayList<>();
    List<ConvenienceStore> convenienceStores = new ArrayList<>();

    public List<CCTV> getCctvs() {
        return cctvs;
    }

    public void setCctvs(List<CCTV> cctvs) {
        this.cctvs = cctvs;
    }

    public List<SchoolZone> getSchoolZones() {
        return schoolZones;
    }

    public void setSchoolZones(List<SchoolZone> schoolZones) {
        this.schoolZones = schoolZones;
    }

    public List<Crime> getCrimes() {
        return crimes;
    }

    public void setCrimes(List<Crime> crimes) {
        this.crimes = crimes;
    }

    public List<EmergencyBell> getEmergencyBells() {
        return emergencyBells;
    }

    public void setEmergencyBells(List<EmergencyBell> emergencyBells) {
        this.emergencyBells = emergencyBells;
    }

    public List<ConvenienceStore> getConvenienceStores() {
        return convenienceStores;
    }

    public void setConvenienceStores(List<ConvenienceStore> convenienceStores) {
        this.convenienceStores = convenienceStores;
    }
}
