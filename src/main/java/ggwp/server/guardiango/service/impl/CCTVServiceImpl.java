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
            System.out.println("No CCTV data found in Firestore.");
        } else {
            System.out.println("Retrieved CCTV data from Firestore:");
            for (QueryDocumentSnapshot document : documents) {
                if (document.exists()) {
                    CCTV cctv = document.toObject(CCTV.class);
                    System.out.println(cctv); // CCTV 객체의 상태를 출력
                    list.add(cctv);
                } else {
                    System.out.println("Document does not exist: " + document.getId());
                }
            }
        }
        return list;
    }
}