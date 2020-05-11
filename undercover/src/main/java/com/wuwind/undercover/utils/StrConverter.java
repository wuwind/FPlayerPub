package com.wuwind.undercover.utils;

/**
 * Created by wuhf on 2020/5/11.
 * Description ：
 */
public class StrConverter {

    public static byte[] toByteArray(String str) {
        if (null == str || str.length() < 1)
            return null;
        byte[] sequence = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            sequence[i] = (byte) (str.charAt(i) - 48);
        }
        return sequence;
    }
}
