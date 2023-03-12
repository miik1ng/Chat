package com.mik1ng.network.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    //获取当前完整的日期和时间
    public static String getNowDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date());
    }
}
