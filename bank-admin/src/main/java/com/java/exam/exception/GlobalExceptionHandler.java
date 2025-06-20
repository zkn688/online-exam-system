package com.java.exam.exception;

import com.java.exam.utils.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpResult handleRuntimeException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        if (e instanceof BaseException){
            BaseException baseException = (BaseException) e;
            log.info(baseException.getMessage(),  e);
            return HttpResult.error(baseException.getMessage(),baseException.getCode());
        }
        log.info("请求地址'"+requestURI+"',发生未知异常.",  e);
        return HttpResult.error(ErrorCode.UNKNOWN);
    }

}
