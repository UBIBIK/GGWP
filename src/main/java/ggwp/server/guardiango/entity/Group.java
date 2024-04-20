package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Group {
    private String groupName;
    private String groupKey;
    private String groupMaster;
    private ArrayList<Map<String, Object>> groupMember = new ArrayList<>();
    private Map<String, Object> memberInfo = new HashMap<>();

    public Group() {}

    public Group(String groupMemberName, String groupKey) {
        this.groupName = groupMemberName + "의 그룹";
        this.groupKey = groupKey;
        memberInfo.put("groupMemberName", groupMemberName);
        memberInfo.put("groupRole", "보호자");
        this.groupMember.add(memberInfo);
    }
}