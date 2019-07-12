package com.summit.send.controller;

import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendSms;
import com.summit.common.util.ResultBuilder;
import com.summit.send.pojo.SmsEntity;
import com.summit.send.service.SendSmsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/msg")
public class SendSmsController {

    @Autowired
    private SendSmsService sendSmsService;

    @ApiOperation(value = "发送短信",  notes = "接收者号码(phoneNumbers)，签名(signNames),模板号(templateCode)都是必输项,其中phoneNumbers和signNames可传多个，且一一对应。返回data为存放号码和bizid的对应关系的map")
    @PostMapping("/sms")
    public RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms){
        log.info("###SendSmsController.sendSms");
        System.out.println(sendSms);
        return sendSmsService.sendSms(sendSms);

    }
    @ApiOperation(value = "根据手机号查询短信发送结果",  notes = "用接收者号码查询，返回data为一个短信记录集合")
    @PostMapping("/querySmsRecordByPhone")
    public RestfulEntityBySummit querySmsRecordByPhone(String resPhone){
        log.info("###SendSmsController.querySmsRecordByPhone");
        List<SmsEntity> smsEntities = sendSmsService.querySmsRecordByPhone(resPhone);
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,smsEntities);

    }
    @ApiOperation(value = "根据短信回执号查询唯一短信发送结果",  notes = "回执id在发送邮件接口返回数据中，返回data为一条确定的短信记录")
    @PostMapping("/querySmsRecordByBizId")
    public RestfulEntityBySummit querySmsRecordByBizId(String bizId){
        log.info("###SendSmsController.querySmsRecordByBizId");
        SmsEntity smsEntity = sendSmsService.querySmsRecordByBizId(bizId);
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000 ,smsEntity);

    }
}
