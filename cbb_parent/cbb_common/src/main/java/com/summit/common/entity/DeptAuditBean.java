package com.summit.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门审核基本信息接口实体
 */
@Data
@TableName(value = "sys_dept_auth")
public class DeptAuditBean implements Serializable {


    private static final long serialVersionUID = -6061627711178803654L;

    @ApiModelProperty(value = "主键id", name = "id", required = true)
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;


    @ApiModelProperty(value = "部门id", name = "deptIdAuth", required = true)
    @TableField(value = "deptId_auth")
    private String deptIdAuth;

    @ApiModelProperty(value = "上级部门id", name = "pIdAuth")
    @TableField(value = "pId_auth")
    private String pIdAuth;

    @ApiModelProperty(value = "部门编号", name = "deptcodeAuth")
    @TableField(value = "deptcode_auth")
    private String deptcodeAuth;


    @ApiModelProperty(value = "部门名称", name = "deptNameAuth")
    @TableField(value = "deptName_auth")
    private String deptNameAuth;

    @ApiModelProperty(value = "行政区划", name = "adcdAuth")
    @TableField(value = "adcd_auth")
    private String adcdAuth;

    @ApiModelProperty(value = "审核人", name = "authPerson")
    @TableField(value = "auth_person")
    private String authPerson;

    @ApiModelProperty(value = "审核是否通过(0：发起审核，1：通过，2：不通过)", name = "authPerson")
    @TableField(value = "isAudited")
    private String isAudited;

    @ApiModelProperty(value = "审核是否通过(0：发起审核，1：通过，2：不通过)", name = "authPerson")
    @TableField(value = "auth_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date authtime;

    @ApiModelProperty(value = "提交至哪个部门", name = "submittedTo")
    @TableField(value = "submitted_to")
    private String submittedTo;


    public DeptAuditBean() {
    }
}


