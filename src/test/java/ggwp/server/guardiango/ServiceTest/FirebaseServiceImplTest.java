package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.service.FirebaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FirebaseServiceImplTest {
    @Autowired
    private FirebaseService firebaseService;

    private static final String TEST_USER_EMAIL = "test@example.com";
    private static final String TEST_USER_NAME = "testUser";

    @Test
    void insertUser_Test() throws Exception {
        // 새로운 사용자 데이터 정보가 저장되는지 확인
        User user = new User();
        user.setUser_email(TEST_USER_EMAIL);
        user.setUser_name(TEST_USER_NAME);

        firebaseService.insertUser(user);
    }

    @Test
    void getUserDetail_Test() throws Exception {
        // 테스트용 사용자 데이터가 추가되었으므로 해당 사용자 정보를 가져와서 테스트
        User user = firebaseService.getUserDetail(TEST_USER_EMAIL);

        assertNotNull(user);
        assertEquals(TEST_USER_EMAIL, user.getUser_email());
        System.out.println(user.getUser_name() + ", " + user.getUser_email());
    }

    @Test
    void updateUser_Test() throws Exception {
        // 테스트용 사용자 데이터가 업데이트되는지 확인
        User updatedUser = new User();
        updatedUser.setUser_email("test@example.com");
        updatedUser.setUser_name("updater");

        String result = firebaseService.updateUser(updatedUser);

        assertNotNull(result);
    }

    @Test
    void deleteUser_Test() throws Exception {
        // 테스트용 사용자 데이터가 삭제되는지 확인
        String result = firebaseService.deleteUser(TEST_USER_EMAIL);

        assertNotNull(result);
    }

    @Test
    void getAllUsers() throws Exception {
        // 모든 사용자 데이터가 조회 되는지 확인
        List<User> users = firebaseService.getUsers();
        users.forEach(user -> System.out.println("user = " + user.getUser_email() + ", email = " + user.getUser_email()));
    }
}