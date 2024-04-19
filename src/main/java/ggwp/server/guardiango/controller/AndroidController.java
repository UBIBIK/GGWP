package ggwp.server.guardiango.controller;

import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.service.GroupService;
import ggwp.server.guardiango.service.UserService;
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
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class AndroidController {

    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public AndroidController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @PostMapping("/save-user") // 안드로이드 스튜디오를 통해 받은 사용자 객체를 파이어베이스에 저장
    @ResponseBody
    public void saveUser(@RequestBody User user) throws Exception {
        log.info("username={}",user.getUserName());
        log.info("Phone_number={}",user.getPhoneNumber());
        log.info("useremail={}",user.getUserEmail());
        log.info("userPassword={}",user.getPassword());
        userService.insertUser(user);
    }

    //로그인 확인
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserInfo> loginUser(@RequestBody User loginUser) throws Exception {
        List<User> userList = userService.getUsers();

        log.info("입력받은 이메일 = {}",loginUser.getUserEmail());
        log.info("입력받은 비밀번호 = {}",loginUser.getPassword());

        for (User user : userList) {
            log.info("데이터베이스 이메일 = {}", user.getUserEmail());
            if (user.getUserEmail().equals(loginUser.getUserEmail())) {
                if (user.getPassword().equals(loginUser.getPassword())) {
                    log.info("사용자 로그인: {}", user.getUserName());
                    UserInfo userinfo = new UserInfo(user.getGroupKey(), user.getUserEmail()
                            , user.getPhoneNumber(), user.getUserName());
                    return ResponseEntity.ok(userinfo);
                }
            }
        }
        throw new Exception("사용자를 찾을 수 없습니다.");
    }

    // 그룹을 만드는 유저의 정보 받아오기
    @PostMapping("/group-create")
    @ResponseBody
    public ResponseEntity<UserInfo> groupCreate(@RequestBody UserInfo userinfo) throws Exception {
        List<User> userList = userService.getUsers();

        for (User user : userList) {
            if (user.getUserEmail().equals(userinfo.getUserEmail())) {
                Group tempGroup = new Group(userinfo.getUserName(), randomNumber());
                userinfo.setGroupKey(tempGroup.getGroupKey());
                log.info("reader={}", tempGroup.getGroupName());
                log.info("code={}", tempGroup.getGroupKey());
                groupService.insertGroup(tempGroup);
                user.setGroupKey(userinfo.getGroupKey());
                userService.updateUser(user); // user 컬렉션에 해당 groupKey 정보 업데이트
                return ResponseEntity.ok(userinfo);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userinfo);
    }

    // 그룹참가
    @PostMapping("/group-join")
    @ResponseBody
    public ResponseEntity<Group> groupJoin(@RequestBody UserInfo userinfo, String groupKey) throws Exception {
        List<Group> list = groupService.getGroups();
        List<User> userList = userService.getUsers();
        Group updateGroup;

        for(Group groupList : list) {
            if(groupList.getGroupKey().equals(groupKey)) {
                updateGroup = groupService.addGroupMember(groupKey, userinfo);

                for(User user : userList) {
                    if (user.getUserEmail().equals(userinfo.getUserEmail())) {
                        user.setGroupKey(updateGroup.getGroupKey());
                        userService.updateUser(user); // user 컬렉션에 해당 groupKey 정보 업데이트
                    }
                }

                return ResponseEntity.ok(updateGroup);
            }
        }

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 그룹 존재 여부 확인
    @PostMapping("/group-exist")
    @ResponseBody
    public ResponseEntity<Group> groupJoin(@RequestBody UserInfo user) throws ExecutionException, InterruptedException {
        List<Group> list = groupService.getGroups();

        for(Group groupList : list) {
            if(groupList.getGroupKey().equals(user.getGroupKey())) {
                return ResponseEntity.ok(groupList);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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