package com.hyunwoong.pestsaver.util.others;

/**
 * @author : Hyunwoong
 * @when : 2019-11-18 오후 4:32
 * @homepage : https://github.com/gusdnd852
 */
public class Strings {

    public static String key(String key) {
        return key.replaceAll("\\.", "")
                .replaceAll(" ", "");
    }

    public static boolean empty(String str) {
        return str == null || str.replaceAll(" ", "").equals("");
    }
}