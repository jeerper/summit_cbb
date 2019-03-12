package com.summit.common.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户基本信息接口实体
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -3561620536450870088L;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value="name",required=true)
    private String name;
    /**
     * 账户名称
     */
    @ApiModelProperty(value="userName",required=true)
    private String userName;
    /**
     * 账户密码
     */
    @ApiModelProperty(value="password",required=true)
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 启用状态
     */
    private Integer isEnabled;
    /**
     * 最后一次更新时间
     */
    @ApiModelProperty(hidden = true)
    private String lastUpdateTime;
    /**
     * 判断是否删除
     */
    @ApiModelProperty(hidden = true)
    private int state;

    /**
     * 备注
     */
    private String note;

    /**
     * 权限标识集合(对应sys_function表中的ID字段)
     */
    @ApiModelProperty(hidden = true)
    private String[] permissions;

    /**
     * 角色集合
     */
    @ApiModelProperty(hidden = true)
    private String[] roles;


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
