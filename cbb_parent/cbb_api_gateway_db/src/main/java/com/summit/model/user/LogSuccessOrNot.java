package com.summit.model.user;

public enum LogSuccessOrNot {

    //登录成功
    Success("0"),
    //登录失败
    Failure("1");

    private String code;

    LogSuccessOrNot(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static LogSuccessOrNot codeOf(String code) {
        for (LogSuccessOrNot v : values()) {
            if (v.code.equals(code)) {
                return v;
            }
        }
        return null;
    }

}
