package com.summit.common.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户基本信息接口实体
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -3561620536450870088L;
    /**
     * 账户名称
     */
    @ApiModelProperty(value="登录名称",name="userName",required=true)
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value="姓名",name="name",required=true)
    private String name;
    /**
     * 性别
     */
    @ApiModelProperty(value="性别",name="sex")
    private String sex;
    /**
     * 账户密码
     */
    @ApiModelProperty(value="账户密码",name="password",required=true)
    private String password;
    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱",name="email")
    private String email;
    /**
     * 电话号码
     */
    @ApiModelProperty(value="电话号码",name="phoneNumber")
    private String phoneNumber;
    /**
     * 启用状态 
     */
    @ApiModelProperty(value="启用状态:1是，0否 ,添加默认为启用",name="isEnabled")
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
    @ApiModelProperty(value="备注",name="note")
    private String note;

    @ApiModelProperty(value="行政区划集合，以,分割",name="adcds")
    private String[] adcds;
    
    @ApiModelProperty(value="部门集合，以,分割",name="depts")
    private String[] depts;
    
    /**
     * 权限标识集合(对应sys_function表中的ID字段)
     */
    @ApiModelProperty(value="权限标识集合，查询用",name="permissions")
    private String[] permissions;

    
    /**
     * 角色集合
     */
    @ApiModelProperty(value="角色集合，查询用",name="roles")
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

	public String[] getAdcds() {
		return adcds;
	}

	public void setAdcds(String[] adcds) {
		this.adcds = adcds;
	}

	public String[] getDepts() {
		return depts;
	}

	public void setDepts(String[] depts) {
		this.depts = depts;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
