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
    private String groupCode;
    private ArrayList<Map<String, Object>> groupMember = new ArrayList<>();

    public Group() {}

    public Group(String groupMemberName, String groupCode) {
        this.groupName = groupMemberName + "의 그룹";
        this.groupCode = groupCode;
        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("groupMemberName", groupMemberName);
        memberInfo.put("groupRole", "보호자");
        this.groupMember.add(memberInfo);
    }
}