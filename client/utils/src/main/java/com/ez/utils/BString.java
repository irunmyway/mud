package com.ez.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BString {

    /**
     * Regex of simple mobile.
     */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * Regex of exact mobile.
     * <p>china mobile: 134(0-8), 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 198</p>
     * <p>china unicom: 130, 131, 132, 145, 155, 156, 166, 171, 175, 176, 185, 186</p>
     * <p>china telecom: 133, 153, 173, 177, 180, 181, 189, 199</p>
     * <p>global star: 1349</p>
     * <p>virtual operator: 170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
    /**
     * Regex of telephone number.
     */
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    /**
     * Regex of id card number which length is 15.
     */
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * Regex of id card number which length is 18.
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * Regex of email.
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * Regex of url.
     */
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    /**
     * Regex of Chinese character.
     */
    public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    /**
     * Regex of username.
     * <p>scope for "a-z", "A-Z", "0-9", "_", "Chinese character"</p>
     * <p>can't end with "_"</p>
     * <p>length is between 6 to 20</p>
     */
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * Regex of date which pattern is "yyyy-MM-dd".
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * Regex of ip address.
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    ///////////////////////////////////////////////////////////////////////////
    // The following come from http://tool.oschina.net/regex
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Regex of double-byte characters.
     */
    public static final String REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";
    /**
     * Regex of blank line.
     */
    public static final String REGEX_BLANK_LINE = "\\n\\s*\\r";
    /**
     * Regex of QQ number.
     */
    public static final String REGEX_QQ_NUM = "[1-9][0-9]{4,}";
    /**
     * Regex of postal code in China.
     */
    public static final String REGEX_CHINA_POSTAL_CODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * Regex of positive integer.
     */
    public static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";
    /**
     * Regex of negative integer.
     */
    public static final String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /**
     * Regex of integer.
     */
    public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";
    /**
     * Regex of non-negative integer.
     */
    public static final String REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$";
    /**
     * Regex of non-positive integer.
     */
    public static final String REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$";
    /**
     * Regex of positive float.
     */
    public static final String REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /**
     * Regex of negative float.
     */
    public static final String REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

    ///////////////////////////////////////////////////////////////////////////
    // If u want more please visit http://toutiao.com/i6231678548520731137
    ///////////////////////////////////////////////////////////////////////////


    public static String trim(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim());
    }

    public static String firstLetterToLowerCase(String str) {
        return toLowerCase(str, 0, 1);
    }

    public static String firstLetterToUpperCase(String str) {
        return toUpperCase(str, 0, 1);
    }

    //将给定字符串中给定的区域的字符转换成小写
    public static String toLowerCase(String str, int beginIndex, int endIndex) {
        return str.replaceFirst(str.substring(beginIndex, endIndex),
                str.substring(beginIndex, endIndex)
                        .toLowerCase(Locale.getDefault()));
    }

    //将给定字符串中给定的区域的字符转换成大写
    public static String toUpperCase(String str, int beginIndex, int endIndex) {
        return str.replaceFirst(str.substring(beginIndex, endIndex),
                str.substring(beginIndex, endIndex)
                        .toUpperCase(Locale.getDefault()));
    }

    //计算给定的字符串的长度，计算规则是：一个汉字的长度为2，一个字符的长度为1
    public static int countLength(String string) {
        int length = 0;
        char[] chars = string.toCharArray();
        for (int w = 0; w < string.length(); w++) {
            char ch = chars[w];
            if (ch >= '\u0391' && ch <= '\uFFE5') {
                length++;
                length++;
            } else {
                length++;
            }
        }
        return length;
    }

    //在给定的字符串中，用新的字符替换所有旧的字符
    public static String replace(String string, char oldchar, char newchar) {
        char[] chars = string.toCharArray();
        for (int w = 0; w < chars.length; w++) {
            if (chars[w] == oldchar) {
                chars[w] = newchar;
                break;
            }
        }
        return new String(chars);
    }

    public static boolean isNumber(char[] chars) {
        boolean result = true;
        for (int w = 0; w < chars.length; w++) {
            if (!Character.isDigit(chars[w])) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static Boolean isNumber(String str) {
        Boolean isNumber = false;
        String expr = "^[0-9]+$";
        if (str.matches(expr)) {
            isNumber = true;
        }
        return isNumber;
    }

    //是否是中文
    public static Boolean isChinese(String str) {
        Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                //获取一个字符
                String temp = str.substring(i, i + 1);
                //判断是否为中文字符
                if (temp.matches(chinese)) {
                } else {
                    isChinese = false;
                }
            }
        }
        return isChinese;
    }

    //是否包含中文
    public static Boolean isContainChinese(String str) {
        Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                //获取一个字符
                String temp = str.substring(i, i + 1);
                //判断是否为中文字符
                if (temp.matches(chinese)) {
                    isChinese = true;
                } else {

                }
            }
        }
        return isChinese;
    }

    public static String utf8Encode(String str) {

        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * 判断给定的字符串是否以一个特定的字符串开头，忽略大小写
     *
     * @param sourceString 给定的字符串
     * @param newString    一个特定的字符串
     */
    public static boolean startsWithIgnoreCase(String sourceString, String newString) {
        int newLength = newString.length();
        int sourceLength = sourceString.length();
        if (newLength == sourceLength) {
            return newString.equalsIgnoreCase(sourceString);
        } else if (newLength < sourceLength) {
            char[] newChars = new char[newLength];
            sourceString.getChars(0, newLength, newChars, 0);
            return newString.equalsIgnoreCase(String.valueOf(newChars));
        } else {
            return false;
        }
    }

    /**
     * 判断给定的字符串是否以一个特定的字符串结尾，忽略大小写
     *
     * @param sourceString 给定的字符串
     * @param newString    一个特定的字符串
     */
    public static boolean endsWithIgnoreCase(String sourceString, String newString) {
        int newLength = newString.length();
        int sourceLength = sourceString.length();
        if (newLength == sourceLength) {
            return newString.equalsIgnoreCase(sourceString);
        } else if (newLength < sourceLength) {
            char[] newChars = new char[newLength];
            sourceString.getChars(sourceLength - newLength, sourceLength,
                    newChars, 0);
            return newString.equalsIgnoreCase(String.valueOf(newChars));
        } else {
            return false;
        }
    }

    //是否只是字母和数字.
    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    //是否包含中文数字字母的用户名
    public static boolean isConintChinseUser(String str) {
        /**此正则表达式将上面二者结合起来进行判断，中文、大小写字母和数字**/
        String all = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{2,10}$";
        Pattern pattern = Pattern.compile(all);
        return pattern.matcher(str).matches();
    }

    public static Boolean isMobileNo(String str) {
        Boolean isMobileNo = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMobileNo;
    }

    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    public static boolean isTel(final CharSequence input) {
        return isMatch(REGEX_TEL, input);
    }

    public static boolean isIDCard15(final CharSequence input) {
        return isMatch(REGEX_ID_CARD15, input);
    }

    public static boolean isIDCard18(final CharSequence input) {
        return isMatch(REGEX_ID_CARD18, input);
    }

    public static boolean isEmail(final CharSequence input) {
        return isMatch(REGEX_EMAIL, input);
    }

    public static boolean isURL(final CharSequence input) {
        return isMatch(REGEX_URL, input);
    }

    //验证汉字
    public static boolean isZh(final CharSequence input) {
        return isMatch(REGEX_ZH, input);
    }

    public static boolean isIP(final CharSequence input) {
        return isMatch(REGEX_IP, input);
    }

    //"a-z", "A-Z", "0-9", "_"
    public static boolean isUsername(final CharSequence input) {
        return isMatch(REGEX_USERNAME, input);
    }

    //验证 yyyy-MM-dd 格式的日期校验，已考虑平闰年.
    public static boolean isDate(final CharSequence input) {
        return isMatch(REGEX_DATE, input);
    }

    //判断是否匹配正则
    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    //取两个文本之间的文本值
    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) yLen = text.length();
        result = text.substring(zLen, yLen);
        return result;
    }

    /**
     * 返回首字母
     */
    public static String getPYIndexStr(String strChinese, boolean bUpCase) {
        try {
            StringBuffer buffer = new StringBuffer();
            byte[] b = strChinese.getBytes("GBK");//把中文转化成byte数组
            for (int i = 0; i < b.length; i++) {
                if ((b[i] & 255) > 128) {
                    int char1 = b[i++] & 255;
                    char1 <<= 8;//左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方
                    int chart = char1 + (b[i] & 255);
                    buffer.append(getPYIndexChar((char) chart, bUpCase));
                    continue;
                }
                char c = (char) b[i];
                if (!Character.isJavaIdentifierPart(c))//确定指定字符是否可以是 Java 标识符中首字符以外的部分。
                    c = 'A';
                buffer.append(c);
            }
            return buffer.toString();
        } catch (Exception e) {
            System.out.println((new StringBuilder()).append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519").append(e.getMessage()).toString());
        }
        return null;
    }

    /**
     * 得到汉字首字母
     */
    private static char getPYIndexChar(char strChinese, boolean bUpCase) {
        int charGBK = strChinese;
        char result;
        if (charGBK >= 45217 && charGBK <= 45252) result = 'A';
        else if (charGBK >= 45253 && charGBK <= 45760) result = 'B';
        else if (charGBK >= 45761 && charGBK <= 46317) result = 'C';
        else if (charGBK >= 46318 && charGBK <= 46825) result = 'D';
        else if (charGBK >= 46826 && charGBK <= 47009) result = 'E';
        else if (charGBK >= 47010 && charGBK <= 47296) result = 'F';
        else if (charGBK >= 47297 && charGBK <= 47613) result = 'G';
        else if (charGBK >= 47614 && charGBK <= 48118) result = 'H';
        else if (charGBK >= 48119 && charGBK <= 49061) result = 'J';
        else if (charGBK >= 49062 && charGBK <= 49323) result = 'K';
        else if (charGBK >= 49324 && charGBK <= 49895) result = 'L';
        else if (charGBK >= 49896 && charGBK <= 50370) result = 'M';
        else if (charGBK >= 50371 && charGBK <= 50613) result = 'N';
        else if (charGBK >= 50614 && charGBK <= 50621) result = 'O';
        else if (charGBK >= 50622 && charGBK <= 50905) result = 'P';
        else if (charGBK >= 50906 && charGBK <= 51386) result = 'Q';
        else if (charGBK >= 51387 && charGBK <= 51445) result = 'R';
        else if (charGBK >= 51446 && charGBK <= 52217) result = 'S';
        else if (charGBK >= 52218 && charGBK <= 52697) result = 'T';
        else if (charGBK >= 52698 && charGBK <= 52979) result = 'W';
        else if (charGBK >= 52980 && charGBK <= 53688) result = 'X';
        else if (charGBK >= 53689 && charGBK <= 54480) result = 'Y';
        else if (charGBK >= 54481 && charGBK <= 55289) result = 'Z';
        else result = (char) (65 + (new Random()).nextInt(25));
        if (!bUpCase) result = Character.toLowerCase(result);
        return result;
    }

    //效验
    public static boolean isSql(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|master|truncate|" +
                "declare|sitename|net user|xp_cmdshell|;|-|+|,|like'|create|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|count|*|" +
                "master|truncate|declare|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

}
