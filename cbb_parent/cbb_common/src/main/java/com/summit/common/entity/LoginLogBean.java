package com.summit.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName(value = "sys_login_log")
public class LoginLogBean {

    @TableField(value = "id")
    private String id;
    @TableField(value = "userName")
    private String loginUserName;
    @TableField(value = "callerIP")
    private String loginIp;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @TableField(value = "loginTime")
    private Date loginTime;
    @TableField(value = "onlineTime")
    private int onlineTime;
    @TableField(value = "log_sucesss_or_not")
    private String logSuccessOrNot;


}
