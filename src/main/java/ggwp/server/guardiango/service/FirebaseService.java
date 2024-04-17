package ggwp.server.guardiango.service;

import ggwp.server.guardiango.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface FirebaseService {
    public String insertUser(User user) throws Exception; // 사용자 추가

    public User getUserDetail(String email) throws Exception; // 사용자 정보 조회

    public String updateUser(User user) throws ExecutionException, InterruptedException; // 사용자 정보 수정

    public void deleteUser(String email); // 사용자 삭제

    public List<User> getUsers() throws ExecutionException, InterruptedException; // 모든 사용자 조회
}
