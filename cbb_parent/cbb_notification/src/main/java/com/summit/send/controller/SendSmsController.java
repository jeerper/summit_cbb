package com.summit.send.controller;

import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendSms;
import com.summit.common.entity.notification.VerificationSms;
import com.summit.common.util.ResultBuilder;
import com.summit.send.pojo.SmsEntity;
import com.summit.send.service.SendSmsService;
import com.summit.send.util.RedisUtil;
import com.summit.send.util.VCodeGenerator;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/msg")
public class SendSmsController {

    @Autowired
    private SendSmsService sendSmsService;

    @Value("${ccb_msg.sms.vcod-timeout}")
    private long vcodTimeout;


    @ApiOperation(value = "发送通知类短信",  notes = "接收者号码(phoneNumbers)，签名(signNames),模板号(templateCode)都是必输项,其中phoneNumbers和signNames可传多个，且一一对应。返回data为存放号码和bizid的对应关系的map")
    @PostMapping("/sms")
    public RestfulEntityBySummit<Map<String,String>> sendSms(@RequestBody SendSms sendSms){
        log.info("###收到发送通知短信请求");
//        System.out.println(sendSms);
        return sendSmsService.sendSms(sendSms);

    }

    @ApiOperation(value = "发送验证码短信",  notes = "接收者号码(phoneNumber)，签名(signName),模板号(templateCode)都是必输项,其中phoneNumbers和signNames可传多个，且一一对应。返回data为存放号码和bizid的对应关系的map")
    @PostMapping("/verificationSms")
    public RestfulEntityBySummit<Map<String,String>> sendSms(@RequestBody VerificationSms serificationSms){

        log.info("###收到发送验证码短信请求");
        //生成6位随机数字验证码
        String verificationCode = VCodeGenerator.getVerificationCode();

        //验证码存入redis并设置超时时间
        String phoneNumber = serificationSms.getPhoneNumber();
        String signName = serificationSms.getSignName();
        String templateCode = serificationSms.getTemplateCode();
        RedisUtil.set(phoneNumber,verificationCode,60L * 24);
        RedisUtil.set(verificationCode, verificationCode, vcodTimeout);

        SendSms sendSms = new SendSms();
        sendSms.setPhoneNumbers(new String[]{phoneNumber});
        sendSms.setSignName(signName);
        sendSms.setTemplateCode(templateCode);
        Map<String,Object> templateVarsMap = new HashMap<>();
        templateVarsMap.put("code",verificationCode);

        sendSms.setTemplateVars(templateVarsMap);

        return sendSmsService.sendSms(sendSms);
    }

    @ApiOperation(value = "验证所传入验证码是否是最近发送给该号码的验证码",  notes = "接收者号码(phoneNumber)，验证码(vCode)，返回数据为true表示验证通过，为false表示校验未通过")
    @GetMapping("/isCorrectVCode")
    public RestfulEntityBySummit<Boolean> isCorrectVCode(@RequestParam("phoneNumber") String phoneNumber,
                                                         @RequestParam(value = "vCode",required = false) String vCode){
        log.info("###收到验证码校验请求");
        if(vCode == null ){
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4026 ,false);
        }
        //去redis根据所传号码查询，若验证码为所传验证码，则正确
        String correctVCode = RedisUtil.getVCodeToRedis(phoneNumber);
        if(correctVCode == null){
            //redis中用手机号获取的验证码为null，说明该手机号在24小时内未发送过验证码
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4029 ,false);
        }
        if(RedisUtil.get(correctVCode) == null){
            //redis中用手机号获取的验证码作为key获取的验证码value为null，说明该手机号发送的验证码已失效
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4028 ,false);
        }

        if(vCode.equals(correctVCode)){
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,"验证码正确", true);
        }
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_4027 ,false);
    }



    @ApiOperation(value = "根据手机号查询短信发送结果",  notes = "用接收者号码查询，返回data为一个短信记录集合")
    @GetMapping("/querySmsRecordByPhone")
    public RestfulEntityBySummit querySmsRecordByPhone(@RequestParam("resPhone") String resPhone){
        log.info("###SendSmsController.querySmsRecordByPhone");
        List<SmsEntity> smsEntities = sendSmsService.querySmsRecordByPhone(resPhone);
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,smsEntities);

    }
    @ApiOperation(value = "根据短信回执号查询短信发送结果",  notes = "回执id在发送邮件接口返回数据中，返回data为一条确定的短信记录")
    @GetMapping("/querySmsRecordByBizId")
    public RestfulEntityBySummit querySmsRecordByBizId(@RequestParam("bizId") String bizId){
        log.info("###SendSmsController.querySmsRecordByBizId");
        SmsEntity smsEntity = sendSmsService.querySmsRecordByBizId(bizId);
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,smsEntity);

    }
    @ApiOperation(value = "直接查询阿里的短信状态记录",  notes = "根据短信回执号直接查询阿里的短信状态，获取短信发送结果,1为发送中，2为发送失败，3为发送成功，-1表示阿里暂未结果记录")
    @GetMapping("/queryStateDirectToAli")
    public RestfulEntityBySummit queryStateDirectToAli(String bizId){
        log.info("###SendSmsController.querySmsRecordByBizId");
        int state = sendSmsService.directToAliQueryState(bizId);
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,state);

    }
}
