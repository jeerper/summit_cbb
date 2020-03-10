package com.summit.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(description="封装登陆日志记录类")
@Data
public class LoginLogInfo {
    @ApiModelProperty(value="账户名称")
    private String userName;
    @ApiModelProperty(value="用户昵称")
    private String name;
    @ApiModelProperty(value="角色名称")
    private String roleName;
    @ApiModelProperty(value="登陆时间",example="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date loginTime;
    @ApiModelProperty(value="在线时长(单位:分钟)")
    private int onlineTime;
    @ApiModelProperty(value="登陆IP")
    private String callerIP;

}
