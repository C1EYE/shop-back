package com.c1eye.server.core;

import com.c1eye.server.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author c1eye
 * time 2021/10/18 21:45
 */
public class LocalUser {
    private static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();
    public static void set(User user,Integer scope){
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("scope", scope);
        LocalUser.threadLocal.set(map);
    }

    public static User getUser(){
        Map<String, Object> map = LocalUser.threadLocal.get();
        User user = (User) map.get("user");
        return user;
    }

    public static Integer getScope(){
        Map<String, Object> map = LocalUser.threadLocal.get();
        Integer scope = (Integer) map.get("scope");
        return scope;
    }

    public static void clear(){
        LocalUser.threadLocal.remove();
    }
}
