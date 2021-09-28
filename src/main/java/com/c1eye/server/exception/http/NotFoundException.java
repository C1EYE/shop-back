package com.c1eye.server.exception.http;

/**
 * @author c1eye
 */
public class NotFoundException extends HttpException {
    public NotFoundException(int code) {
        this.httpStatusCode = 404;
        this.code = code;
    }
}
