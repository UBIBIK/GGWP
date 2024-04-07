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
    private String[] group;
    private String[] role;

    public User() {

    }
}