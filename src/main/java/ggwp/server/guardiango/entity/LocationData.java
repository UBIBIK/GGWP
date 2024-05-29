package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationData {
    private double latitude; // 위도
    private double longitude; // 경도

    public LocationData() {}

    public LocationData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
