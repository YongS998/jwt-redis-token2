package com.yongs.token2.handler;

import com.yongs.token2.exception.BusinessException;
import com.yongs.token2.pojo.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/18 13:07
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public Result handlerAccessDeniedException(AccessDeniedException e){
        return Result.error(403,"权限不足");
    }

    @ExceptionHandler(BusinessException.class)
    public Result handlerBusinessException(BusinessException e){
        return Result.error(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e){
        return Result.error(e.getMessage());
    }
}
