package com.summit.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserDeptDutyBean implements Serializable {

    @ApiModelProperty(value="id",name="ID")
    private String Id;

    @ApiModelProperty(value="用户名",name="username")
    private String username;

    @ApiModelProperty(value="部门id",name="deptId")
    private String deptId;

    @ApiModelProperty(value="职位(1:管理员，2:河长，3：专员，4：包河人员)",name="duty")
    private String duty;

    public UserDeptDutyBean() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }
}
