package ggwp.server.guardiango.controller;

import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.service.FirebaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AndroidController {

    private final FirebaseService firebaseService;

    @Autowired
    public AndroidController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/save-user")
    @ResponseBody
    public void saveUser(@RequestBody User user) throws Exception {
        log.info("username={}",user.getUser_name());
        log.info("Phone_number={}",user.getPhone_number());
        log.info("useremail={}",user.getUser_email());
        log.info("userPassword={}",user.getPassword());
        firebaseService.insertUser(user);
    }
}