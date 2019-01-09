package com.nemo.game.util;

public class StringUtil {
    //ip正则表达式
    public static final String IP_REGEX = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\."
            + "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";


    //去掉前后空格是否空字符串
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }


}
