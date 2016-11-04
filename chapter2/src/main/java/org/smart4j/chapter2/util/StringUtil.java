package org.smart4j.chapter2.util;

/**
 * Created by zhaoshiqiang on 2016/11/4.
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        if (str != null){
            str = str.trim();
        }
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String strValue) {
        return !isEmpty(strValue);
    }
}
