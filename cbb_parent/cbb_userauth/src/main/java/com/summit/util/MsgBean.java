package com.summit.util;


import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

public class MsgBean implements Serializable {

    // 状态码
    private String code;

    // 请求成功后返回的数据
    private JSONArray data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // 状态码为非成功状态下需要显示的提示内容
    private String message;
}
