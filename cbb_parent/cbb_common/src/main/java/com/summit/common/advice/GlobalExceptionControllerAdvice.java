package com.summit.common.advice;

import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.exception.CommonException;
import com.summit.common.util.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理捕捉类
 *
 * @Author maoyx
 * @Date 2020/3/26 11:29
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionControllerAdvice {

    public GlobalExceptionControllerAdvice() {
    }

    @ExceptionHandler
    @ResponseBody
    public RestfulEntityBySummit<?> handleException(HttpServletRequest req, HttpServletResponse rsp, Exception e) {
        if (e instanceof CommonException) {//捕获CommonException异常
            CommonException ce = (CommonException)e;
            if (ce.getStatusCode() == null){
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999,ce.getErrorMsg(),null);
            }
            return ResultBuilder.buildError(ce.getStatusCode());
        } else {//捕获其他异常
            StringWriter sw= new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }




}
