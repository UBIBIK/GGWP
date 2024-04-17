package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class GroupServiceImpl implements GroupService {
    public static final String COLLECTION_NAME = "groups";
    Firestore firestore = FirestoreClient.getFirestore();

    // 그룹 추가
    @Override
    public String insertGroup(Group group) throws ExecutionException, InterruptedException {
        if (group == null) {
            throw new IllegalArgumentException("Group 객체가 null입니다.");
        }
        // 그룹 정보 저장
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(group.getGroupName()).set(group);
        // 성공적으로 저장되었을 때의 시간을 반환
        return future.get().getUpdateTime().toString();
    }

    // 그룳 멤버 추가
    @Override
    public String addGroupMember(String groupKey, String groupMemberName, String groupRole) throws Exception {
        // 그룹 이름으로 문서 조회
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).whereEqualTo("groupKey", groupKey).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.getFirst();
            Group group = document.toObject(Group.class);
            List<Map<String, Object>> groupMembers = group.getGroupMember();

            // 그룹 멤버 이름이 이미 존재하는지 검사
            for (Map<String, Object> member : groupMembers) {
                if (member.get("groupMemberName").equals(groupMemberName)) {
                    throw new Exception("해당 이름의 그룹 멤버가 이미 존재합니다.");
                }
            }

            // 새로운 멤버 정보를 추가합니다.
            Map<String, Object> memberInfo = new HashMap<>();
            memberInfo.put("groupMemberName", groupMemberName);
            memberInfo.put("groupRole", groupRole);
            group.getGroupMember().add(memberInfo);

            return updateGroup(group);
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

    // 그룹 이름으로 그룹 삭제
    @Override
    public String deleteGroup(String groupName) throws Exception {
        // 그룹 이름이 Firestore에 존재하는지 확인합니다.
        DocumentReference groupDocRef = firestore.collection(COLLECTION_NAME).document(groupName);
        ApiFuture<DocumentSnapshot> future = groupDocRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Group group = document.toObject(Group.class);
            String groupCode = group != null ? group.getGroupKey() : null;

            // User 컬렉션에서 groupCode와 일치하는 모든 문서를 검색
            ApiFuture<QuerySnapshot> userFuture = firestore.collection("User").whereEqualTo("group_Code", groupCode).get();
            List<QueryDocumentSnapshot> userDocuments = userFuture.get().getDocuments();

            // 일치하는 모든 사용자의 group_Code 필드를 삭제
            for (QueryDocumentSnapshot userDocument : userDocuments) {
                userDocument.getReference().update("group_Key", FieldValue.delete());
            }

            // 그룹을 삭제합니다.
            groupDocRef.delete();
        } else {
            throw new Exception("삭제하려는 그룹이 존재하지 않습니다.");
        }
        return groupName;
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

        // 문서가 존재하면, 그룹 멤버의 이름을 리스트에 추가합니다.
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
            return document.getId();
        } else {
            throw new Exception("해당하는 문서가 존재하지 않습니다.");
        }
    }
}