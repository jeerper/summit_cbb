package com.summit.weather.controller;

import com.summit.common.api.notification.NotificationService;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.EmailInfo;
import com.summit.common.entity.notification.SendSms;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "消息组件测试接口", value = "email_test")
@RestController
@RequestMapping("/msg")
public class TestController {
    @Autowired
    private NotificationService notificationService;

    @ApiOperation(value = "测试邮件发送")
    @PostMapping(value = "/sendEmail")
    public RestfulEntityBySummit sendEmail(EmailInfo emailInfo, MultipartFile[] attachFiles) {
        String emailId = emailInfo.getEmailId();
        String[] toEmails = emailInfo.getToEmails();
        String title = emailInfo.getTitle();
        String content = emailInfo.getContent();
        String contentType = emailInfo.getContentType();
        String templateName = emailInfo.getTemplateName();
        String[] templateVars = emailInfo.getTemplateVars();

        RestfulEntityBySummit result = notificationService.sendEmail(attachFiles, emailId, toEmails, title, content, contentType);

        return result;
    }

    @ApiOperation(value = "测试短信发送")
    @PostMapping(value = "/sendSms")
    public RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms) {

        RestfulEntityBySummit result = notificationService.sendSms(sendSms);

        return result;
    }


}
