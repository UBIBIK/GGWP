package ggwp.server.guardiango.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import ggwp.server.guardiango.entity.LocationData;
import ggwp.server.guardiango.entity.Report;
import ggwp.server.guardiango.entity.UserInfo;
import ggwp.server.guardiango.entity.UserReport;
import ggwp.server.guardiango.service.UserReportService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserReportServiceImpl implements UserReportService {
    public static final String COLLECTION_NAME = "userReport";
    Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public String insertUserReport(UserInfo user) throws ExecutionException, InterruptedException {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("User 객체가 null입니다.");
        }

        // 파이어베이스에 동일한 그룹키를 가진 사용자 신고 목록이 있는지 확인
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(user.getGroupKey());
        ApiFuture<DocumentSnapshot> futureSnapshot = docRef.get();
        DocumentSnapshot documentSnapshot = futureSnapshot.get();

        if(documentSnapshot.exists()) {
            // 동일한 그룹키를 가진 사용자 신고 목록이 있으면 예외
            throw new IllegalArgumentException("동일한 그룹키를 가진 사용자 신고 목록이 존재합니다.");
        }

        UserReport userReport = new UserReport(user.getUserName(), user.getGroupKey());

        // 사용자 신고 정보 저장
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(userReport.getReportName()).set(userReport);

        // 성공적으로 저장되었을 때의 시간을 반환
        return future.get().getUpdateTime().toString();
    }

    @Override
    public UserReport addReport(Report report, UserInfo user) throws Exception {
        // Firestore에서 그룹 컬렉션을 참조
        CollectionReference userReportCollection = firestore.collection(COLLECTION_NAME);

        // 그룹 키로 그룹 문서를 조회
        ApiFuture<QuerySnapshot> future = userReportCollection.whereEqualTo("groupKey", user.getGroupKey()).get();

        // 문서 결과를 가져옴
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if(!documents.isEmpty()) {
            // 첫 번째 문서를 가져옴
            QueryDocumentSnapshot document = documents.getFirst();

            // 문서를 객체로 변환
            UserReport userReport = document.toObject(UserReport.class);

            // 새로운 신고 정보를 생성
            Map<String, Object> newUserReport = new HashMap<>();
            newUserReport.put("reporterName", user.getUserName());
            newUserReport.put("latitude", report.getLatitude());
            newUserReport.put("longitude", report.getLongitude());
            newUserReport.put("reportTime", report.getTime());

            if(!report.getImage().isEmpty()) {
                newUserReport.put("image", report.getImage());
            }

            // 신고 목록에 새로운 신고 정보를 추가
            userReport.getReport().add(newUserReport);

            // 그룹 정보를 업데이트
            updateUserReport(userReport);

            // 업데이트된 그룹 정보를 반환
            return userReport;
        }

        // 문서가 존재하지 않는 경우 예외를 발생
        throw new Exception("해당 그룹 신고 목록이 존재하지 않습니다.");
    }


    // 신고 정보 수정
    @Override
    public String updateUserReport(UserReport userReport) throws Exception {
        ApiFuture<WriteResult> future =
                firestore.collection(COLLECTION_NAME).document(userReport.getReportName()).set(userReport);
        return future.get().getUpdateTime().toString();
    }

    @Override
    public UserReport deleteReport(Report report, UserInfo user) throws Exception {
        // 신고 목록이 존재하는지 확인
        Query UserReportQuery = firestore.collection(COLLECTION_NAME).whereEqualTo("groupKey", user.getGroupKey());
        ApiFuture<QuerySnapshot> querySnapshot = UserReportQuery.get();

        // 신고 목록이 존재하지 않으면 예외처리
        if (querySnapshot.get().getDocuments().isEmpty()) {
            throw new Exception("해당 그룹이 존재하지 않습니다.");
        }

        // 문서 결과를 가져옴
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if(!documents.isEmpty()) {
            // 첫 번째 문서를 가져옴
            QueryDocumentSnapshot document = documents.getFirst();

            // 문서를 객체로 변환
            UserReport userReport = document.toObject(UserReport.class);

            List<Map<String, Object>> reports = userReport.getReport();

            // 위도와 경도가 일치하는 요소를 찾아 삭제
            boolean isRemoved = reports.removeIf(findReport ->
                    (double) findReport.get("latitude") == report.getLatitude() &&
                            (double) findReport.get("longitude") == report.getLongitude()
            );

            if(isRemoved) {
                // 변경 사항을 업데이트
                document.getReference().set(userReport);
                return userReport;
            } else {
                throw new Exception("해당 신고를 찾을 수 없습니다.");
            }
        } else {
            // 문서가 존재하지 않는 경우 예외를 발생
            throw new Exception("해당 그룹 신고 목록이 존재하지 않습니다.");
        }
    }

    @Override
    public Report getUserReport(LocationData reportLocation, UserInfo user) throws Exception {
        // 신고 목록이 존재하는지 확인
        Query UserReportQuery = firestore.collection(COLLECTION_NAME).whereEqualTo("groupKey", user.getGroupKey());
        ApiFuture<QuerySnapshot> querySnapshot = UserReportQuery.get();

        // 신고 목록이 존재하지 않으면 예외처리
        if (querySnapshot.get().getDocuments().isEmpty()) {
            throw new Exception("해당 그룹이 존재하지 않습니다.");
        }

        // 문서 결과를 가져옴
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if(!documents.isEmpty()) {
            // 첫 번째 문서를 가져옴
            QueryDocumentSnapshot document = documents.getFirst();

            // 문서를 객체로 변환
            UserReport userReport = document.toObject(UserReport.class);
            ArrayList<Map<String, Object>> reports = userReport.getReport();
            // 위도와 경도가 일치하는 요소 조회
            Report findReport = new Report();
            for (Map<String, Object> report : reports) {
                if (report.get("latitude").equals(reportLocation.getLatitude()) && report.get("longitute").equals(reportLocation.getLongitude())) {
                    findReport.setLatitude(reportLocation.getLatitude());
                    findReport.setLongitude(reportLocation.getLongitude());
                    findReport.setImage(report.get("image").toString());
//                    findReport.setTime(report.get("time").toString());
                }
            }

            return findReport;
        } else {
            throw new Exception("해당 신고를 찾을 수 없습니다.");
        }
    }
}