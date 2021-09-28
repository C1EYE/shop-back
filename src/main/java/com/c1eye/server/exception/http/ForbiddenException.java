package com.c1eye.server.exception.http;

/**
 * @author c1eye
 */
public class ForbiddenException extends HttpException{
    public ForbiddenException(int code) {
        this.httpStatusCode = 403;
        this.code = code;
    }
}
