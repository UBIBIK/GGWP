package ggwp.server.guardiango.controller;

import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.service.FirebaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class AndroidController {

    private final FirebaseService firebaseService;

    @Autowired
    public AndroidController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/save-user") // 안드로이드 스튜디오를 통해 받은 사용자 객체를 파이어베이스에 저장
    @ResponseBody
    public void saveUser(@RequestBody User user) throws Exception {
        log.info("username={}",user.getUser_name());
        log.info("Phone_number={}",user.getPhone_number());
        log.info("useremail={}",user.getUser_email());
        log.info("userPassword={}",user.getPassword());
        firebaseService.insertUser(user);
    }

    //로그인 확인
    @PostMapping("/login")
    @ResponseBody
    public void loginUser(@RequestBody User loginUser) throws Exception {
        List<User> userList = firebaseService.getUsers();

        for (User user : userList){
            if(user.getUser_email().equals(loginUser.getUser_email())){
                if(user.getPassword().equals(loginUser.getPassword())){
                    log.info("사용자 로그인: {}", user.getUser_name());
                    return;
                }
            }
            else {
                log.info("로그인 실패 : {}",user.getUser_name());
                return;
            }
        }

    log.info("이메일이 존재하지 않습니다.:{}",loginUser.getUser_email());
    }
}