package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.CCTV;
import ggwp.server.guardiango.service.CCTVService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CCTVServiceImpl implements CCTVService {
    public static final String COLLECTION_NAME = "CCTV";
    Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public List<CCTV> getCCTVs() throws Exception {
        List<CCTV> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .select("latitude", "longitude", "address").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (documents.isEmpty()) {
           throw new Exception("CCTV 데이터를 찾을 수 없습니다.");
        } else {
            for (QueryDocumentSnapshot document : documents) {
                if (document.exists()) {
                    CCTV cctv = document.toObject(CCTV.class);
                    list.add(cctv);
                } else {
                    System.out.println("해당 문서가 존재하지 않습니다." + document.getId());
                }
            }
        }
        return list;
    }
}