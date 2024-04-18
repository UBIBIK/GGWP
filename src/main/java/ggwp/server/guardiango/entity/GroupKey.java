package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupKey {
    private String groupKey;

    public GroupKey(){}

    public GroupKey(String groupKey) {
        this.groupKey = groupKey;
    }
}