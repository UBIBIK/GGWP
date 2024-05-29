package ggwp.server.guardiango.controller;

import ggwp.server.guardiango.entity.*;
import ggwp.server.guardiango.service.ElementSerivce;
import ggwp.server.guardiango.service.GroupService;
import ggwp.server.guardiango.service.UserReportService;
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
    private final ElementSerivce elementSerivce;
    private final UserReportService userReportService;

    @Autowired
    public AndroidController(UserService userService, GroupService groupService, ElementSerivce elementSerivce, UserReportService userReportService) {
        this.userService = userService;
        this.groupService = groupService;
        this.elementSerivce = elementSerivce;
        this.userReportService = userReportService;
    }

    // 회원가입
    @PostMapping("/save-user")
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
    public ResponseEntity<Group> groupCreate(@RequestBody UserInfo userinfo) throws Exception {
        List<User> userList = userService.getUsers();
        Group tempGroup = null;

        for (User user : userList) {
            if (user.getUserEmail().equals(userinfo.getUserEmail())) {
                tempGroup = new Group(userinfo.getUserName(), randomNumber());
                tempGroup.setGroupMaster(user.getUserEmail());
                userinfo.setGroupKey(tempGroup.getGroupKey());
                groupService.insertGroup(tempGroup, userinfo);
                user.setGroupKey(userinfo.getGroupKey());
                userService.updateUser(user); // user 컬렉션에 해당 groupKey 정보 업데이트
                groupService.updateLocationInfo(userinfo);
                userReportService.insertUserReport(userinfo);
                userReportService.insertUserReport(userinfo); // 사용자 신고 목록 생성
                return ResponseEntity.ok(tempGroup);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tempGroup);
    }

    // 그룹 참가
    @PostMapping("/group-join")
    @ResponseBody
    public ResponseEntity<Group> groupJoin(@RequestBody UserInfo userinfo) throws Exception {
        List<Group> list = groupService.getGroups();
        List<User> userList = userService.getUsers();
        Group updateGroup;

        log.info("그룹 참가 유저 이메일 = {}",userinfo.getUserEmail());
        log.info("받은 그룹 키 = {}", userinfo.getGroupKey());

        for(Group groupList : list) {
            log.info("데이터베이스 그룹키 = {}", groupList.getGroupKey());
            if(groupList.getGroupKey().equals(userinfo.getGroupKey())) {
                updateGroup = groupService.addGroupMember(userinfo.getGroupKey(), userinfo);

                for(User user : userList) {
                    if (user.getUserEmail().equals(userinfo.getUserEmail())) {
                        user.setGroupKey(updateGroup.getGroupKey());
                        groupService.updateLocationInfo(userinfo);
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
    public ResponseEntity<Group> getUserGroups(@RequestBody UserInfo user) throws ExecutionException, InterruptedException {
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

    // 그룹 삭제
    @PostMapping("/group-delete")
    @ResponseBody
    public void groupDelete(@RequestBody UserInfo user) throws Exception {
        userReportService.deleteUserReport(user);
        groupService.deleteGroup(user);
    }

    //그룹 멤버 삭제
    @PostMapping("/group-member-delete")
    @ResponseBody
    public ResponseEntity<Group> groupUserDelete(@RequestBody String deleteUserName) throws Exception {
        deleteUserName = deleteUserName.replaceAll("\"", "");
        log.info("삭제 할 멤버 이름 : {}", deleteUserName);
        Group updateGroup = groupService.deleteGroupMember(deleteUserName);

        return ResponseEntity.ok(updateGroup);
    }

    // 위치 정보를 저장하고 처리 결과를 반환하는 메서드
    @PostMapping("/save-location")
    public ResponseEntity<Group> updateLocation(@RequestBody UserInfo user) throws Exception {
        return ResponseEntity.ok(groupService.updateLocationInfo(user));
    }

    // 요소 정보 조회
    @PostMapping("/get-Element")
    public ResponseEntity<Element> getElement() throws Exception {
        return ResponseEntity.ok(elementSerivce.getElement());
    }

    // 사용자 신고 저장
    @PostMapping("/add-userReport")
    public ResponseEntity<UserReport> addUserReport(@RequestBody Report report, UserInfo user) throws Exception {
        return ResponseEntity.ok(userReportService.addReport(report, user));
    }

    // 사용자 신고 삭제
    @PostMapping("/report-delete")
    public ResponseEntity<UserReport> reportDelete(@RequestBody Report report, UserInfo user) throws Exception {
        return ResponseEntity.ok(userReportService.deleteReport(report, user));
    }

    // 사용자 신고 정보 조회
    @PostMapping("/get-report")
    public ResponseEntity<Report> getReport(@RequestBody LocationData deleteLocalDate, UserInfo user) throws Exception {
        return ResponseEntity.ok(userReportService.getReportByLocation(deleteLocalDate, user));
    }

    // 신고 정보 조회
    @PostMapping("/get-group-reports")
    public ResponseEntity<List<Report>> getGroupReports(@RequestBody UserInfo user) throws Exception {
        return ResponseEntity.ok(userReportService.getReportsLocationByGroupKey(user.getGroupKey()));
    }
}