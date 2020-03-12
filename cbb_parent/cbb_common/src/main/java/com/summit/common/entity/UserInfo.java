package com.summit.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户基本信息接口实体
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -3561620536450870088L;
    /**
     * 账户名称
     */
    @ApiModelProperty(value = "登录名称", name = "userName", required = true)
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "姓名", name = "name", required = true)
    private String name;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别", name = "sex", allowableValues = "1,2")
    private String sex;
    /**
     * 账户密码
     */
    @ApiModelProperty(value = "账户密码", name = "password", required = true)
    private String password;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;
    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码", name = "phoneNumber")
    private String phoneNumber;
    /**
     * 启用状态
     */
    @ApiModelProperty(value = "启用状态:1是，0否 ,添加默认为启用", name = "isEnabled", example = "1", allowableValues = "1,0")
    private Integer isEnabled = null;
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
     * 移动设备
     */
    @ApiModelProperty(value = "移动设备识别码 ,用于发送短信验证用", name = "imei")
    private String imei;

    @ApiModelProperty(value = "工作单位", name = "company")
    private String company;


    @ApiModelProperty(value = "岗位", name = "duty")
    private String duty;

    @ApiModelProperty(value = "职务", name = "post")
    private String post;

    @ApiModelProperty(value = "序号", name = "sn")
    private Integer sn;

    @ApiModelProperty(value = "用户头像", name = "headPortrait")
    private String headPortrait;

    @ApiModelProperty(value = "审核是否通过(0：发起审核，1：通过，2：不通过)", name = "isAudited")
    private String isAudited;

    @ApiModelProperty(value = "机构类型(0:内部机构;1:外部机构)", name = "deptType")
    private String deptType;

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getAdnms() {
        return adnms;
    }

    public void setAdnms(String adnms) {
        this.adnms = adnms;
    }

    public String getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(String deptNames) {
        this.deptNames = deptNames;
    }

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "note")
    private String note;

    @ApiModelProperty(value = "行政区划编码集合", name = "adcds")
    private String[] adcds;

    @ApiModelProperty(value = "查询用--行政区划名称，以,分割", name = "adcds", hidden = true)
    private String adnms;

    @ApiModelProperty(value = "部门集合", name = "depts")
    private String[] depts;

    @ApiModelProperty(value = "查询用--部门名称，以,分割", name = "deptNames", hidden = true)
    private String deptNames;

    /**
     * 权限标识集合(对应sys_function表中的ID字段)
     */
    @ApiModelProperty(value = "查询用--权限标识集合，查询用", name = "permissions", hidden = true)
    private String[] permissions;

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    /**
     * 角色集合
     */
    @ApiModelProperty(value = "查询用--角色编码集合", name = "roles")
    private String[] roles;

    @ApiModelProperty(value = "查询用--角色名称，以,分割", name = "roleNames", hidden = true)
    private String roleNames;


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



    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getIsAudited() {
        return isAudited;
    }

    public void setIsAudited(String isAudited) {
        this.isAudited = isAudited;
    }


}
