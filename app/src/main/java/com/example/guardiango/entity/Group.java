package com.example.guardiango.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {
    private String groupName;
    private String groupKey;
    private String groupMaster;
    private ArrayList<Map<String, Object>> groupMember = new ArrayList<>();

    public Group() {}

    public ArrayList<Map<String, Object>> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(ArrayList<Map<String, Object>> groupMember) {
        this.groupMember = groupMember;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupMaster() {
        return groupMaster;
    }

    public void setGroupMaster(String groupMaster) {
        this.groupMaster = groupMaster;
    }
}