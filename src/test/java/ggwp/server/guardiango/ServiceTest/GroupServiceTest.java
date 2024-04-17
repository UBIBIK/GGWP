package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GroupServiceTest {
    private static final String TEST_GROUP_NAME = "test의 그룹";
    private static final String TEST_GROUP_MEMBER_NAME = "test2";
    private static final String TEST_GROUP_KEY = "09bokfesd81vcc60";

    @Autowired
    private GroupService groupService;
    
    // 그룹 정보 추가 테스트
    @Test
    public void insertGroupTest() throws Exception {
        Group group = new Group(TEST_GROUP_NAME, TEST_GROUP_KEY);
        groupService.insertGroup(group);
    }

    // 그룹 멤버 추가 테스트
    @Test
    public void addGroupMemberTest() throws Exception {
        groupService.addGroupMember(TEST_GROUP_KEY, TEST_GROUP_MEMBER_NAME, "보호 대상");
    }

    // 그룹 멤버 삭제 테스트
    @Test
    public void deleteGroupMemberTest() throws Exception {
        groupService.deleteGroupMember(TEST_GROUP_NAME+"의 그룹", TEST_GROUP_MEMBER_NAME);
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
        groupService.deleteGroup(TEST_GROUP_NAME);
    }

    // 그룹 코드로 모든 그룹 멤버 이름 조회 테스트
    @Test
    void getGroupMemberByGroupCodeTest() throws Exception {
        List<String> groupMemberNames = groupService.getGroupMemberByGroupCode(TEST_GROUP_KEY);

        for (String name : groupMemberNames) {
            System.out.println(name);
        }
    }

    // 그룹 코드로 그룹 이름 조회 테스트
    @Test
    void getGroupNameByGroupCodeTest() throws Exception {
        String groupName = groupService.getGroupNameByGroupCode(TEST_GROUP_KEY);
        System.out.println(groupName);
    }
}
