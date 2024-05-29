package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.EmergencyBell;
import ggwp.server.guardiango.service.EmergencyBellService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmergencyBellSeriveImpl implements EmergencyBellService {
    public static final String COLLECTION_NAME = "emergencyBell";
    Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public List<EmergencyBell> getEmergencyBells() throws Exception {
        List<EmergencyBell> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .select("latitude", "longitude", "address").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) {
            throw new Exception("비상벨 데이터를 찾을 수 없습니다.");
        } else {
            for (QueryDocumentSnapshot document : documents) {
                EmergencyBell emergencyBell = document.toObject(EmergencyBell.class);
                list.add(emergencyBell);
            }
        }
        return list;
    }
}
