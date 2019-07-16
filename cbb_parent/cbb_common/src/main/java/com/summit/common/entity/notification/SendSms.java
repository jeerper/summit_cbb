package com.summit.common.entity.notification;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value="短信发送类", description="发送短信所需的所有信息")
public class SendSms  implements Serializable {

    @ApiModelProperty(value="短信编号",name="smsId")
    private String smsId;
    @ApiModelProperty(value="接收者号码，可传多个",name="phoneNumbers",required = true)
    private String[] phoneNumbers;
    @ApiModelProperty(value="签名",name="signName",required = true)
    private String signName;
    @ApiModelProperty(value="模板号",name="templateCode",required = true)
    private String templateCode;
    @ApiModelProperty(value="模板参数，模板key和value的键值对",name="templateVars",required = true)
//    private List<Map<String,Object>> templateVars;
    private Map<String,Object> templateVars;


}
