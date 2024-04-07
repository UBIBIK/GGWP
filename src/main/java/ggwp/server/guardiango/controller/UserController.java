package ggwp.server.guardiango.controller;


import ch.qos.logback.core.model.Model;
import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private FirebaseService firebaseService;

    @GetMapping()
    public void saveAndGetUserInfo(Model model) throws Exception {
        User user = new User();
        user.setEmail("ControllerTest123@naver.com");
        user.setName("ControllerTest");
        user.setPhoneNumber("01011111111");
        user.setPassword("qwe123");

        firebaseService.insertUser(user);
    }
}
