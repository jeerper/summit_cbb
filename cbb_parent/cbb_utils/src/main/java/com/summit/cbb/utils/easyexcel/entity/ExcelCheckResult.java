package com.summit.cbb.utils.easyexcel.entity;

import lombok.Data;

import java.util.List;

/**
 *  excel数据导入结果
 *
 * @Author maoyx
 * @Date 2020/3/28 20:20
 **/
@Data
public class ExcelCheckResult<T> {

    private List<T> successBeans;

    private List<T> errorBeans;

    private List<String> errorMsgs;

    public ExcelCheckResult(List<T> successBeans, List<T> errorBeans, List<String> errorMsgs) {
        this.successBeans = successBeans;
        this.errorBeans = errorBeans;
        this.errorMsgs = errorMsgs;
    }

    public ExcelCheckResult(List<T> successBeans) {
        this.successBeans = successBeans;
    }

}
