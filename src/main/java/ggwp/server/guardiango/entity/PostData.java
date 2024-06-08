package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostData {
    private UserInfo userInfo;
    private String postContent;
    private String UUID; // 이미지 URL
    private double latitude;
    private double longitude;

    public PostData(UserInfo userInfo, String postContent, String imageUrl) {
        this.userInfo = userInfo;
        this.postContent = postContent;
        this.UUID = imageUrl;
    }

    public PostData(UserInfo testUserInfo, String postContent, String uuid, double latitude, double longitude) { // UserReportRepositoryTest를 위한 생성자
        this.userInfo = testUserInfo;
        this.postContent = postContent;
        this.UUID = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}