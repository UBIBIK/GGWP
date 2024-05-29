package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.ConvenienceStore;
import ggwp.server.guardiango.service.ConvenienceStoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConvenienceStoreServiceImpl implements ConvenienceStoreService {
    public static final String COLLECTION_NAME = "convenienceStore";
    Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public List<ConvenienceStore> getConvenienceStores() throws Exception {
        List<ConvenienceStore> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .select("latitude", "longitude", "address").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) {
            throw new Exception("CCTV 데이터를 찾을 수 없습니다.");
        } else {
            for (QueryDocumentSnapshot document : documents) {
                ConvenienceStore convenienceStore = document.toObject(ConvenienceStore.class);
                list.add(convenienceStore);
            }
        }
        return list;
    }
}
