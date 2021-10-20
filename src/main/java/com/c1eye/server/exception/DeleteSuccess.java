package com.c1eye.server.exception;


import com.c1eye.server.exception.http.HttpException;

public class DeleteSuccess extends HttpException {
    public DeleteSuccess(int code){
        this.httpStatusCode = 200;
        this.code = code;
    }
    // 200 201 204 200
    // 200 201 200 200

    // Create：201 资源本身
    // Get: 200
    // Put: 200
    // Delete: 200
}
