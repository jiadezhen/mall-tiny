package com.macro.mall.tiny.config;

import com.macro.mall.tiny.common.api.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AllExceptionHandler {

    /**
     * 处理其他异常
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    public CommonResult exceptionHandler(Exception e){
        log.error("未知异常！原因是:",e);
        log.info("===============================================================================================================================================\n\n");
        return CommonResult.failed("操作失败");
    }


}

