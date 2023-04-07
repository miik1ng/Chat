package com.mik1ng.chat.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    /**
     * 获取系统时间戳
     * @return
     */
    public static long getTimeStamp(){
        return System.currentTimeMillis();
    }

    public static String getMmDdHhMmSs(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 HH:mm");//这里的格式可换"yyyy年-MM月dd日-HH时mm分ss秒"等等格式
        String date = sf.format(calendar.getTime());
        return date;
    }
}
