package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String user_email;
    private String password;
    private String phone_number;
    private String user_name;

    public User() {

    }

    public User(String user_name, String phone_number, String user_email, String password) {
        this.user_name = user_name;
        this.phone_number = phone_number;
        this.user_email = user_email;
        this.password = password;
    }
}