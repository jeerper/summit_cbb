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
public class RestFulEntityBySummit<T> implements Serializable {


    private static final long serialVersionUID = 4157608212869329025L;


    @ApiModelProperty(value = "返回码", name = "code", example = "CODE_0000")
    private String code = ResponseCodeBySummit.CODE_0000.name();

    @ApiModelProperty(value = "返回信息", name = "msg", example = "操作成功")
    private String msg = ResponseCodeBySummit.CODE_0000.getDescription();

    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;

    /**
     * 用于只响应正确状态时使用
     */
    public RestFulEntityBySummit() {
    }

    /**
     * 响应正确消息时使用
     *
     * @param data 内容主体
     */
    public RestFulEntityBySummit(T data) {
        this.data = data;
    }

    /**
     * 自定义响应消息
     *
     * @param responseCodeBySummit 状态码枚举
     * @param data                 主体数据
     */
    public RestFulEntityBySummit(ResponseCodeBySummit responseCodeBySummit, T data) {
        this.code = responseCodeBySummit.name();
        this.msg = responseCodeBySummit.getDescription();
        this.data = data;
    }

    /**
     * 用于服务器内部错误消息
     *
     * @param e 异常实例
     */
    public RestFulEntityBySummit(Throwable e) {
        this.code = ResponseCodeBySummit.CODE_9999.name();
        this.msg = ResponseCodeBySummit.CODE_9999.getDescription() + ":" + e.getMessage();
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

    public static final class RestFulEntityBySummitBuilder<T> {
        private String code = ResponseCodeBySummit.CODE_0000.name();
        private String msg = ResponseCodeBySummit.CODE_0000.getDescription();
        private T data;

        private RestFulEntityBySummitBuilder() {
        }

        public static RestFulEntityBySummitBuilder createBuilder() {
            return new RestFulEntityBySummitBuilder();
        }

        public RestFulEntityBySummitBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public RestFulEntityBySummitBuilder withMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public RestFulEntityBySummitBuilder withData(T data) {
            this.data = data;
            return this;
        }

        public RestFulEntityBySummit build() {
            RestFulEntityBySummit restFulEntityBySummit = new RestFulEntityBySummit();
            restFulEntityBySummit.setCode(code);
            restFulEntityBySummit.setMsg(msg);
            restFulEntityBySummit.setData(data);
            return restFulEntityBySummit;
        }
    }
}
