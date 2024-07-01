package ggwp.server.guardiango.repository;

import ggwp.server.guardiango.entity.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserRepository {
    String insertUser(User user) throws Exception; // 사용자 추가

    User getUserDetail(String email) throws Exception; // 사용자 정보 조회

    String updateUser(User user) throws ExecutionException, InterruptedException; // 사용자 정보 수정

    void deleteUser(String email); // 사용자 삭제

    List<User> getUsers() throws ExecutionException, InterruptedException; // 모든 사용자 조회

    void setGroupKeybyUserName(String userName, String groupKey) throws Exception; // 그룹 키 정보 수정
}
