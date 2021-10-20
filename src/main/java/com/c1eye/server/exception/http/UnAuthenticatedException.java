package com.c1eye.server.exception.http;

/**
 * @author c1eye
 * time 2021/10/16 17:11
 */
public class UnAuthenticatedException  extends HttpException{
    public UnAuthenticatedException(int code){
        this.code = code;
        this.httpStatusCode = 401;
    }
}
