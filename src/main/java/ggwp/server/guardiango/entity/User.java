package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String email;
    private String password;
    private String phoneNumber;
    private String name;

    public User() {

    }

    public User(String name, String phoneNumber, String email, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}