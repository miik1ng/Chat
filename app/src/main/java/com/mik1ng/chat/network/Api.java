package com.mik1ng.chat.network;

import com.google.gson.Gson;
import com.mik1ng.chat.entity.GetUsersEntity;
import com.mik1ng.chat.entity.RegisterEntity;
import com.mik1ng.network.NetworkApi;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@SuppressWarnings("rawtypes")
public class Api {

    static ApiService apiService = NetworkApi.createService(ApiService.class);

    public static Observable<GetUsersEntity> getUsers() {
        return apiService
                .getUsers()
                .subscribeOn(Schedulers.io())
                .compose(NetworkApi.applySchedulers())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<RegisterEntity> register(HashMap map) {
        return apiService
                .register(init(map))
                .subscribeOn(Schedulers.io())
                .compose(NetworkApi.applySchedulers())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static RequestBody init(HashMap map) {
        MediaType mediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
        String json = new Gson().toJson(map);
        return RequestBody.create(mediaType, json);
    }
}
