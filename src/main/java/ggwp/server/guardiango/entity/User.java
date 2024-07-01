package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

// 사용자 객체
@Getter
@Setter
public class User {
    private String userEmail;
    private String password;
    private String phoneNumber;
    private String userName;
    private String groupKey;

    public User() {}

    public User(String userEmail, String userName, String phoneNumber, String password) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userEmail = userEmail;
        this.password = password;
    }
}