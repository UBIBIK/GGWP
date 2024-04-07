package ggwp.server.guardiango.ServiceTest;

import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.service.FirebaseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FirebaseServiceImplTest {
    @Autowired
    private FirebaseService firebaseService;

    private static final String TEST_USER_EMAIL = "test@example.com";
    private static final String TEST_USER_NAME= "testUser";

    @Test
    void insertUser_Test() throws Exception {
        User user = new User();
        user.setEmail(TEST_USER_EMAIL);
        user.setName(TEST_USER_NAME);

        firebaseService.insertUser(user);
    }

    @Test
    void getUserDetail_Test() throws Exception {
        // 테스트용 사용자 데이터가 추가되었으므로 해당 사용자 정보를 가져와서 테스트
        User user = firebaseService.getUserDetail(TEST_USER_EMAIL);

        assertNotNull(user);
        assertEquals(TEST_USER_EMAIL, user.getEmail());
        assertEquals(TEST_USER_NAME, user.getName());
        System.out.println(user.getName() + ", " + user.getEmail());
    }

    @Test
    void updateUser_Test() throws Exception {
        // 테스트용 사용자 데이터가 업데이트되는지 확인
        User updatedUser = new User();
        updatedUser.setEmail("updateduser@naver.com");
        updatedUser.setName("updater");

        String result = firebaseService.updateUser(updatedUser);

        assertNotNull(result);
    }

    @Test
    void deleteUser_Test() throws Exception {
        String result = firebaseService.deleteUser(TEST_USER_EMAIL);

        assertNotNull(result);
    }
}