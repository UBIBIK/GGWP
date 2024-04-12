package com.example.guardiango;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private static UserRetrofitInterface userRetrofitInterface;

    //외부 호스트
    //private static String baseUrl = "http://34.64.241.204:8080/";

    //로컬 호스트
    private static String baseUrl = "http://10.0.2.2:8080/";

    private RetrofitClient() {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userRetrofitInterface = retrofit.create(UserRetrofitInterface.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public static UserRetrofitInterface getUserRetrofitInterface() {
        return userRetrofitInterface;
    }
}