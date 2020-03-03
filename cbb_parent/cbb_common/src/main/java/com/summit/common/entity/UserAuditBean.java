package com.summit.common.entity;

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
public class UserAuditBean  implements Serializable {

    private static final long serialVersionUID = -1481870624215326162L;

    @ApiModelProperty(value = "主键id", name = "id", required = true)
    private String id;

    @ApiModelProperty(value = "账户名称", name = "userNameAuth", required = true)
    private String userNameAuth;

    @ApiModelProperty(value = "用户姓名", name = "nameAuth")
    private String nameAuth;

    @ApiModelProperty(value = "性别", name = "sexAuth")
    private String sexAuth;

    @ApiModelProperty(value = "密码", name = "passwordAuth")
    private String passwordAuth;

    @ApiModelProperty(value = "邮箱", name = "emailAuth")
    private String emailAuth;

    @ApiModelProperty(value = "电话号码", name = "phoneNumberAuth")
    private String phoneNumberAuth;

    @ApiModelProperty(value = "是否启用 0禁用1启用", name = "isEnabledAuth")
    private String isEnabledAuth;

    @ApiModelProperty(value = "头像", name = "headPortraitAuth")
    private String headPortraitAuth;

    @ApiModelProperty(value = "岗位", name = "dutyAuth")
    private String dutyAuth;

    @ApiModelProperty(value = "部门集合", name = "deptAuth")
    private String[] deptAuth;

    @ApiModelProperty(value = "行政区划集合", name = "adcdAuth")
    private String[] adcdAuth;

    @ApiModelProperty(value = "职位", name = "postAuth")
    private String postAuth;

    @ApiModelProperty(value = "审核人", name = "authPerson")
    private String authPerson;

    @ApiModelProperty(value = "审核是否通过(0：发起审核，1：通过，2：不通过)", name = "isAudited")
    private String isAudited;

    @ApiModelProperty(value = "审核时间", name = "auth_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auth_time;

    @ApiModelProperty(value = "提交至哪个部门", name = "submittedTo")
    private String submittedTo;



    public UserAuditBean() {
    }


}
