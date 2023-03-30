package com.mik1ng.network.interceptor;


import android.content.Context;

import com.mik1ng.network.INetworkRequiredInfo;
import com.mik1ng.network.utils.SharePreferenceConfig;
import com.mik1ng.network.utils.SharedPreferenceUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;

/**
 * 请求拦截器
 */
public class RequestInterceptor implements Interceptor {
    /**
     * 网络请求信息
     */
    private Context context;
    private INetworkRequiredInfo iNetworkRequiredInfo;

    public RequestInterceptor(Context context, INetworkRequiredInfo iNetworkRequiredInfo){
        this.context = context;
        this.iNetworkRequiredInfo = iNetworkRequiredInfo;
    }

    /**
     * 拦截
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = changeRequestMethod(chain);

        //String nowDateTime = DateUtil.getNowDateTime();
        //构建器
        //构建器
        Request.Builder builder = null;
        if (request != null) {
            builder = request.newBuilder();
        } else {
            builder = chain.request().newBuilder();
        }
        //添加使用环境
        builder.addHeader("os","android");
        //添加版本号
        builder.addHeader("Version-Code",this.iNetworkRequiredInfo.getAppVersionCode());
        //添加版本名
        builder.addHeader("Client-Version", this.iNetworkRequiredInfo.getAppVersionName());
        //添加日期时间
        //builder.addHeader("datetime",nowDateTime);
        builder.addHeader("Accept", "application/json");
        // token
        String token = SharedPreferenceUtils.getString(context, SharePreferenceConfig.SP_TOKEN);
        if (token != null) {
            builder.addHeader("token", token);
        }
        //返回
        return chain.proceed(builder.build());
    }

    private Request changeRequestMethod(Chain chain) {
        //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的 requeat 如增加 header,不做操作则返回 request
//        String uplaod = chain.request().header(ApiConfig.APP_PATH_STORAGE_HEADER_FLAG);

        Request request = chain.request();
        // 为 PATCH 请求添加方法，解决 cdn 不解析PATCH导致请求400问题
        String method = request.method();
        Request newRequest = null;
        long contentLength = 0;
        if ("PATCH".equals(method)
                || "DELETE".equals(method)
                || "PUT".equals(method)) {
            RequestBody requestBody = request.body();
            Buffer buffer = new Buffer();
            try {
                if (requestBody == null) {
                    requestBody = new RequestBody() {
                        @org.jetbrains.annotations.Nullable
                        @Override
                        public MediaType contentType() {
                            return MediaType.parse("application/json");
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    };
                }
                requestBody.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String oldParam = new String(buffer.readByteArray());
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(oldParam);
                jsonObject.put("_method", method);

                String result = jsonObject.toString();


                // 计算长度
                for(int i = 0; i < result.length(); i++) {
                    int ascii = Character.codePointAt(result, i);
                    if(ascii >= 0 && ascii <=255)
                        contentLength++;
                    else
                        contentLength += 3;

                }

//                        result = "[size=" + len + " text=" + result + "]";
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), result.getBytes());
                newRequest = request.newBuilder().post(requestBody).build();

                return newRequest;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
