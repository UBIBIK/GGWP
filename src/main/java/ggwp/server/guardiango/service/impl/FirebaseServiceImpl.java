package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.User;
import ggwp.server.guardiango.service.FirebaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseServiceImpl implements FirebaseService {
    public static final String COLLECTION_NAME = "users";
    Firestore firestore = FirestoreClient.getFirestore();

    // 사용자 추가
    @Override
    public String insertUser(User user) throws ExecutionException, InterruptedException {
        if (user == null) {
            throw new IllegalArgumentException("User 객체가 null입니다.");
        }
        // Firestore에 사용자 정보를 저장합니다.
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(user.getUser_email()).set(user);
        // 성공적으로 저장되었을 때의 시간을 반환합니다.
        return future.get().getUpdateTime().toString();
    }

    // 사용자 정보 조회
    @Override
    public User getUserDetail(String email) throws Exception {
        DocumentReference documentReference =
                firestore.collection(COLLECTION_NAME).document(email);
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        if(documentSnapshot.exists()){
            return documentSnapshot.toObject(User.class);
        } else {
            throw new Exception("해당하는 유저가 존재하지 않습니다.");
        }
    }

    // 사용자 정보 수정
    @Override
    public String updateUser(User user) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(user.getUser_email()).set(user);
        return future.get().getUpdateTime().toString();
    }

    // 사용자 삭제
    @Override
    public void deleteUser(String email) {
        firestore.collection(COLLECTION_NAME).document(email).delete();
    }

    // 모든 사용자 조회
    @Override
    public List<User> getUsers() throws ExecutionException, InterruptedException {
        List<User> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            list.add(document.toObject(User.class));
        }
        return list;
    }
}