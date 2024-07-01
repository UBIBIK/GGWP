package ggwp.server.guardiango.repository;

import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.entity.UserInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GroupRepository {
    String insertGroup(Group group, UserInfo user) throws ExecutionException, InterruptedException;     // 그룹 추가

    Group addGroupMember(String groupKey, UserInfo user) throws Exception; // 그룹 멤버 추가

    Group deleteGroupMember(String deleteUserName) throws Exception; // 그룹 멤버 삭제

    String updateGroup(Group group) throws Exception; // 그룹 정보 수정

    boolean deleteGroup(UserInfo user) throws Exception; // 그룹 삭제

    List<Group> getGroups() throws ExecutionException, InterruptedException; // 모든 그룹 조회

    Group getGroupByGroupKey(String groupKey) throws Exception; // 그룹 키로 그룹 정보 조회
   
    List<String> getGroupMemberNameByGroupKey(String groupKey) throws Exception;  // 그룹 코드로 그룹원 이름 조회

    String getGroupNameByGroupKey(String groupKey) throws Exception; // 그룹 코드로 그룹 이름 조회

    Group updateLocationInfo(UserInfo user) throws Exception; // 그룹 멤버의 본인 위치 업데이트
}