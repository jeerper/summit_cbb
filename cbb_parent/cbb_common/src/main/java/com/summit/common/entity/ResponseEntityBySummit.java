package com.summit.common.entity;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author Administrator
 */

public class ResponseEntityBySummit<T> implements Serializable {


    private static final long serialVersionUID = 4157608212869329025L;
    private int code = HttpStatus.OK.value();


    private String msg = "success";


    private T data;

    /**
     * 用于只响应正确状态时使用
     */
    public ResponseEntityBySummit() {
    }

    /**
     * 响应正确消息时使用
     *
     * @param data 内容主体
     */
    public ResponseEntityBySummit(T data) {
        this.data = data;
    }

    /**
     * 响应正确消息时使用
     *
     * @param data 内容主体
     * @param msg  响应的额外信息
     */
    public ResponseEntityBySummit(T data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    /**
     * 用于服务器内部错误消息
     *
     * @param e 异常实例
     */
    public ResponseEntityBySummit(Throwable e) {
        this.msg = e.getMessage();
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    /**
     * 自定义消息
     *
     * @param httpStatus 响应状态
     * @param msg        响应消息
     */
    public ResponseEntityBySummit(HttpStatus httpStatus, String msg) {
        this.msg = msg;
        this.code = httpStatus.value();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
