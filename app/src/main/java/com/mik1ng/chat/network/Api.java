package com.mik1ng.chat.network;

import com.google.gson.Gson;
import com.mik1ng.chat.entity.GetFriendListEntity;
import com.mik1ng.chat.entity.LoginEntity;
import com.mik1ng.chat.entity.RegisterEntity;
import com.mik1ng.chat.entity.SearchUserEntity;
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

    private static RequestBody init(HashMap map) {
        MediaType mediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
        String json = new Gson().toJson(map);
        return RequestBody.create(mediaType, json);
    }

    /**
     * 注册
     * @param map
     * @return
     */
    public static Observable<RegisterEntity> register(HashMap map) {
        return apiService
                .register(init(map))
                .subscribeOn(Schedulers.io())
                .compose(NetworkApi.applySchedulers())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 登录
     * @param map
     * @return
     */
    public static Observable<LoginEntity> login(HashMap map) {
        return apiService
                .login(init(map))
                .subscribeOn(Schedulers.io())
                .compose(NetworkApi.applySchedulers())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查找用户
     * @return
     */
    public static Observable<SearchUserEntity> searchUser(String username) {
        return apiService
                .searchUser(username)
                .subscribeOn(Schedulers.io())
                .compose(NetworkApi.applySchedulers())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取当前用户信息
     * @return
     */
    public static Observable<SearchUserEntity> getMyInfo() {
        return apiService
                .getMyInfo()
                .subscribeOn(Schedulers.io())
                .compose(NetworkApi.applySchedulers())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取好友列表
     * @return
     */
    public static Observable<GetFriendListEntity> getFriends() {
        return apiService
                .getFriends()
                .subscribeOn(Schedulers.io())
                .compose(NetworkApi.applySchedulers())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
