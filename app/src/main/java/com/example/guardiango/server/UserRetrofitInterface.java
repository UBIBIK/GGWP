package com.example.guardiango.server;

import com.example.guardiango.entity.Element;
import com.example.guardiango.entity.Group;
import com.example.guardiango.entity.Report;
import com.example.guardiango.entity.UserDTO;
import com.example.guardiango.entity.UserInfo;
import com.example.guardiango.entity.UserReport;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @POST("get-element")
    Call<Element> getElementData(@Body UserInfo user);

    @Multipart
    @POST("upload-postdata")
    Call<ResponseBody> uploadPostData(
            @Part MultipartBody.Part image,
            @Part("PostData") RequestBody PostData);

    @GET("/uploads/{groupKey}/{uuid}")
    Call<ResponseBody> getImage(@Path("groupKey") String groupKey, @Path("uuid") String uuid);
}
