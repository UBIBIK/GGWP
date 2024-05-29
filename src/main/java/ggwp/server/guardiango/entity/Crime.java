package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Crime {
    private double 위도; // 위도
    private double 경도; // 경도
    private String 거주지; // 거주지
}
