package com.summit.send.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="短信发送类", description="发送短信所需的所有信息")
public class SendSms  implements Serializable {

    @ApiModelProperty(value="短信编号",name="smsId")
    private String smsId;
    @ApiModelProperty(value="接收者号码，可传多个",name="phoneNumbers",required = true)
    private String[] phoneNumbers;
    @ApiModelProperty(value="签名，可传多个，和号码一一对应",name="signNames",required = true)
    private String[] signNames;
    @ApiModelProperty(value="模板号",name="templateCode",required = true)
    private String templateCode;
    @ApiModelProperty(value="模板参数，用冒号隔开",name="templateVars",required = true)
    private String[] templateVars;


}
