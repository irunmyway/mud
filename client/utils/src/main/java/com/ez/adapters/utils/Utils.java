package com.ez.adapters.utils;

import java.util.Collection;

/**
 * 比较杂乱的工具类,dp转px,返回2位小数,是否是空集合等
 */
public final class Utils {

    //集合是否是空的
    public static boolean isEmptyArray(Collection list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isEmptyArray(T[] list) {
        return list == null || list.length == 0;
    }
}
