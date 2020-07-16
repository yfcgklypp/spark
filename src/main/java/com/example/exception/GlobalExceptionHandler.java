package com.example.exception;

import com.example.utils.ResultData;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = RateLimiterException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultData runtimeExceptionHandler(RateLimiterException e) {
        logger.error("系统错误:", e);
        return ResultData.getResultError(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "处理失败");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultData runtimeExceptionHandler(Exception e) {
        Throwable cause = e.getCause();
        logger.error("系统错误:", e);
        logger.error(e.getMessage());
        if (cause instanceof JsonMappingException) {
            return ResultData.getResultError("参数错误");
        }
        return ResultData.getResultError(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "处理失败");
    }

}
