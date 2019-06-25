package com.summit.send.controller;

import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendSms;
import com.summit.common.util.ResultBuilder;
import com.summit.send.service.SendSmsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/msg")
public class SendSmsController {

    @Autowired
    private SendSmsService sendSmsService;

    @ApiOperation(value = "发送短信",  notes = "接收者号码(phoneNumbers)，签名(signNames),模板号(templateCode)都是必输项,其中phoneNumbers和signNames可传多个，且一一对应")
    @PostMapping("/sms")
    public RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms){
        log.info("==========");
        System.out.println(sendSms);
        return sendSmsService.sendSms(sendSms);
    }
}
