package ggwp.server.guardiango.service;

import ggwp.server.guardiango.entity.Group;

import java.util.List;

public interface GroupService {
    public String insertGroup(Group group) throws Exception;     // 그룹 추가
    //TODO 그룹 멤버 추가 구현 예정
    public String addGroupMember(String groupName, String GroupMemberName, String groupRole) throws Exception;
    
    public String updateGroup(Group group) throws Exception; // 그룹 수정

    public String deleteGroup(String groupName) throws Exception; // 그룹 삭제

    public List<String> getGroupMember(String groupName) throws Exception; // 그룹원 조회
}