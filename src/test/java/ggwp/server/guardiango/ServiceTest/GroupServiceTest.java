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
    private static final String TEST_GROUP_MASTER = "test1@example.com";
    private static final String TEST_GROUP_MEMBER_NAME = "test2";
    private static final String TEST_GROUP_KEY = "09bokfesd81vcc60";

    @Autowired
    private GroupService groupService;
    
    // 그룹 추가 테스트
    @Test
    public void insertGroupTest() throws Exception {
        Group group = new Group(TEST_GROUP_NAME, TEST_GROUP_KEY);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserEmail(TEST_GROUP_MASTER);
        groupService.insertGroup(group, userInfo);
    }

    // 그룹 멤버 추가 테스트
    @Test
    public void addGroupMemberTest() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(TEST_GROUP_MEMBER_NAME);
        Group group = groupService.addGroupMember(TEST_GROUP_KEY, userInfo);
        System.out.println(group.getGroupKey());
    }

    // 그룹 멤버 삭제 테스트
    @Test
    public void deleteGroupMemberTest() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserEmail(TEST_GROUP_MASTER);
        groupService.deleteGroupMember(TEST_GROUP_MEMBER_NAME, userInfo);
    }

    // 그룹 수정 테스트
    @Test
    public void updateGroup() throws Exception {
        Group updateGroup = new Group(TEST_GROUP_NAME, "09bokfesd81vcc60");
        groupService.updateGroup(updateGroup);
    }

    // 그룹 삭제 테스트
    @Test
    public void deleteGroupTest() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserEmail(TEST_GROUP_MASTER);
        userInfo.setGroupKey(TEST_GROUP_KEY);
        groupService.deleteGroup(userInfo);
    }

    @Test
    public void getGroupByGroupCodeTest() throws Exception {
        Group group = groupService.getGroupByGroupCode(TEST_GROUP_KEY);
        System.out.println(group.getGroupName());
        System.out.println(group.getGroupKey());
    }

    // 그룹 코드로 모든 그룹 멤버 이름 조회 테스트
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
}