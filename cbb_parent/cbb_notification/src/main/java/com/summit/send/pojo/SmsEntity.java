package com.summit.send.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SmsEntity {

    private String smsId;
    private String templateId;
    private String resPhone;
    private String bizId;
    private String smsSignname;
    private String smsContent;
    private int sendState;
    private Date createTime;
    private Date updateTime;

}
