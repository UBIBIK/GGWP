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
}