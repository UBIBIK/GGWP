package com.example.guardiango.server;

import com.example.guardiango.entity.Groupcode;
import com.example.guardiango.entity.Groupcreate;
import com.example.guardiango.entity.UserDTO;
import com.example.guardiango.entity.UserInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserRetrofitInterface {
    @POST("save-user")
    Call<ResponseBody> saveUser(@Body UserDTO jsonUser);

    @POST("login")
    Call<UserInfo> loginUser(@Body UserDTO userDTO);

    @POST("group-create")
    Call<ResponseBody> groupCreate(@Body Groupcreate userEmail);

    @POST("group-join")
    Call<ResponseBody> groupjoin(@Body Groupcode groupCode);

    @POST("get-user-group")
    Call<ResponseBody> getUserGroups(@Body String groupKey);
}
