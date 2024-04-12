package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Groupcode {
    private String groupCode;

    public Groupcode(){}

    public Groupcode(String groupCode) {
        this.groupCode = groupCode;
    }
}