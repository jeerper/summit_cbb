package com.summit.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName(value = "sys_dept_record")
public class DeptRecordBean implements Serializable {
    private static final long serialVersionUID = 4492192174379444960L;

    @ApiModelProperty(value = "主键id", name = "id", required = true)
    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "上级部门id", name = "pId")
    @TableField(value = "pId")
    private String pId;

    @ApiModelProperty(value = "部门编号", name = "deptcode")
    @TableField(value = "deptcode")
    private String deptcode;

    @ApiModelProperty(value = "部门名称", name = "deptName")
    @TableField(value = "deptName")
    private String deptName;

    @ApiModelProperty(value = "行政区划", name = "adcd")
    @TableField(value = "adcd")
    private String adcd;

    @ApiModelProperty(value = "部门联系人", name = "deptHead")
    @TableField(value = "deptHead")
    private String deptHead;

    @ApiModelProperty(value = "机构类型(0:内部机构;1:外部机构)", name = "deptType")
    @TableField(value = "deptType")
    private String deptType;

    @ApiModelProperty(value = "部门id", name = "deptId")
    @TableField(value = "deptName")
    private String deptId;

    public DeptRecordBean() {
    }
}
