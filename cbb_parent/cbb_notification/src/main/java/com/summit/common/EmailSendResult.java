package com.summit.common;

public enum EmailSendResult {
    SEND_SUCCESS(1),
    SEND_FAIL(2);
    private int code;

    EmailSendResult(int code){
        this.code=code;
    }
    public int getCode(){
        return this.code;
    }
}
