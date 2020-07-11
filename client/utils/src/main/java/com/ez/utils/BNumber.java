package com.ez.utils;

import java.math.BigDecimal;

public class BNumber {
    private static final String[] CN_NUM = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] CN_UNIT = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
    private static final String CN_NEGATIVE = "负";
    private static final String CN_POINT = "点";

    //四舍五入
    public static BigDecimal round(double number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP);
    }

    //整数转中文
    public static String int2Num(int intNum) {
        StringBuffer sb = new StringBuffer();
        boolean isNegative = false;
        if (intNum < 0) {
            isNegative = true;
            intNum *= -1;
        }
        int count = 0;
        while (intNum > 0) {
            sb.insert(0, CN_NUM[intNum % 10] + CN_UNIT[count]);
            intNum = intNum / 10;
            count++;
        }
        if (isNegative) sb.insert(0, CN_NEGATIVE);
        return sb.toString().replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }

    //小数转中文
    public static String text2Num(Object obj) {
        BigDecimal bigDecimalNum = new BigDecimal(0);
        if (obj instanceof Double)
            bigDecimalNum = new BigDecimal((Double) obj);
        if (obj instanceof Integer)
            bigDecimalNum = new BigDecimal((Integer) obj);
        if (obj instanceof String)
            bigDecimalNum = new BigDecimal((String) obj);
        if (obj instanceof Long)
            bigDecimalNum = new BigDecimal((Long) obj);
        if (obj instanceof Float)
            bigDecimalNum = new BigDecimal((Float) obj);
        if (bigDecimalNum == null)
            return CN_NUM[0];
        StringBuffer sb = new StringBuffer();
        //将小数点后面的零给去除
        String numStr = bigDecimalNum.abs().stripTrailingZeros().toPlainString();
        String[] split = numStr.split("\\.");
        String integerStr = int2Num(Integer.parseInt(split[0]));
        sb.append(integerStr);
        //如果传入的数有小数，则进行切割，将整数与小数部分分离
        if (split.length == 2) {
            //有小数部分
            sb.append(CN_POINT);
            String decimalStr = split[1];
            char[] chars = decimalStr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                int index = Integer.parseInt(String.valueOf(chars[i]));
                sb.append(CN_NUM[index]);
            }
        }
        //判断传入数字为正数还是负数
        int signum = bigDecimalNum.signum();
        if (signum == -1) sb.insert(0, CN_NEGATIVE);
        return sb.toString();
    }
}
