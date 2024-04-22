package ggwp.server.guardiango.service;

import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.entity.UserInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GroupService {
    String insertGroup(Group group) throws Exception;     // 그룹 추가

    Group addGroupMember(String groupKey, UserInfo user) throws Exception; // 그룹 멤버 추가

    Group deleteGroupMember(String deleteUserName) throws Exception; //그룹 멤버 정보 삭제

    String updateGroup(Group group) throws Exception; // 그룹 정보 수정

    void deleteGroup(UserInfo user) throws Exception; // 그룹 삭제

    List<Group> getGroups() throws ExecutionException, InterruptedException; // 모든 그룹 조회
    
    Group getGroupByGroupCode(String groupKey) throws Exception; // 그룹 키로 그룹 정보 조회

    List<String> getGroupMemberByGroupCode(String groupName) throws Exception; // 그룹 코드로 그룹원 조회
    
    String getGroupNameByGroupCode(String groupCode) throws Exception; // 그룹 코드로 그룹 이름 조회

    Group updateLocationInfo(UserInfo user) throws Exception; // 그룹 멤버의 본인 위치 업데이트

}