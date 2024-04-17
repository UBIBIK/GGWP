package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GroupServiceTest {
    private static final String TEST_GROUP_NAME = "group";
    private static final String TEST_GROUP_MEMBER_NAME = "testMember";
    private static final String TEST_GROUP_CODE = "k4p8usof9v312lht";

    @Autowired
    private GroupService groupService;
    
    // 그룹 정보 추가 테스트
    @Test
    public void insertGroupTest() throws Exception {
        Group group = new Group(TEST_GROUP_NAME, TEST_GROUP_CODE);
        groupService.insertGroup(group);
    }

    // 그룹 멤버 추가 테스트
    @Test
    public void addGroupMemberTest() throws Exception {
        groupService.addGroupMember(TEST_GROUP_CODE, TEST_GROUP_MEMBER_NAME, "보호 대상");
    }

    /*@Test
    public void deleteGroupMemberTest() throws Exception {
        groupService.deleteGroupMember(TEST_GROUP_NAME, TEST_GROUP_MEMBER_NAME);
    }*/

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

    // 모든 그룹 멤버 이름 조회 테스트
    @Test
    void getGroupMemberByGroupCodeTest() throws Exception {
        List<String> groupMemberNames = groupService.getGroupMemberByGroupCode(TEST_GROUP_CODE);

        for (String name : groupMemberNames) {
            System.out.println(name);
        }
    }

    @Test
    void getGroupNameByGroupCodeTest() throws Exception {
        String groupName = groupService.getGroupNameByGroupCode(TEST_GROUP_CODE);
        System.out.println(groupName);
    }
}
