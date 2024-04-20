package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.service.GroupService;
import ggwp.server.guardiango.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class GroupServiceImpl implements GroupService {
    public static final String COLLECTION_NAME = "groups";
    Firestore firestore = FirestoreClient.getFirestore();
    private final UserService userService;

    @Autowired
    public GroupServiceImpl(UserService userService) {
        this.userService = userService;
    }

    // 그룹 추가
    @Override
    public String insertGroup(Group group) throws ExecutionException, InterruptedException {
        if (group == null) {
            throw new IllegalArgumentException("Group 객체가 null입니다.");
        }

        // 파이어베이스에 동일한 groupname을 가진 그룹이 있는지 확인
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(group.getGroupName());
        ApiFuture<DocumentSnapshot> futureSnapshot = docRef.get();
        DocumentSnapshot documentSnapshot = futureSnapshot.get();

        if (documentSnapshot.exists()) {
            // 동일한 groupname을 가진 사용자가 이미 존재하면 예외
            throw new IllegalArgumentException("동일한 userEmail을 가진 사용자가 이미 존재합니다.");
        }

        // 그룹 정보 저장
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(group.getGroupName()).set(group);
        // 성공적으로 저장되었을 때의 시간을 반환
        return future.get().getUpdateTime().toString();
    }

    // 그룳 멤버 추가
    @Override
    public Group addGroupMember(String groupKey, UserInfo user) throws Exception {
        // 그룹 이름으로 문서 조회
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).whereEqualTo("groupKey", groupKey).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.getFirst();
            Group group = document.toObject(Group.class);
            List<Map<String, Object>> groupMembers = group.getGroupMember();

            // 그룹 멤버 이름이 이미 존재하는지 검사
            for (Map<String, Object> member : groupMembers) {
                if (member.get("groupMemberName").equals(user.getUserName())) {
                    throw new Exception("해당 이름의 그룹 멤버가 이미 존재합니다.");
                }
            }

            // 새로운 멤버 정보를 추가합니다.
            Map<String, Object> memberInfo = new HashMap<>();
            memberInfo.put("groupMemberName", user.getUserName());
            memberInfo.put("groupRole", "보호 대상");
            group.getGroupMember().add(memberInfo);

            updateGroup(group);
            return group;
        }
        throw new Exception("그룹키에 해당하는 그룹은 존재하지 않습니다.");
    }

    // 그룹 멤버 정보 삭제
    @Override
    public String deleteGroupMember(String groupName, String groupMemberName) throws Exception {
        // Firestore에서 그룹 이름으로 문서를 조회
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).whereEqualTo("groupName", groupName).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.getFirst();
            Group group = document.toObject(Group.class);
            List<Map<String, Object>> groupMembers = group.getGroupMember();

            // groupMemberName이 존재하는지 확인, 존재하면 삭제
            for (Iterator<Map<String, Object>> it = groupMembers.iterator(); it.hasNext();) {
                Map<String, Object> member = it.next();
                if (member.get("groupMemberName").equals(groupMemberName)) {
                    it.remove();
                    userService.setGroupKeybyUserName(groupMemberName, null);
                    return updateGroup(group);
                }
            }
        }
        throw new Exception("해당하는 그룹은 존재하지 않습니다.");
    }

    // 그룹 정보 수정
    @Override
    public String updateGroup(Group group) throws Exception {
        ApiFuture<WriteResult> future =
                firestore.collection(COLLECTION_NAME).document(group.getGroupName()).set(group);
        return future.get().getUpdateTime().toString();
    }

    public void deleteGroup(UserInfo user) throws Exception {
        // 그룹 이름이 Firestore에 존재하는지 확인
        Query groupQuery = firestore.collection(COLLECTION_NAME).whereEqualTo("groupKey", user.getGroupKey());
        ApiFuture<QuerySnapshot> querySnapshot = groupQuery.get();

        if (querySnapshot.get().getDocuments().isEmpty()) {
            throw new Exception("삭제하려는 그룹이 존재하지 않습니다.");
        }

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            if (Objects.requireNonNull(document.getString("groupMaster")).equals(user.getUserEmail())) {
                String groupKey = document.getString("groupKey");

                List<User> userList = userService.getUsers();
                for (User findUser : userList) {
                    if (findUser.getGroupKey() != null && findUser.getGroupKey().equals(groupKey)) {
                        findUser.setGroupKey(null);
                        userService.updateUser(findUser);
                    }
                }

                // 그룹을 삭제
                document.getReference().delete();
            } else {
                throw new Exception("해당 그룹 마스터의 이메일이 일치하지 않습니다.");
            }
        }
    }


    // 모든 그룹 조회
    @Override
    public List<Group> getGroups() throws ExecutionException, InterruptedException {
        List<Group> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(Group.class));
        }
        return list;
    }

    // 그룹 키로 그룹 정보 조회
    @Override
    public Group getGroupByGroupCode(String groupKey) throws Exception {
        DocumentReference documentReference =
                firestore.collection(COLLECTION_NAME).document(groupKey);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        if(documentSnapshot.exists()){
            return documentSnapshot.toObject(Group.class);
        } else {
            throw new Exception("해당하는 유저가 존재하지 않습니다.");
        }
    }


    // 그룹 코드로 그룹원 조회
    @Override
    public List<String> getGroupMemberByGroupCode(String groupCode) throws Exception {
        List<String> groupMemberNames = new ArrayList<>();

        // Firestore에서 그룹 이름으로 문서를 조회
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).whereEqualTo("groupCode", groupCode).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) {
            throw new Exception("해당 그룹이 존재하지 않습니다.");
        }

        // 문서가 존재하면, 그룹 멤버의 이름을 리스트에 추가
        for (QueryDocumentSnapshot document : documents) {
            List<Map<String, Object>> groupMembers = (List<Map<String, Object>>) document.get("groupMember");
            if (groupMembers != null) {
                for (Map<String, Object> member : groupMembers) {
                    groupMemberNames.add((String) member.get("groupMemberName"));
                }
            }
        }
        return groupMemberNames;
    }

    // 그룹 코드로 그룹 이름 조회
    @Override
    public String getGroupNameByGroupCode(String groupCode) throws Exception {
        // Firestore에서 특정 필드로 문서를 조회합니다.
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).whereEqualTo("groupCode", groupCode).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.getFirst();
            return document.toObject(Group.class).getGroupName();
        } else {
            throw new Exception("해당하는 문서가 존재하지 않습니다.");
        }
    }

}