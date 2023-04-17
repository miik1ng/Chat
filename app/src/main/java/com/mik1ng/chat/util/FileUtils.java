package com.mik1ng.chat.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    /**
     * 将base64转为文件保存并返回地址
     * @param context
     * @param base64
     * @param fileType
     * @return
     */
    public static String createFileWithBase64(Context context, String base64, String fileType) {
        //将base64转位byte数组
        if (TextUtils.isEmpty(base64)) {
            return null;
        }
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        //创建文件
        String fileName = String.valueOf(DateUtils.getTimeStamp()) + fileType;
        File file = new File(context.getFilesDir(), fileName);
        String path = context.getFilesDir() + "/" + fileName;
        //创建FileOutputStream对象
        FileOutputStream outputStream = null;
        //创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            //如果目录文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            //在文件系统中创建一个新的空文件
            file.createNewFile();
            //获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            //获取BufferdOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            //向文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }

    /**
     * 将文件转为base64
     * @param path
     * @return
     */
    public static String createBase64WithFile(String path) {
        //将图片转位base64
        File file = new File(path);
        byte[] bytes = new byte[(int) file.length()];
        String base64 = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64;
    }
}
