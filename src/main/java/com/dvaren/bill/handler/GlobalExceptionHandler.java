package com.dvaren.bill.handler;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 捕获controller异常
 * controller抛出异常执行下边的函数
 * 返回Response写入ApiResult
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    public Object handleException(Exception e) {
//        log.error("服务器异常-->");
//        log.error(e.getMessage());
//        return new ResponseResult(StatusCode.SERVER_ERROR,"服务器异常");
//    }

    @ExceptionHandler(value = ApiException.class)
    public Object handleException(ApiException e) {
        log.error(e.getMessage());
        return new ResponseResult<>(e.getCode(), e.getMessage(),null);
    }


}
