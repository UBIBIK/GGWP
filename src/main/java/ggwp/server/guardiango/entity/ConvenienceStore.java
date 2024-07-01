package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvenienceStore {
    private double latitude; // 위도
    private double longitude; // 경도
    private String address; // 도로명 주소
}
