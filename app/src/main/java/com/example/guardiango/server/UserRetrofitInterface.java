package com.example.guardiango.server;

import com.example.guardiango.entity.Groupcode;
import com.example.guardiango.entity.Groupname;
import com.example.guardiango.entity.UserDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserRetrofitInterface {
    @POST("save-user")
    Call<ResponseBody> saveUser(@Body UserDTO jsonUser);

    @POST("login")
    Call<ResponseBody> loginUser(@Body UserDTO userDTO);

    @POST("group-create")
    Call<ResponseBody> groupCreate(@Body Groupname groupName);

    @POST("group-join")
    Call<ResponseBody> groupjoin(@Body Groupcode groupCode);
}
