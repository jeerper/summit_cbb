/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

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


}
