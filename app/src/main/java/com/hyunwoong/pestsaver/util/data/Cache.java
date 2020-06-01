package com.hyunwoong.pestsaver.util.data;

import com.hyunwoong.pestsaver.core.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Hyunwoong
 * @when : 2019-11-15 오후 7:23
 * @homepage : https://github.com/gusdnd852
 */
@SuppressWarnings("unchecked")
public class Cache {
    private static User user = new User();
    private static Map<String, Float> results = new HashMap<>();

    public static void copyUser(User user) {
        Cache.user = user;
    }

    public static void copyResult(Map<String, Float> results) {
        Cache.results = results;
    }

    public static User readUser() {
        return Cache.user;
    }

    public static Map<String, Float> readResults() {
        return Cache.results;
    }
}
