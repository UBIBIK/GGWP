package ggwp.server.guardiango.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.Objects;

// 파이어베이스 초기화
@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void init() {
        try {
            // 파일 시스템 경로 대신 클래스패스 리소스 접근 방식을 사용
            InputStream serviceAccount = getClass().getResourceAsStream("/serviceAccountKey.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(Objects.requireNonNull(serviceAccount)))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
