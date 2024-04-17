package ggwp.server.guardiango.controller;

import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.entity.Groupcode;
import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.service.FirebaseService;
import ggwp.server.guardiango.service.GroupService;
import ggwp.server.guardiango.service.impl.GroupServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@Slf4j
@RestController
public class AndroidController {

    private final FirebaseService firebaseService;
    private final GroupService groupService;

    @Autowired
    public AndroidController(FirebaseService firebaseService, GroupServiceImpl groupServiceImpl, GroupService groupService) {
        this.firebaseService = firebaseService;
        this.groupService = groupService;
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
    public ResponseEntity<UserInfo> loginUser(@RequestBody User loginUser) throws Exception {
        List<User> userList = firebaseService.getUsers();

        for (User user : userList) {
            if (user.getUser_email().equals(loginUser.getUser_email())) {
                if (user.getPassword().equals(loginUser.getPassword())) {
                    log.info("사용자 로그인: {}", user.getUser_name());
                    UserInfo userinfo = new UserInfo(user.getGroup_key(), user.getUser_email()
                            , user.getPhone_number(), user.getUser_name());
                    return ResponseEntity.ok(userinfo);
                }
            } else {
                log.info("로그인 실패 : {}", user.getUser_name());
                UserInfo userinfo = new UserInfo();
                userinfo.setStatus("실패");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userinfo);
            }
        }
        throw new Exception("사용자를 찾을 수 없습니다.");
    }

    // 그룹장 이름 받아오기
    @PostMapping("/group-create")
    @ResponseBody
    public void groupCreate(@RequestBody Group group) throws Exception {
        Group tempGroup = new Group(group.getGroupName(), randomNumber());
        log.info("reader={}", tempGroup.getGroupName());
        log.info("code={}", tempGroup.getGroupCode());
        groupService.insertGroup(tempGroup);
//        firebaseService.updateUser();
    }

    // 그룹참가 코드 받아오기
    @PostMapping("/group-join")
    @ResponseBody
    public ResponseEntity<String> groupJoin(@RequestBody Groupcode code) {
        String correctCode = "1234";
        log.info("Received group join code: {}", code.getGroupCode());

        if (code.getGroupCode().equals(correctCode)) {
            log.info("Group join code is correct.");
//            groupService.addGroupMember();
            return ResponseEntity.ok("그룹 참가 성공!");
        } else {
            log.error("Invalid group join code received: {}", code.getGroupCode());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("올바른 그룹 코드가 아닙니다.");
        }
    }

    // 난수 생성 함수
    public String randomNumber() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
}