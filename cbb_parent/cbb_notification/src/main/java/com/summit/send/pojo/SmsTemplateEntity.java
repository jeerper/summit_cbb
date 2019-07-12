package com.summit.send.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SmsTemplateEntity {
    private String templateId;
    private String templateContent;
    private String templateCode;
    private String templateType;
    private Date createTime;
    private Date updateTime;
}
