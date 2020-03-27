package com.summit.common.exception;

import com.summit.common.constant.ResponseCode;

/**
 * 通用异常类
 *
 * @Author maoyx
 * @Date 2020/3/26 11:34
 **/
public class CommonException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private ResponseCode responseCode;

    public CommonException() {
    }

    public CommonException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public ResponseCode getStatusCode() {
        return this.responseCode;
    }

    public void setStatusCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }


}
