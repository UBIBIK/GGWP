package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.service.FirebaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class FirebaseServiceImplTest {
    @Autowired
    private FirebaseService firebaseService;

    private static final String TEST_USER_EMAIL = "test@ample.com";
    private static final String TEST_USER_NAME = "testUser";

    @Test
    void insertUser_Test() throws Exception {
        // 새로운 사용자 데이터 정보가 저장되는지 확인
        User user = new User(TEST_USER_EMAIL, TEST_USER_NAME, "01012341234", "123qwe");

        firebaseService.insertUser(user);
    }

    @Test
    void getUserDetail_Test() throws Exception {
        // 테스트용 사용자 데이터가 추가되었으므로 해당 사용자 정보를 가져와서 테스트
        User user = firebaseService.getUserDetail(TEST_USER_EMAIL);

        System.out.println(user.getUser_name() + ", " + user.getUser_email());
    }

    @Test
    void updateUser_Test() throws ExecutionException, InterruptedException {
        // 테스트용 사용자 데이터가 업데이트되는지 확인
        User updatedUser = new User();
        updatedUser.setUser_email("3jsex@naver.com");
        updatedUser.setUser_name("updater");
        firebaseService.updateUser(updatedUser);
    }

    @Test
    void deleteUser_Test() throws Exception {
        // 테스트용 사용자 데이터가 삭제되는지 확인
        firebaseService.deleteUser(TEST_USER_EMAIL);
    }

    @Test
    void getAllUsers() throws Exception {
        // 모든 사용자 데이터가 조회 되는지 확인
        List<User> users = firebaseService.getUsers();
        users.forEach(user -> System.out.println("user = " + user.getUser_name() + ", email = " + user.getUser_email()));
    }
}