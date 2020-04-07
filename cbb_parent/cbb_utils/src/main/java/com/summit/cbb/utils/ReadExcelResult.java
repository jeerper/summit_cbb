package com.summit.cbb.utils;

import java.util.List;

/**
 * @author xjtuhgd
 * @date 2019/12/09
 */
public class ReadExcelResult<T> {

    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 提示信息
     */
    private String infoMsg;
    /**
     * 数据
     */
    private List<T> dataList;

    public ReadExcelResult() {}

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
