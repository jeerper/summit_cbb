package com.summit.send.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.summit.send.dao.SmsDao;
import com.summit.send.dao.SmsTemplateDao;
import com.summit.send.pojo.SmsEntity;
import com.summit.send.service.SendSmsService;
import com.summit.send.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class SendSmsServiceImpl implements SendSmsService {

    @Value("${ccb_msg.sms.access-key-id}")
    private String accessKeyId;
    @Value("${ccb_msg.sms.access-key-secret}")
    private String secret;
    @Value("${ccb_msg.sms.period}")
    private int period;
    @Value("${ccb_msg.sms.max-polling-count}")
    private int maxPollingCount;


    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    @Autowired
    private SmsDao smsDao;
    @Autowired
    private SmsTemplateDao smsTemplateDao;

    @Override
    public RestfulEntityBySummit sendSms(SendSms sendSms) {
//        if(sendSms.getTemplateVars() == null || sendSms.getTemplateVars().size() == 0){
//            return sendSmsByForeach(sendSms);
//        }else {
//            return sendSmsByJson(sendSms);
//        }
        return sendSmsByForeach(sendSms);
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
            String responseData = response.getData();
            Map<String , Object> repMap = JSONUtil.parseJsonToMap(responseData);
            if("OK".equals(repMap.get("Code"))){
                log.info("返回数据为 {}", responseData);
                result = "发送短信完成";
            }else{
                log.error("返回数据为 {}", responseData);
                result = "发送短信失败";
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
            }

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
        if(sendSms == null){
            log.error("短信不能为空");
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        }
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        String[] phoneNumbers = sendSms.getPhoneNumbers();
        if(phoneNumbers == null || phoneNumbers.length == 0){
            log.error("号码不能为空");
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        }
        int len  = phoneNumbers.length;
        String[] signNames = sendSms.getSignNames();
        if(signNames == null || signNames.length == 0){
            log.error("短信签名不能为空");
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        }
        if(signNames.length != len){
            log.error("短信签名个数与号码个数不匹配");
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        }
        String templateCode = sendSms.getTemplateCode();
        //名字暂未改，实际传过来的应该是templateId,去数据库查询出templateCode
        templateCode = smsTemplateDao.queryTmpalteCodeById(templateCode);
        if(StringUtils.isEmpty(templateCode)){
            log.error("模板号不能为空");
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        }
        List<Map<String, Object>> templateVars = sendSms.getTemplateVars();
        if(templateVars != null && templateVars.size() != len){
            log.error("模板变量组数与号码个数不匹配");
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,null);
        }
        int failCount = 0;
        //用于存放号码和bizid的对应关系
        Map<String,String> bizIdMap = new HashMap<>();
        for (int i = 0;i < len;i++){
            String phone = phoneNumbers[i];
            request.putQueryParameter("PhoneNumbers", phone);
            String signName = signNames[i];
            request.putQueryParameter("SignName", signName);
            request.putQueryParameter("TemplateCode",templateCode);
//            request.putQueryParameter("TemplateParam", "{\"code\": \"6666666\"}");
            Map<String, Object> varsMap;

            if(templateVars != null && (varsMap = templateVars.get(i)) != null){
                request.putQueryParameter("TemplateParam",JSONUtil.parseObjToJson(varsMap));
            }
            try {
                CommonResponse response = client.getCommonResponse(request);
                String responseData = response.getData();
                Map<String , Object> repMap = JSONUtil.parseJsonToMap(responseData);
                if("OK".equals(repMap.get("Code"))){
                    log.info("返回数据为 {}", responseData);
//                    result = "发送短信完成";
                    //TODO 插库
                    SmsEntity smsEntity = new SmsEntity();
                    String bizId = (String) repMap.get("BizId");
                    bizIdMap.put(phone,bizId);
                    //目前默认将smsId设为bizId
                    smsEntity.setSmsId(bizId);
                    smsEntity.setBizId(bizId);
                    //状态为发送中
                    smsEntity.setSendState(1);
                    //目前将传过来的templateCode当做templateId
                    smsEntity.setTemplateId(sendSms.getTemplateCode());
                    smsEntity.setResPhone(phone);
                    smsEntity.setSmsSignname(signName);
                    smsEntity.setSmsContent("");
                    Date date = new Date();
                    smsEntity.setCreateTime(date);
                    smsEntity.setUpdateTime(date);
                    smsDao.insertSms(smsEntity);
                    //发送完成后启动定时任务，查询阿里短信接口，获取发送结果
                    Timer timer = new Timer();

                    TimerTask timerTask = new TimerTask() {
//                        private int count = 0;
                        private ThreadLocal<Integer> count = new ThreadLocal<Integer>(){
                            @Override
                            protected Integer initialValue() {
                                return 0;
                            }
                        };
                        @Override
                        public void run() {
                            /*DefaultProfile profile = DefaultProfile.getProfile("default", "<accessKeyId>", "<accessSecret>");
                            IAcsClient client = new DefaultAcsClient(profile);*/
                            count.set(count.get() + 1);;
                            if(count.get() > maxPollingCount)
                                timer.cancel();
                            try {
                                CommonRequest queryRequest = new CommonRequest();
                                queryRequest.setMethod(MethodType.POST);
                                queryRequest.setDomain("dysmsapi.aliyuncs.com");
                                queryRequest.setVersion("2017-05-25");
                                queryRequest.setAction("QuerySendDetails");
                                queryRequest.putQueryParameter("PhoneNumber", phone);
                                String queryDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                                queryRequest.putQueryParameter("BizId", bizId);
                                queryRequest.putQueryParameter("SendDate", queryDate);
                                queryRequest.putQueryParameter("PageSize", "1");
                                queryRequest.putQueryParameter("CurrentPage", "1");
                                CommonResponse response = client.getCommonResponse(queryRequest);
                                log.info(response.getData());
                                JSONArray results = JSONUtil.parseResponseDataToMap(response.getData());

                                if(results.size() == 0){
                                    log.info("暂无结果");
                                    return;
                                }
                                //如果成功或失败，则插库并停止定时任务。若是发送中，则继续查询
                                JSONObject result = (JSONObject)results.get(0);
                                int sendStatus = result.getInteger("SendStatus");
                                String content = result.getString("Content");
                                if(sendStatus == 3){
                                    log.info("发送成功");
                                    //更新状态和内容
                                    SmsEntity sms = new SmsEntity();
                                    sms.setBizId(bizId);
                                    sms.setSendState(3);
                                    sms.setSmsContent(content);
                                    sms.setUpdateTime(new Date());
                                    smsDao.updateSms(sms);
                                    timer.cancel();
                                }else if(sendStatus == 2){
                                    log.info("发送失败");
                                    //更新状态
                                    SmsEntity sms = new SmsEntity();
                                    sms.setBizId(bizId);
                                    sms.setSendState(2);
                                    sms.setSmsContent(content);
                                    sms.setUpdateTime(new Date());
                                    smsDao.updateSms(sms);
                                    timer.cancel();
                                }else if(sendStatus == 1){
                                    log.info("发送中...");
                                }

                            } catch (ClientException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    timer.schedule(timerTask,50,period);
                }else{
                    log.error("返回数据为 {}", responseData);
//                    result = "发送短信失败";
                    failCount++;
                }

            } catch (ServerException e) {
                failCount++;
                log.error("发送短信失败,服务端异常{}",e);
            } catch (ClientException e) {
                failCount++;
                log.error("发送短信失败,客户端异常{}",e);
            }
        }
        if(failCount == len){
            result = "发送短信失败";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025 ,result,bizIdMap);
        }
        result = "发送短信完成";
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,result,bizIdMap);
    }

    public int directToAliQueryState(String bizId){
        SmsEntity smsRecord = smsDao.querySmsRecordByBizId(bizId);

        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest queryRequest = new CommonRequest();
        queryRequest.setMethod(MethodType.POST);
        queryRequest.setDomain("dysmsapi.aliyuncs.com");
        queryRequest.setVersion("2017-05-25");
        queryRequest.setAction("QuerySendDetails");
        queryRequest.putQueryParameter("PhoneNumber", smsRecord.getResPhone());
        String queryDate = new SimpleDateFormat("yyyyMMdd").format(smsRecord.getUpdateTime());
        queryRequest.putQueryParameter("BizId", bizId);
        queryRequest.putQueryParameter("SendDate", queryDate);
        queryRequest.putQueryParameter("PageSize", "-1");
        queryRequest.putQueryParameter("CurrentPage", "-1");
        CommonResponse response = null;
        try {
            response = client.getCommonResponse(queryRequest);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        log.info(response.getData());
        JSONArray results = JSONUtil.parseResponseDataToMap(response.getData());

        if(results.size() == 0){
            log.info("暂无结果");
            return -1;
        }
        //如果成功或失败，则插库并停止定时任务。若是发送中，则继续查询
        JSONObject result = (JSONObject)results.get(0);
        int sendStatus = result.getInteger("SendStatus");
        if(sendStatus == 2 || sendStatus == 3){
            //若查询出成功或失败，更新数据库
            SmsEntity sms = new SmsEntity();
            sms.setBizId(bizId);
            sms.setSendState(sendStatus);
            sms.setSmsContent(result.getString("Content"));
            sms.setUpdateTime(new Date());
            smsDao.updateSms(sms);
        }
        return sendStatus;
    }

//    @Override
//    public RestfulEntityBySummit sendSmsByOldVersion(SendSms sendSms) {
//        List<String> result = new ArrayList<String>();
//        //可自助调整超时时间
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//
//        //初始化acsClient,暂不支持region化
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
//        try {
//            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//        //组装请求对象
//        SendBatchSmsRequest request = new SendBatchSmsRequest();
//        //使用post提交
//        request.setMethod(MethodType.POST);
//        request.setPhoneNumberJson(JSONUtil.parseObjToJson(sendSms.getPhoneNumbers()));
//        request.setSignNameJson(JSONUtil.parseObjToJson(sendSms.getSignNames()));
//        request.setTemplateCode(sendSms.getTemplateCode());
//        request.setTemplateParamJson(JSONUtil.parseObjToJson(sendSms.getTemplateVars()));
//        SendBatchSmsResponse sendSmsResponse = null;
//        try {
//            sendSmsResponse = acsClient.getAcsResponse(request);
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
//            //请求成功
//            log.info(sendSmsResponse.getCode());
//        }
//
//        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,result);
//    }

    public List<SmsEntity> querySmsRecordByPhone(String resPhone){
        return smsDao.querySmsRecordByPhone(resPhone);
    }

    public SmsEntity querySmsRecordByBizId(String bizId){
        return smsDao.querySmsRecordByBizId(bizId);
    }
}
