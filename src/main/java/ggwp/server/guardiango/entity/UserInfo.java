package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private String status;
    private String groupKey;
    private String userEmail;
    private String phoneNumber;
    private String userName;

    public UserInfo() {}

    public UserInfo(String groupKey, String userEmail, String phoneNumber, String userName) {
        this.groupKey = groupKey;
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }
}