package com.ez.utils;

import java.util.ArrayList;

public class BDebug {

    public static boolean USE = true;

    /**
     * 打印任意类型，包括一唯数组
     *
     * @param args
     */
    public static void trace(Object... args) {
        if (USE == false) return;

        if (args == null) {
            BDebug.trace("null");
            return;
        }

        String res = "";
        for (int i = 0; i < args.length; i++) {
            Object val = args[i];
            if (val == null) {
                val = "null";
            }

            if (val instanceof String[]) {
                res += (i > 0 ? " ," : "") + getStringArr((String[]) val);

            } else if (val instanceof int[]) {
                res += (i > 0 ? " ," : "") + getNumberArr((int[]) val);

            } else if (val instanceof ArrayList) {
                res += (i > 0 ? " ," : "") + getArrayList((ArrayList) val);

            } else {
                res += (i > 0 ? " ," : "") + val.toString();

            }
        }

        System.out.println(res);
    }


    private static String getNumberArr(int[] strArr) {
        String strTemp = "";
        for (int i = 0; i < strArr.length; i++) {
            strTemp += (i > 0 ? " ," : "") + strArr[i] + "";
        }
        return strTemp;
    }

    private static String getStringArr(Object[] strArr) {
        String strTemp = "";
        for (int i = 0; i < strArr.length; i++) {
            strTemp += (i > 0 ? " ," : "") + strArr[i].toString();
        }
        return strTemp;
    }

    private static String getArrayList(ArrayList listArr) {
        String listTemp = "";
        for (int i = 0; i < listArr.size(); i++) {
            listTemp += (i > 0 ? " ," : "") + listArr.get(i);
        }

        return listTemp;
    }
}
