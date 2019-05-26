package com.kyoshii.util;
import com.sun.istack.internal.NotNull;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description:
 */
public class StringUtil {
    public static final String SEPARATOR = String.valueOf((char) 29);

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
            return str.isEmpty();
        }
        return true;
    }

    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 分割固定格式的字符串
     */
    public static String[] splitString(@NotNull String str, String separator) {
        return str.split(separator);
    }

}
