package com.summit.send.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("msg_email")
public class EmailEntity {

    @ApiModelProperty(value = "邮件发送记录ID",hidden = true)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "邮件标题")
    @TableField(value = "email_title")
    private String emailTitle;

    @ApiModelProperty(value = "邮件内容")
    @TableField(value = "email_content")
    private String emailContent;

    @ApiModelProperty(value = "邮件保存地址")
    @TableField(value = "file_url")
    private String fileUrl;

    @ApiModelProperty(value = "发送状态 1发送中 2发送完毕")
    @TableField(value = "send_state")
    private int sendState;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time")
    private Date updateTime;
}
