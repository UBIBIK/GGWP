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

    public Group() {} // GroupService를 위한 기본 생성자

    public Group(String groupMemberName, String groupKey) { // GroupServiceTest를 위한 생성자
        this.groupName = groupMemberName + "의 그룹";
        this.groupKey = groupKey;
        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("groupMemberName", groupMemberName);
        memberInfo.put("groupRole", "보호자");
        memberInfo.put("latitude", null);
        memberInfo.put("longitude", null);
        this.groupMember.add(memberInfo);
    }
}