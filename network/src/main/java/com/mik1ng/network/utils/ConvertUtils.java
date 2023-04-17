package com.mik1ng.network.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.FloatRange;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Response;

/**
 * @Describe 转换相关工具类
 */

public class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 16 进制
     */
    private static final char lexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    /**
     * 解析错误信息 des to see {@see https://slimkit.github.io/plus-docs/v2/#messages }
     *
     * @param response1 需要解析的数据，json str
     * @return 抛给用户的 message
     */
    public static String praseErrorMessage(String response1) {
        String message = "";

        Map<String, Object> errorMessageMap = new Gson().fromJson(response1,
                new TypeToken<Map<String, Object>>() {
                }.getType());
        for (Map.Entry<String, Object> entry : errorMessageMap.entrySet()) {

            if (entry.getValue() instanceof String) {
                if ("message".equals(entry.getKey())) {
                    message = (String) entry.getValue();
                }
            } else if (entry.getValue() instanceof Map) {
                if ("errors".equals(entry.getKey())) {
                    message = praseErrorMessage((new Gson().toJson(entry.getValue())));
                }
            } else if (entry.getValue() instanceof String[]) {
                try {
                    message = ((String[]) entry.getValue())[0];
                } catch (Exception ignored) {
                }
                break;
            } else if (entry.getValue() instanceof List) {
                try {
                    message = (String) ((List) entry.getValue()).get(0);
                } catch (Exception ignored) {
                }
                break;
            }
        }
        return message;

    }

    /**
     * 获取网络返回数据体内容
     *
     * @param response 返回体
     * @return
     */
    public static String getResponseBodyString(Response response) throws IOException {
        ResponseBody responseBody = response.errorBody();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        //获取content的压缩类型
        String encoding = response
                .headers()
                .get("Content-Encoding");
        Buffer clone = buffer.clone();
        return praseBodyString(responseBody, encoding, clone);
    }

    /**
     * 解析返回体数据内容
     *
     * @param responseBody 返回体
     * @param encoding     编码
     * @param clone        数据
     * @return
     */
    public static String praseBodyString(ResponseBody responseBody, String encoding, Buffer clone) {
        String bodyString;//解析response content
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
            bodyString = ZipHelper.decompressForGzip(clone.readByteArray());//解压
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
            bodyString = ZipHelper.decompressToStringForZlib(clone.readByteArray());//解压
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }
        return bodyString;
    }

    public static <T> T base64Str2Object(String productBase64) {
        T device = null;
        if (productBase64 == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象
            device = (T) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return device;
    }

    public static <T> String object2Base64Str(T object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {   //Device为自定义类
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符串
            return new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
