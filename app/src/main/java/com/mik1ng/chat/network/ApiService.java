package com.mik1ng.chat.network;

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
     * @param requestBody
     * @return
     */
    @POST("fit/user/register")
    Observable<RegisterEntity> register(@Body RequestBody requestBody);

    /**
     * 登录
     * @param requestBody
     * @return
     */
    @POST("fit/user/login")
    Observable<LoginEntity> login(@Body RequestBody requestBody);

    /**
     * 查找用户
     * @return
     */
    @POST("fit/user/searchUser")
    Observable<SearchUserEntity> searchUser(@Query("username") String username);

    /**
     * 获取当前用户信息
     * @return
     */
    @GET("fit/user/myInformation")
    Observable<SearchUserEntity> getMyInfo();
}
