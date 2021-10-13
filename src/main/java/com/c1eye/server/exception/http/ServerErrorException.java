package com.c1eye.server.exception.http;

/**
 * @author c1eye
 * time 2021/10/11 10:20
 */
public class ServerErrorException extends HttpException{
    public ServerErrorException(int code) {
        this.code = code;
        this.httpStatusCode = 500;
    }
}
