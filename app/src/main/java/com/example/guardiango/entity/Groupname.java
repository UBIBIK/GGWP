package com.example.guardiango.entity;

public class Groupname {
    private String groupName;
    public Groupname(String groupName) {
        this.groupName = groupName;
    }

    public String getGroup_reader_name() {
        return groupName;
    }

    public void setGroup_reader_name(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Group_name{" +
                "Group_reader_name='" + groupName + '\'' +
                '}';
    }
}
