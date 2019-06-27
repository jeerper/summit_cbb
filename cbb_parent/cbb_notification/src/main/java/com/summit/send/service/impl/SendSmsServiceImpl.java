package com.summit.send.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.summit.common.constant.ResponseCode;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendSms;
import com.summit.common.util.ResultBuilder;
import com.summit.send.service.SendSmsService;
import com.summit.send.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.dysmsapi.transform.v20170525.SendSmsResponseUnmarshaller;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SendSmsServiceImpl implements SendSmsService {

    @Value("${access-key-id}")
    private String accessKeyId;
    @Value("${access-key-secret}")
    private String secret;

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";


    @Override
    public RestfulEntityBySummit sendSms(SendSms sendSms) {
        if(sendSms.getTemplateVars() == null || sendSms.getTemplateVars().size() == 0){
            return sendSmsByForeach(sendSms);
        }else {
            return sendSmsByJson(sendSms);
        }
    }


    /**
     * 调用阿里批量发送api发送多个号码
     * @param sendSms 发送短信所需信息
     * @return RestfulEntityBySummit结果对象
     */
    public RestfulEntityBySummit sendSmsByJson(SendSms sendSms){
        String result;
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion("2017-05-25");
        request.setAction("SendBatchSms");

        request.putQueryParameter("PhoneNumberJson", JSONUtil.parseObjToJson(sendSms.getPhoneNumbers()));
        request.putQueryParameter("SignNameJson", JSONUtil.parseObjToJson(sendSms.getSignNames()));
        request.putQueryParameter("TemplateCode", sendSms.getTemplateCode());
        request.putQueryParameter("TemplateParamJson", JSONUtil.parseObjToJson(sendSms.getTemplateVars()));

        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("response data is {}",response.getData());
            result = "发送短信完成";
        } catch (ServerException e) {
            log.info("发送短信失败,服务端异常{}",e);
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        } catch (ClientException e) {
            log.info("发送短信失败,客户端异常{}",e);
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        }
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,result,null);
    }

    /**
     * 当所传模板参数为空时，调用阿里单条发送api循环发送多个号码
     * @param sendSms 发送短信所需信息
     * @return RestfulEntityBySummit结果对象
     */
    public RestfulEntityBySummit sendSmsByForeach(SendSms sendSms){
        String result = "";
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        String[] phoneNumbers = sendSms.getPhoneNumbers();
        String[] signNames = sendSms.getSignNames();
        String templateCode = sendSms.getTemplateCode();
        int len  = phoneNumbers.length;
        int failCount = 0;
        for (int i = 0;i < len;i++){
            request.putQueryParameter("PhoneNumbers", phoneNumbers[i]);
            request.putQueryParameter("SignName", signNames[i]);
            request.putQueryParameter("TemplateCode",templateCode);
//            request.putQueryParameter("TemplateParam", "{\"code\": \"6666666\"}");
            try {
                CommonResponse response = client.getCommonResponse(request);
                log.info("response data is {}",response.getData());
            } catch (ServerException e) {
                failCount++;
                log.info("发送短信失败,服务端异常{}",e);
                result = "发送短信失败";

                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
            } catch (ClientException e) {
                failCount++;
                log.info("发送短信失败,客户端异常{}",e);
                result = "发送短信失败";
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
            }
        }
        if(failCount == len){
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        }else {
            result = "发送短信完成";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,result,null);
        }


    }



//    @Override
    public RestfulEntityBySummit sendSmsByOldVersion(SendSms sendSms) {
        List<String> result = new ArrayList<String>();
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendBatchSmsRequest request = new SendBatchSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        request.setPhoneNumberJson(JSONUtil.parseObjToJson(sendSms.getPhoneNumbers()));
        request.setSignNameJson(JSONUtil.parseObjToJson(sendSms.getSignNames()));
        request.setTemplateCode(sendSms.getTemplateCode());
        request.setTemplateParamJson(JSONUtil.parseObjToJson(sendSms.getTemplateVars()));
        SendBatchSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println(sendSmsResponse.getCode());
            System.out.println(sendSmsResponse);
        }

        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,result);
    }


}
