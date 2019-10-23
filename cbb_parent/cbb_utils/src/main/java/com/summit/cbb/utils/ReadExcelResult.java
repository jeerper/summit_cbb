package com.summit.cbb.utils;

import java.util.List;

public class ReadExcelResult<T> {

    private String errorMsg;// 错误信息
    private String infoMsg;// 提示信息
    private List<T> dataList;// 数据

    public ReadExcelResult() {
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public String getInfoMsg() {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }
}
