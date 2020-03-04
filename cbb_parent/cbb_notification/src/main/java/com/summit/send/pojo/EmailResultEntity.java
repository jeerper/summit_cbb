package com.summit.send.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("msg_email_result")
public class EmailResultEntity {

    @ApiModelProperty(value = "邮件结果ID",hidden = true)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "邮件发送记录ID",hidden = true)
    @TableId(value = "record_id")
    private String recordId;

    @ApiModelProperty(value = "接收人邮件地址")
    @TableField(value = "send_to")
    private String sendTo;

    @ApiModelProperty(value = "发送结果 1发送成功 2发送失败")
    @TableField(value = "send_result")
    private int sendResult;

}
