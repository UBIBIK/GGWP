package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UserInfo {
    private String status;
    private String groupKey;
    private String userEmail;
    private String phoneNumber;
    private String userName;
    private Map<String, Object> locationInfo = new HashMap<>();

    public UserInfo() {}

    public UserInfo(String groupKey, String userEmail, String phoneNumber, String userName) {
        this.groupKey = groupKey;
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }
}
