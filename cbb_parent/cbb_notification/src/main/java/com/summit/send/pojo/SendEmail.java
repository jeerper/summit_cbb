package com.summit.send.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@ApiModel(value="邮件发送类", description="继承了EmailInfo类，包含附件和图片参数，用来真正发送邮件")
public class SendEmail extends EmailInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="邮件附件，可传多个 ",name="attachMultipartFiles")
    private MultipartFile[] attachMultipartFiles;
    @ApiModelProperty(value="邮件图片，可传多个",name="imagesMultipartFiles")
    private MultipartFile[] imagesMultipartFiles;

}
