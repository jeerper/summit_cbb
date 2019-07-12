package com.summit.send.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class EmailEntity {
    private String emailId;
    private String emailName;
    private String sendTo;
    private String emailTitle;
    private String emailContent;
    private int sendState;
    private Date createTime;
    private Date updateTime;
}
