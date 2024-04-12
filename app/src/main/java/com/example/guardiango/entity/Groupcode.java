package com.example.guardiango.entity;

public class Groupcode {
    private String groupCode;

    public Groupcode(String code) {this.groupCode = code;}

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public String toString() {
        return "Groupcode{" +
                "groupCode='" + groupCode + '\'' +
                '}';
    }
}
