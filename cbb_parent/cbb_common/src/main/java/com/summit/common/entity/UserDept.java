package com.summit.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDept implements Serializable {
    private static final long serialVersionUID = 326428682321610237L;
    @ApiModelProperty(value = "主键id(添加自动生成,修改必填)", name = "id")
    private String id;

    @ApiModelProperty(value = "用户名", name = "username")
    private String username;

    @ApiModelProperty(value = "部门id", name = "deptId")
    private String deptId;

    @ApiModelProperty(value = "crateTime", name = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date crateTime;

    public UserDept() {
    }
}
