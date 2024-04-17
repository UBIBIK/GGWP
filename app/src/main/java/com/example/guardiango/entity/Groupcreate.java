package com.example.guardiango.entity;

public class Groupcreate {
    private String userEmail;
    public Groupcreate(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String groupName) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "Group_name{" +
                "Group_reader_name='" + userEmail + '\'' +
                '}';
    }
}
