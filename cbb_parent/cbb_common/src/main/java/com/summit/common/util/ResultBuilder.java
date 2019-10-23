package com.summit.common.util;

import com.summit.common.constant.ResponseCode;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;

/**
 * RESTful接口返回构建器
 *
 * @author hegd
 */
public class ResultBuilder {

    /**
     * 无数据返回时使用
     *
     * @return
     */
    public static <T> RestfulEntityBySummit<T> buildSuccess() {
        return buildSuccess(null);
    }

    /**
     * 有数据返回时使用
     *
     * @param data
     * @return
     */
    public static <T> RestfulEntityBySummit<T> buildSuccess(T data) {
        RestfulEntityBySummit<T> result = new RestfulEntityBySummit<>();
        result.setCode(ResponseCodeEnum.CODE_0000.getCode());
        result.setMsg(ResponseCodeEnum.CODE_0000.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 出现异常时返回使用
     *
     * @param code
     * @return
     */
    public static <T> RestfulEntityBySummit<T> buildError(ResponseCode code) {
        RestfulEntityBySummit<T> result = new RestfulEntityBySummit<>();
        result.setCode(code.getCode());
        result.setMsg(code.getMessage());
        result.setData(null);
        return result;
    }

    /**
     * 出现异常,且需要设置data时使用
     *
     * @param code 返回码
     * @param t    返回数据
     * @return
     */
    public static <T> RestfulEntityBySummit<T> buildError(ResponseCode code, T t) {
        RestfulEntityBySummit<T> result = new RestfulEntityBySummit<>();
        result.setCode(code.getCode());
        result.setMsg(code.getMessage());
        result.setData(t);
        return result;
    }

    /**
     * 出现异常,且需要设置message和data时使用
     *
     * @param code 返回码
     * @param t    返回数据
     * @return
     */
    public static <T> RestfulEntityBySummit<T> buildError(ResponseCode code, String msg, T t) {
        RestfulEntityBySummit<T> result = new RestfulEntityBySummit<>();
        result.setCode(code.getCode());
        result.setMsg(msg);
        result.setData(t);
        return result;
    }
}
