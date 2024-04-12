package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GroupServiceTest {
    private static final String TEST_GROUP_NAME = "testGroup";
    private static final String TEST_GROUP_KEY = "1234123412341234";

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
        groupService.getGroupMember(TEST_GROUP_NAME);
    }

    @Test
    public void updateGroup() throws Exception {
        Group updateGroup = new Group(TEST_GROUP_NAME, "4321432143214321");
        groupService.updateGroup(updateGroup);
    }

    // 그룹 정보 삭제 테스트
    @Test
    public void deleteGroupTest() throws Exception {
        groupService.deleteGroup(TEST_GROUP_NAME);
    }

    // 모든 그룹 멤버 이름 조회 테스트
    @Test
    void getGroupMemberTest() throws Exception {
        List<String> groupMemberNames = groupService.getGroupMember(TEST_GROUP_NAME);

        for (String name : groupMemberNames) {
            System.out.println(name);
        }
    }
}
