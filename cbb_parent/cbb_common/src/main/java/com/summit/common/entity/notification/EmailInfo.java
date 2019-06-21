package com.summit.common.entity.notification;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="邮件信息类", description="包含邮件所含信息，用来接收参数，不包含附件和图片参数")
public class EmailInfo  implements Serializable {

    //邮件编号
    @ApiModelProperty(value="邮件编号",name="emailId")
    private String emailId;
    @ApiModelProperty(value="接收邮箱地址，可填多个",name="toEmails",required = true)
    private String[] toEmails;
    @ApiModelProperty(value="邮件标题",name="title",required = true)
    private String title;
    @ApiModelProperty(value="发送内容，html内容中的图片cid需和上传图片的文件名一致",name="content",required = true)
    private String content;
    //内容类型 text html template
    @ApiModelProperty(value="内容类型",name="contentType",allowableValues = "text,html,template",required = true)
    private String contentType;

    //模板名称
    @ApiModelProperty(value="模板名称",name="templateName")
    private String templateName;
    //模板变量替换,键和值用冒号隔开
    @ApiModelProperty(value="模板变量替换,键和值用冒号隔开",name="templateVars")
    private String[] templateVars;
}
