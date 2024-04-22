package com.example.guardiango.server;

import com.example.guardiango.entity.Group;
import com.example.guardiango.entity.LocationData;
import com.example.guardiango.entity.UserDTO;
import com.example.guardiango.entity.UserInfo;

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
    Call<Group> groupCreate(@Body UserInfo user);

    @POST("group-join")
    Call<Group> groupJoin(@Body UserInfo user);

    @POST("group-exist")
    Call<Group> getUserGroups(@Body UserInfo user);

    @POST("group-delete")
    Call<ResponseBody> groupDelete(@Body UserInfo user);

    @POST("group-member-delete")
    Call<Group> groupUserDelete(@Body String deleteUserName);

    @POST("save-location")
    Call<Group> updateLocation(@Body UserInfo user);
}
