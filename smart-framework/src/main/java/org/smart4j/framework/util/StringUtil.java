package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhaoshiqiang on 2016/11/5.
 */
public final class StringUtil {

    public static boolean isEmpty(String str){
        if (str != null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String strValue) {
        return !isEmpty(strValue);
    }

    public static String[] splitString(String str,String separator){
        return StringUtils.splitByWholeSeparator(str,separator);
    }
}
