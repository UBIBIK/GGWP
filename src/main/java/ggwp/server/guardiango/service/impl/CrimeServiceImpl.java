package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.Crime;
import ggwp.server.guardiango.service.CrimeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrimeServiceImpl implements CrimeService {
    public static final String COLLECTION_NAME = "crime";
    Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public List<Crime> getCrimes() throws Exception {
        List<Crime> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .select("위도", "경도", "거주지").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) {
            throw new Exception("범죄자 데이터를 찾을 수 없습니다.");
        } else {
            for (QueryDocumentSnapshot document : documents) {
                Crime crime = document.toObject(Crime.class);
                list.add(crime);
            }
        }
        return list;
    }
}
