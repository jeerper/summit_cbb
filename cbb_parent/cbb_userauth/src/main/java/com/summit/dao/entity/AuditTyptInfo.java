package com.summit.dao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description="封装审核处理信息类")
@Data
public class AuditTyptInfo {
    @ApiModelProperty(value = "主键id", name = "authIds", required = true)
    private List<String> authIds;
    @ApiModelProperty(value = "审核方式(1:批准,2:拒绝)", name = "isAudited", required = true)
    private String isAudited;

    public AuditTyptInfo() {
    }
}
