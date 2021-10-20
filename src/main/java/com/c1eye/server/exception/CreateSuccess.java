
package com.c1eye.server.exception;


import com.c1eye.server.exception.http.HttpException;

public class CreateSuccess extends HttpException {
    public CreateSuccess(int code){
        this.httpStatusCode = 201;
        this.code = code;
    }
//    201 202 204
}
