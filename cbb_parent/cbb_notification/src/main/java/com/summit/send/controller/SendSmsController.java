package com.summit.send.controller;

import com.summit.send.pojo.SendSms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/msg")
public class SendSmsController {

    @PostMapping("/sms")
    public SendSms sendSms(@RequestBody SendSms sendSms){
        log.info("==========");
        System.out.println(sendSms);
        return sendSms;
    }
}
