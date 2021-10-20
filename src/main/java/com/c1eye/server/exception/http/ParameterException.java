package com.c1eye.server.exception.http;

/**
 * @author c1eye
 * time 2021/10/15 10:38
 */
public class ParameterException extends HttpException{
    public ParameterException(int code) {
        this.code = code;
        this.httpStatusCode = 400;
    }
}
