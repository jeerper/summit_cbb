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

@Data
@TableName(value = "sys_auth")
public class AuthBean implements Serializable {
    private static final long serialVersionUID = -6476508846502001175L;
    @ApiModelProperty(value = "主键id", name = "id", required = true)
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "申请人姓名", name = "applyName")
    @TableField(value = "apply_name")
    private String applyName;

    @ApiModelProperty(value = "申请类型：(0:机构申请，1：用户申请)", name = "applyType")
    @TableField(value = "apply_type")
    private String applyType;


    @ApiModelProperty(value = "提交至哪个部门", name = "submittedTo")
    @TableField(value = "submitted_to")
    private String submittedTo;

    @ApiModelProperty(value = "审核是否通过(0：待处理，1：通过，2：不通过)", name = "isAudited")
    @TableField(value = "isAudited")
    private String isAudited;

    @ApiModelProperty(value = "审核时间", name = "applytime")
    @TableField(value = "apply_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applytime;

    @ApiModelProperty(value = "申请人Id(分别来自用户审核表和部门审核表)", name = "applyId")
    @TableField(value = "apply_Id")
    private String applyId;

    public AuthBean() {
    }
}
