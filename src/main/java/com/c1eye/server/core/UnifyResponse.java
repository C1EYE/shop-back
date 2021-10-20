package com.c1eye.server.core;

import com.c1eye.server.exception.CreateSuccess;
import lombok.Getter;
import lombok.Setter;

/**
 * author c1eye
 * time 2021/9/28 09:47
 */
@Getter
@Setter
public class UnifyResponse {
    private int code;
    private String message;
    private String request;

    public UnifyResponse(int code, String message, String request) {
        this.code = code;
        this.message = message;
        this.request = request;
    }

    public static void createSuccess(int i) {
        throw new CreateSuccess(0);
    }
}
