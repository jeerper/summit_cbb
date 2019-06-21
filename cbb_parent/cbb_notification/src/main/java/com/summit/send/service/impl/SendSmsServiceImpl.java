package com.summit.send.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.send.pojo.SendSms;
import com.summit.send.service.SendSmsService;
import com.summit.send.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SendSmsServiceImpl implements SendSmsService {

    @Override
    public RestfulEntityBySummit sendSms(SendSms sendSms) {
        List<String> result = new ArrayList<String>();
        DefaultProfile profile = DefaultProfile.getProfile("default", "<accessKeyId>", "<accessSecret>");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendBatchSms");

        request.putQueryParameter("PhoneNumberJson", JSONUtil.parseObjToJson(sendSms.getPhoneNumbers()));
        request.putQueryParameter("SignNameJson", JSONUtil.parseObjToJson(sendSms.getSignNames()));
        request.putQueryParameter("TemplateCode", sendSms.getTemplateCode());
        request.putQueryParameter("TemplateParamJson", JSONUtil.parseObjToJson(sendSms.getTemplateVars()));
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("response data is {}",response.getData());
        } catch (ServerException e) {
            log.info("发送短信失败,{}",e);
        } catch (ClientException e) {
            log.info("发送短信失败,{}",e);
        }

        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,result);
    }


}
