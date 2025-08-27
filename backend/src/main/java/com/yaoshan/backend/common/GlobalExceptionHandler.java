package com.yaoshan.backend.common;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // 全局异常捕获
public class GlobalExceptionHandler {

    // 捕获所有 RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        return Result.error(500, "服务器运行错误: " + e.getMessage());
    }

    // 捕获所有 Exception
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error(500, "系统错误: " + e.getMessage());
    }
}