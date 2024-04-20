package com.example.guardiango.entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferencesGroup {
    private static final String PREFS_NAME = "group_prefs";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesGroup(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveGroupInfo(Group group) {
        String json = gson.toJson(group);
        sharedPreferences.edit().putString("groupInfo", json).apply();
    }

    public Group getGroupInfo() {
        String json = sharedPreferences.getString("groupInfo", null);
        return gson.fromJson(json, Group.class);
    }

    public void clearGroupInfo() {
        sharedPreferences.edit().remove("groupInfo").apply();
    }
}
