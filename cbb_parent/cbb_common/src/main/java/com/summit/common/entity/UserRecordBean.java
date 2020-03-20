package com.summit.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName(value = "sys_user_record")
public class UserRecordBean implements Serializable {
    private static final long serialVersionUID = 3518090892225690953L;

    @ApiModelProperty(value = "主键id", name = "id", required = true)
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "账户名称", name = "username", required = true)
    @TableField(value = "username")
    private String username;

    @ApiModelProperty(value = "名字", name = "name")
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "性别", name = "sex")
    @TableField(value = "sex")
    private String sex;

    @ApiModelProperty(value = "密码", name = "password")
    @TableField(value = "password")
    private String password;

    @ApiModelProperty(value = "邮箱", name = "email")
    @TableField(value = "email")
    private String email;

    @ApiModelProperty(value = "电话", name = "phoneNumber")
    @TableField(value = "phoneNumber")
    private String phoneNumber;

    @ApiModelProperty(value = "是否启用", name = "isEnable")
    @TableField(value = "is_enable")
    private String isEnable;

    @ApiModelProperty(value = "头像", name = "headPortrait")
    @TableField(value = "headPortrait")
    private String headPortrait;

    @ApiModelProperty(value = "岗位", name = "duty")
    @TableField(value = "duty")
    private String duty;

    @ApiModelProperty(value = "职位", name = "post")
    @TableField(value = "post")
    private String post;

    @ApiModelProperty(value = "部门", name = "dept")
    @TableField(value = "dept")
    private String dept;

    @ApiModelProperty(value = "行政区划", name = "adcd")
    @TableField(value = "adcd")
    private String adcd;

    public UserRecordBean() {
    }
}
