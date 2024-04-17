package ggwp.server.guardiango.service;

import ggwp.server.guardiango.entity.Group;

import java.util.List;

public interface GroupService {
    String insertGroup(Group group) throws Exception;     // 그룹 추가

    String addGroupMember(String groupCode, String GroupMemberName, String groupRole) throws Exception; // 그룳 멤버 추가
    // TODO 구현중
    String deleteGroupMember(String groupName, String groupMemberName) throws Exception; // 그룹 멤버 정보 삭제

    String updateGroup(Group group) throws Exception; // 그룹 정보 수정

    String deleteGroup(String groupName) throws Exception; // 그룹 삭제

    List<String> getGroupMemberByGroupCode(String groupName) throws Exception; // 그룹 코드로 그룹 이름 조회
    
    String getGroupNameByGroupCode(String groupCode) throws Exception; // 그룹 코드로 그룹 이름 조회
}