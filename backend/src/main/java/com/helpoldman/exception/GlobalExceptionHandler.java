package com.helpoldman.exception;

import com.helpoldman.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return Result.error(500, "系统异常：" + e.getMessage());
    }
    
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        e.printStackTrace();
        return Result.error(400, "业务异常：" + e.getMessage());
    }
}