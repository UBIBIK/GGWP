package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.Group;
import ggwp.server.guardiango.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        // Firestore에 그룹 정보를 저장합니다.
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(group.getGroupName()).set(group);
        // 성공적으로 저장되었을 때의 시간을 반환합니다.
        return future.get().getUpdateTime().toString();
    }

    //TODO 사용자 추가 코드 작성
    @Override
    public String addGroupMember(String groupName, String GroupMemberName, String groupRole) throws Exception {
        return " ";
    }

    @Override
    public String updateGroup(Group group) throws Exception {
        ApiFuture<WriteResult> future =
                firestore.collection(COLLECTION_NAME).document(group.getGroupName()).set(group);
        return future.get().getUpdateTime().toString();
    }

    // 그룹 이름으로 삭제
    @Override
    public String deleteGroup(String groupName) throws Exception {
        // 그룹 이름이 Firestore에 존재하는지 확인합니다.
        DocumentReference groupDocRef = firestore.collection(COLLECTION_NAME).document(groupName);
        ApiFuture<DocumentSnapshot> future = groupDocRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            groupDocRef.delete();
        } else {
            throw new Exception("삭제하려는 그룹 이름이 존재하지 않습니다.");
        }
        return groupName;
    }

    // 그룹 이름으로 그룹원 조회
    @Override
    public List<String> getGroupMember(String groupName) throws Exception {
        List<String> groupMemberNames = new ArrayList<>();

        // Firestore에서 그룹 이름으로 문서를 조회합니다.
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).whereEqualTo("groupName", groupName).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) {
            throw new Exception("Firestore에 해당 그룹 이름이 존재하지 않습니다.");
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
}
