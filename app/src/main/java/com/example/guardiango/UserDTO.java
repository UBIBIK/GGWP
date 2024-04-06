package com.example.guardiango;

public class UserDTO {

    private String user_name;
    private String Phone_number;
    private String user_email;
    private String password;

    public UserDTO(String user_name, String Phone_number, String user_email, String password) {
        this.user_name = user_name;
        this.Phone_number = Phone_number;
        this.user_email = user_email;
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(String phone_number) {
        Phone_number = phone_number;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getPassphrase() {
        return password;
    }

    public void setPassphrase(String passphrase) {
        this.password = passphrase;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "user_name='" + user_name + '\'' +
                ", Phone_number='" + Phone_number + '\'' +
                ", user_email='" + user_email + '\'' +
                ", passphrase='" + password + '\'' +
                '}';
    }
}