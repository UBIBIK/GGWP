package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GroupServiceTest {
    private static final String TEST_GROUP_NAME = "test1";
    private static final String TEST_GROUP_MASTER_EMAIL = "test1@example.com";
    private static final String TEST_GROUP_MASTER_NAME = "test1";
    private static final String TEST_GROUP_MEMBER_EMAIL = "test2";
    private static final String TEST_GROUP_MEMBER_NAME = "test2";
    private static final String TEST_GROUP_KEY = "09bokfesd81vcc60";

    @Autowired
    private GroupService groupService;
    
    // 그룹 정보 추가 테스트
    @Test
    public void insertGroupTest() throws Exception {
        Group group = new Group(TEST_GROUP_NAME, TEST_GROUP_KEY);
        UserInfo master = new UserInfo();
        master.setUserEmail(TEST_GROUP_MASTER_EMAIL);
        master.setUserName(TEST_GROUP_MASTER_NAME);
        groupService.insertGroup(group, master);
    }

    // 그룹 멤버 추가 테스트
    @Test
    public void addGroupMemberTest() throws Exception {
        UserInfo member = new UserInfo();
        member.setUserName(TEST_GROUP_MEMBER_NAME);
        Group group = groupService.addGroupMember(TEST_GROUP_KEY, member);
        System.out.println(group.getGroupKey());
    }

    // 그룹 멤버 삭제 테스트
    @Test
    public void deleteGroupMemberTest() throws Exception {
        groupService.deleteGroupMember(TEST_GROUP_MEMBER_NAME);
    }

    // 그룹 정보 수정 테스트
    @Test
    public void updateGroup() throws Exception {
        Group updateGroup = new Group(TEST_GROUP_NAME, "k4p8usof9v312lht");
        groupService.updateGroup(updateGroup);
    }

    // 그룹 정보 삭제 테스트
    @Test
    public void deleteGroupTest() throws Exception {
        UserInfo master = new UserInfo();
        master.setUserEmail(TEST_GROUP_MASTER_EMAIL);
        master.setUserName(TEST_GROUP_MASTER_NAME);
        master.setGroupKey(TEST_GROUP_KEY);
        groupService.deleteGroup(master);
    }

    // 그룹 키로 그룹 정보 조회 테스트
    @Test
    public void getGroupByGroupCodeTest() throws Exception {
        Group group = groupService.getGroupByGroupCode(TEST_GROUP_KEY);
        System.out.println(group.getGroupName());
        System.out.println(group.getGroupKey());
    }

    // 그룹 코드로 그룹원 조회 테스트
    @Test
    void getGroupMemberByGroupKeyTest() throws Exception {
        List<String> groupMemberNames = groupService.getGroupMemberByGroupCode(TEST_GROUP_KEY);

        for (String name : groupMemberNames) {
            System.out.println(name);
        }
    }

    // 그룹 코드로 그룹 이름 조회 테스트
    @Test
    void getGroupNameByGroupKeyTest() throws Exception {
        String groupName = groupService.getGroupNameByGroupCode(TEST_GROUP_KEY);
        System.out.println(groupName);
    }

    // 그룹 멤버의 본인 위치 업데이트 테스트
    @Test
    void updateLocationInfoTest() throws Exception {
        UserInfo member = new UserInfo();
        member.setUserEmail(TEST_GROUP_MEMBER_EMAIL);
        member.setUserName(TEST_GROUP_MEMBER_NAME);
        member.setGroupKey(TEST_GROUP_KEY);
        member.getLocationInfo().put("latitude", 321.123123);
        member.getLocationInfo().put("longitude", 123.123123);

        Group group = groupService.updateLocationInfo(member);
        System.out.println(group.getGroupKey());
    }
}