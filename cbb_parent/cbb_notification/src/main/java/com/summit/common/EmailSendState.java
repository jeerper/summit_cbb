package com.summit.common;

public enum EmailSendState {
    SEND_ING(1),
    SEND_END(2),
    SEND_EXCEPTION(3);
    private int code;

    EmailSendState(int code){
        this.code=code;
    }
    public int getCode(){
        return this.code;
    }
}
