package com.c1eye.server.exception;


import com.c1eye.server.exception.http.HttpException;

public class UpdateSuccess extends HttpException {
    public UpdateSuccess(int code){
        this.httpStatusCode = 200;
        this.code = code;
    }
//    201 202 204
}
