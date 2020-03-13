package com.summit.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户审核基本信息接口实体
 */

@Data
@TableName(value = "sys_user_auth")
public class UserAuditBean  implements Serializable {

    private static final long serialVersionUID = -1481870624215326162L;

    @ApiModelProperty(value = "主键id", name = "id", required = true)
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "账户名称", name = "userNameAuth", required = true)
    @TableField(value = "userName_auth")
    private String userNameAuth;

    @ApiModelProperty(value = "用户姓名", name = "nameAuth")
    @TableField(value = "name_auth")
    private String nameAuth;

    @ApiModelProperty(value = "性别", name = "sexAuth")
    @TableField(value = "sex_auth")
    private String sexAuth;

    @ApiModelProperty(value = "密码", name = "passwordAuth")
    @TableField(value = "password_auth")
    private String passwordAuth;

    @ApiModelProperty(value = "邮箱", name = "emailAuth")
    @TableField(value = "email_auth")
    private String emailAuth;

    @ApiModelProperty(value = "电话号码", name = "phoneNumberAuth")
    @TableField(value = "phone_number_auth")
    private String phoneNumberAuth;

    @ApiModelProperty(value = "是否启用 0禁用1启用", name = "isEnabledAuth")
    @TableField(value = "is_enabled_auth")
    private String isEnabledAuth;

    @ApiModelProperty(value = "头像", name = "headPortraitAuth")
    @TableField(value = "headPortrait_auth")
    private String headPortraitAuth;

    @ApiModelProperty(value = "岗位", name = "dutyAuth")
    @TableField(value = "duty_auth")
    private String dutyAuth;

    @ApiModelProperty(value = "部门集合", name = "deptAuth")
    @TableField(value = "dept_auth")
    private String[] deptAuth;

    /*@ApiModelProperty(value = "查询用--部门名称，以,分割", name = "deptNamesAuth", hidden = true)
    private String deptNamesAuth;*/


    @ApiModelProperty(value = "行政区划集合", name = "adcdAuth")
    @TableField(value = "adcd_auth")
    private String[] adcdAuth;

   /* @ApiModelProperty(value = "查询用--行政区划名称，以,分割", name = "adnmsAuth", hidden = true)
    private String adnmsAuth;*/

    @ApiModelProperty(value = "职位", name = "postAuth")
    @TableField(value = "post_auth")
    private String postAuth;

    @ApiModelProperty(value = "审核人", name = "authPerson")
    @TableField(value = "auth_person")
    private String authPerson;

    @ApiModelProperty(value = "审核是否通过(0：发起审核，1：通过，2：不通过)", name = "isAudited")
    @TableField(value = "isAudited")
    private String isAudited;

    @ApiModelProperty(value = "审核时间", name = "auth_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "auth_time")
    private Date auth_time;

    @ApiModelProperty(value = "提交至哪个部门", name = "submittedTo")
    @TableField(value = "submitted_to")
    private String submittedTo;


    @ApiModelProperty(value = "备注", name = "remark")
    @TableField(value = "remark")
    private String remark;

    @ApiModelProperty(value = "申请人姓名", name = "applyName")
    @TableField(value = "apply_name")
    private String applyName;

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public UserAuditBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNameAuth() {
        return userNameAuth;
    }

    public void setUserNameAuth(String userNameAuth) {
        this.userNameAuth = userNameAuth;
    }

    public String getNameAuth() {
        return nameAuth;
    }

    public void setNameAuth(String nameAuth) {
        this.nameAuth = nameAuth;
    }

    public String getSexAuth() {
        return sexAuth;
    }

    public void setSexAuth(String sexAuth) {
        this.sexAuth = sexAuth;
    }

    public String getPasswordAuth() {
        return passwordAuth;
    }

    public void setPasswordAuth(String passwordAuth) {
        this.passwordAuth = passwordAuth;
    }

    public String getEmailAuth() {
        return emailAuth;
    }

    public void setEmailAuth(String emailAuth) {
        this.emailAuth = emailAuth;
    }

    public String getPhoneNumberAuth() {
        return phoneNumberAuth;
    }

    public void setPhoneNumberAuth(String phoneNumberAuth) {
        this.phoneNumberAuth = phoneNumberAuth;
    }

    public String getIsEnabledAuth() {
        return isEnabledAuth;
    }

    public void setIsEnabledAuth(String isEnabledAuth) {
        this.isEnabledAuth = isEnabledAuth;
    }

    public String getHeadPortraitAuth() {
        return headPortraitAuth;
    }

    public void setHeadPortraitAuth(String headPortraitAuth) {
        this.headPortraitAuth = headPortraitAuth;
    }

    public String getDutyAuth() {
        return dutyAuth;
    }

    public void setDutyAuth(String dutyAuth) {
        this.dutyAuth = dutyAuth;
    }

    public String[] getDeptAuth() {
        return deptAuth;
    }

    public void setDeptAuth(String[] deptAuth) {
        this.deptAuth = deptAuth;
    }

    public String[] getAdcdAuth() {
        return adcdAuth;
    }

    public void setAdcdAuth(String[] adcdAuth) {
        this.adcdAuth = adcdAuth;
    }

    public String getPostAuth() {
        return postAuth;
    }

    public void setPostAuth(String postAuth) {
        this.postAuth = postAuth;
    }

    public String getAuthPerson() {
        return authPerson;
    }

    public void setAuthPerson(String authPerson) {
        this.authPerson = authPerson;
    }

    public String getIsAudited() {
        return isAudited;
    }

    public void setIsAudited(String isAudited) {
        this.isAudited = isAudited;
    }

    public Date getAuth_time() {
        return auth_time;
    }

    public void setAuth_time(Date auth_time) {
        this.auth_time = auth_time;
    }

    public String getSubmittedTo() {
        return submittedTo;
    }

    public void setSubmittedTo(String submittedTo) {
        this.submittedTo = submittedTo;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
