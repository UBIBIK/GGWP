package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SchoolZone {
    private double latitude; // 위도
    private double longitude; // 경도
    private String address; // 도로명 주소
}
