package com.c1eye.server.core.enumeration;

/**
 * @author c1eye
 * time 2021/10/14 11:22
 */
public enum LoginType {
    USER_WX(0, "微信登录"), USER_Email(1, "邮箱登录");

    LoginType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 枚举值
     */
    Integer value;
    /**
     * 登录方式
     */
    String description;
}
