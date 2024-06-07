package ggwp.server.guardiango.controller;

import ggwp.server.guardiango.entity.*;
import ggwp.server.guardiango.repository.ElementRepository;
import ggwp.server.guardiango.repository.GroupRepository;
import ggwp.server.guardiango.repository.UserReportRepository;
import ggwp.server.guardiango.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class AndroidController {

    @Value("${server.host}")
    private String serverHost;

    @Value("${server.port}")
    private int serverPort;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ElementRepository elementRepository;
    private final UserReportRepository userReportRepository;

    @Autowired
    public AndroidController(UserRepository userRepository, GroupRepository groupRepository, ElementRepository elementRepository, UserReportRepository userReportRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.elementRepository = elementRepository;
        this.userReportRepository = userReportRepository;
    }

    // 회원가입
    @PostMapping("/save-user")
    @ResponseBody
    public void saveUser(@RequestBody User user) throws Exception {
        log.info("username={}",user.getUserName());
        log.info("Phone_number={}",user.getPhoneNumber());
        log.info("useremail={}",user.getUserEmail());
        log.info("userPassword={}",user.getPassword());
        userRepository.insertUser(user);
    }

    //로그인 확인
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserInfo> loginUser(@RequestBody User loginUser) throws Exception {
        List<User> userList = userRepository.getUsers();

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
        List<User> userList = userRepository.getUsers();
        Group tempGroup = null;

        for (User user : userList) {
            if (user.getUserEmail().equals(userinfo.getUserEmail())) {
                tempGroup = new Group(userinfo.getUserName(), randomNumber());
                tempGroup.setGroupMaster(user.getUserEmail());
                userinfo.setGroupKey(tempGroup.getGroupKey());
                groupRepository.insertGroup(tempGroup, userinfo);
                user.setGroupKey(userinfo.getGroupKey());
                userRepository.updateUser(user); // user 컬렉션에 해당 groupKey 정보 업데이트
                groupRepository.updateLocationInfo(userinfo);
                userReportRepository.insertUserReport(userinfo);
                userReportRepository.insertUserReport(userinfo); // 사용자 신고 목록 생성
                return ResponseEntity.ok(tempGroup);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tempGroup);
    }

    // 그룹 참가
    @PostMapping("/group-join")
    @ResponseBody
    public ResponseEntity<Group> groupJoin(@RequestBody UserInfo userinfo) throws Exception {
        List<Group> list = groupRepository.getGroups();
        List<User> userList = userRepository.getUsers();
        Group updateGroup;

        log.info("그룹 참가 유저 이메일 = {}",userinfo.getUserEmail());
        log.info("받은 그룹 키 = {}", userinfo.getGroupKey());

        for(Group groupList : list) {
            log.info("데이터베이스 그룹키 = {}", groupList.getGroupKey());
            if(groupList.getGroupKey().equals(userinfo.getGroupKey())) {
                updateGroup = groupRepository.addGroupMember(userinfo.getGroupKey(), userinfo);

                for(User user : userList) {
                    if (user.getUserEmail().equals(userinfo.getUserEmail())) {
                        user.setGroupKey(updateGroup.getGroupKey());
                        groupRepository.updateLocationInfo(userinfo);
                        userRepository.updateUser(user); // user 컬렉션에 해당 groupKey 정보 업데이트
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
        List<Group> list = groupRepository.getGroups();

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
        userReportRepository.deleteUserReport(user);
        groupRepository.deleteGroup(user);
    }

    // 그룹 멤버 삭제
    @PostMapping("/group-member-delete")
    @ResponseBody
    public ResponseEntity<Group> groupUserDelete(@RequestBody String deleteUserName) throws Exception {
        deleteUserName = deleteUserName.replaceAll("\"", "");
        log.info("삭제 할 멤버 이름 : {}", deleteUserName);
        Group updateGroup = groupRepository.deleteGroupMember(deleteUserName);

        return ResponseEntity.ok(updateGroup);
    }

    // 위치 정보를 저장하고 처리 결과를 반환하는 메서드
    @PostMapping("/save-location")
    public ResponseEntity<Group> updateLocation(@RequestBody UserInfo user) throws Exception {
        return ResponseEntity.ok(groupRepository.updateLocationInfo(user));
    }

    // 요소 정보 조회
    @PostMapping("/get-element")
    public ResponseEntity<Element> getElement() throws Exception {
        return ResponseEntity.ok(elementRepository.getElement());
    }

    // 사용자 신고 저장
    @PostMapping("/add-userReport")
    public ResponseEntity<UserReport> addUserReport(@RequestBody Report report, UserInfo user) throws Exception {
        return ResponseEntity.ok(userReportRepository.addReport(report, user));
    }

    // 사용자 신고 삭제
    @PostMapping("/report-delete")
    public ResponseEntity<UserReport> reportDelete(@RequestBody Report report, UserInfo user) throws Exception {
        return ResponseEntity.ok(userReportRepository.deleteReport(report, user));
    }

    // 단일 신고 정보 조회
    @PostMapping("/get-report")
    public ResponseEntity<Report> getReport(@RequestBody LocationData reportLocation, UserInfo user) throws Exception {
        return ResponseEntity.ok(userReportRepository.getReportByLocation(reportLocation, user));
    }

    // 그룹 신고 정보 조회
    @PostMapping("/get-userReport")
    public ResponseEntity<UserReport> getGroupReports(@RequestBody UserInfo user) throws Exception {
        return ResponseEntity.ok(userReportRepository.getUserReportByGroupKey(user));
    }

    // PostData 엔드포인트
    @PostMapping("/upload-postdata")
    public ResponseEntity<?> uploadPostData(
            @RequestPart("image") MultipartFile imageFile,
            @RequestPart("postData") PostData postData) {

        UserInfo userInfo = postData.getUserInfo();
        String groupKey = userInfo.getGroupKey(); // Assuming UserInfo has getGroupKey() method
        if (groupKey == null || groupKey.isEmpty()) {
            return ResponseEntity.badRequest().body("Group key is missing");
        }

        // 이미지 파일 저장 로직
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        String uploadDir = "uploads/" + groupKey + "/";
        File uploadDirectory = new File(uploadDir);

        // 디렉토리가 존재하지 않으면 생성
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        File file = new File(uploadDir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 이미지 URL 생성
        String UUID = serverHost + ":" + serverPort + "/" + uploadDir + fileName;
        postData.setUUID(UUID);

        // 로그로 데이터 확인
        log.info("Post content: {}", postData.getPostContent());
        log.info("User info - Email: {}, Name: {}, Phone: {}", userInfo.getUserEmail(), userInfo.getUserName(), userInfo.getPhoneNumber());
        log.info("Image URL: {}", postData.getUUID());
        log.info("Location - Latitude: {}, Longitude: {}", postData.getLatitude(), postData.getLongitude());

        // 로그로 데이터가 성공적으로 수신되었음을 확인
        log.info("Data successfully received from client.");

        // 클라이언트로 응답
        return ResponseEntity.ok("Post data uploaded successfully");
    }
}