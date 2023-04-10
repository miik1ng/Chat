package com.mik1ng.chat.util;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class CharUtils {

    /**
     * 获取字符串大写首字母
     * @param chinese
     * @return
     */
    public static String getFirstCharFromChinese(String chinese) {
        if (TextUtils.isEmpty(chinese)) {
            return null;
        }

        String firstChar = "#";
        if (chinese.matches("^[\u4e00-\u9fa5].*")) {
            // 首位为汉字
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            String[] pinyinArray = new String[0];
            try {
                pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(0), format);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
            firstChar = pinyinArray[0].substring(0, 1).toUpperCase();
        }

        if (chinese.matches("^[a-zA-Z].*")) {
            //首位为字母
            firstChar = chinese.substring(0, 1).toUpperCase();
        }

        return firstChar;
    }
}
