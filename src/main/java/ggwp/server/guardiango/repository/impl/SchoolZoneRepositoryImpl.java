package ggwp.server.guardiango.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.SchoolZone;
import ggwp.server.guardiango.repository.SchoolZoneRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolZoneRepositoryImpl implements SchoolZoneRepository {
    public static final String COLLECTION_NAME = "schoolZone";
    Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public List<SchoolZone> getLocations() throws Exception {
        List<SchoolZone> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .select("latitude", "longitude", "address").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) {
            throw new Exception("스쿨존 데이터를 찾을 수 없습니다.");
        } else {
            for (QueryDocumentSnapshot document : documents) {
                SchoolZone schoolZone = document.toObject(SchoolZone.class);
                list.add(schoolZone);
            }
        }
        return list;
    }
}
