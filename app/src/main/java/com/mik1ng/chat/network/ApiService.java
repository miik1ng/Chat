package com.mik1ng.chat.network;

import com.mik1ng.chat.entity.AgreeEntity;
import com.mik1ng.chat.entity.FriendApplyEntity;
import com.mik1ng.chat.entity.GetFriendListEntity;
import com.mik1ng.chat.entity.LoginEntity;
import com.mik1ng.chat.entity.RegisterEntity;
import com.mik1ng.chat.entity.SearchUserEntity;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 注册
     *
     * @param requestBody
     * @return
     */
    @POST("fit/user/register")
    Observable<RegisterEntity> register(@Body RequestBody requestBody);

    /**
     * 登录
     *
     * @param requestBody
     * @return
     */
    @POST("fit/user/login")
    Observable<LoginEntity> login(@Body RequestBody requestBody);

    /**
     * 查找用户
     *
     * @return
     */
    @POST("fit/user/searchUser")
    Observable<SearchUserEntity> searchUser(@Query("username") String username);

    @POST("fit/user/searchUserById")
    Observable<SearchUserEntity> searchUser(@Query("userId") int userID);

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @GET("fit/user/myInformation")
    Observable<SearchUserEntity> getMyInfo();

    /**
     * 获取好友列表
     *
     * @return
     */
    @POST("fit/friend/getFriendByUserId")
    Observable<GetFriendListEntity> getFriends();

    /**
     * 获取好友申请列表
     *
     * @return
     */
    @GET("fit/friend/getApplyList")
    Observable<FriendApplyEntity> getFriendApplyList();

    /**
     * 同意
     * @param requestBody
     * @return
     */
    @POST("fit/friend/confirm")
    Observable<AgreeEntity> agree(@Body RequestBody requestBody);
}
