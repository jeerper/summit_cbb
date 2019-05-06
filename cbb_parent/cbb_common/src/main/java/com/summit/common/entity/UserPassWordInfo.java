package com.summit.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户基本信息接口实体
 */
public class UserPassWordInfo implements Serializable {

    /**
     * 账户名称
     */
    @ApiModelProperty(value="登录名称",name="userName",required=true)
    private String userName;


    /**
     * 账户密码
     */
    @ApiModelProperty(value="账户密码",name="password",required=true)
    private String password;

    @ApiModelProperty(value="确认密码",name="repeatPassword",required=true)
    private String repeatPassword;

    @ApiModelProperty(value="旧密码",name="oldPassword",required=true)
    private String oldPassword;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
