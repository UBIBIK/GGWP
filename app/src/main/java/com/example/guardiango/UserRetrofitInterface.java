package com.example.guardiango;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserRetrofitInterface {
    @POST("save-user")
    Call<ResponseBody> saveUser(@Body UserDTO jsonUser);
}
