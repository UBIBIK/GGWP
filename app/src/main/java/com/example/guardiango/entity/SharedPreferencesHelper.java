package com.example.guardiango.entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "user_prefs";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveUserInfo(UserInfo userInfo) {
        String json = gson.toJson(userInfo);
        sharedPreferences.edit().putString("userInfo", json).apply();
    }

    public UserInfo getUserInfo() {
        String json = sharedPreferences.getString("userInfo", null);
        return gson.fromJson(json, UserInfo.class);
    }

    public void clearUserInfo() {
        sharedPreferences.edit().remove("userInfo").apply();
    }
}
