package com.c1eye.server.core;

import com.c1eye.server.core.configuration.ExceptionCodeConfiguration;
import com.c1eye.server.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * @author c1eye
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    @Autowired
    private ExceptionCodeConfiguration exceptionCodeConfiguration;

    /**
     * 全局异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest request, Exception e) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        UnifyResponse unifyResponse = new UnifyResponse(9999, "服务器异常", method + ": " + requestURI);
        return unifyResponse;
    }


    @ExceptionHandler(HttpException.class)
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest request, HttpException e) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        int code = e.getCode();

        UnifyResponse unifyResponse = new UnifyResponse(code, exceptionCodeConfiguration.getMessage(code),
                method + ": " + requestURI);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());

        ResponseEntity<UnifyResponse> r = new ResponseEntity<UnifyResponse>(unifyResponse, headers, httpStatus);
        return r;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public UnifyResponse handleConstraintException(HttpServletRequest request,ConstraintViolationException e) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        //TODO 这里可以获得更详细的错误信息 e.constraint
        String message = e.getMessage();
        return new UnifyResponse(10001, message, method + ": " + requestURI);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public UnifyResponse handleBeanValidation(HttpServletRequest request, MethodArgumentNotValidException e) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String msg = formatAllErrorMessages(errors);
        return new UnifyResponse(10001, msg, method + ": " + requestURI);
    }

    private String formatAllErrorMessages(List<ObjectError> errors) {
        StringBuilder errorMsg = new StringBuilder();
        errors.forEach(e -> {
            errorMsg.append(e.getDefaultMessage()).append(';');
        });
        return errorMsg.toString();
    }
}
