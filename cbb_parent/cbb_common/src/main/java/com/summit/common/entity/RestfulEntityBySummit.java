package com.summit.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author Administrator
 */
@ApiModel(description = "响应信息主体")
public class RestfulEntityBySummit<T> implements Serializable {


    private static final long serialVersionUID = 4157608212869329025L;

    @ApiModelProperty(value = "返回码", name = "code", example = "CODE_0000")
    private String code;


    @ApiModelProperty(value = "返回信息", name = "msg", example = "操作成功")
    private String msg;

    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;

    /**
     * 用于只响应正确状态时使用
     */
    public RestfulEntityBySummit() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
