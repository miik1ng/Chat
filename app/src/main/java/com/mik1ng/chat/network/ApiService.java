package com.mik1ng.chat.network;

import com.mik1ng.chat.entity.GetUsersEntity;
import com.mik1ng.chat.entity.RegisterEntity;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("fit/user/list")
    Observable<GetUsersEntity> getUsers();

    @POST("fit/user/register")
    Observable<RegisterEntity> register(@Body RequestBody requestBody);
}
