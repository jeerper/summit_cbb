package com.summit.weather.controller;

import com.summit.common.api.notification.NotificationService;
import com.summit.common.entity.notification.EmailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/msg")
public class TestController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/sendEmail")
    public String sendEmail(EmailInfo emailInfo, MultipartFile[] attachFiles){
        String emailId = emailInfo.getEmailId();
        String[] toEmails = emailInfo.getToEmails();
        String title = emailInfo.getTitle();
        String content = emailInfo.getContent();
        String contentType = emailInfo.getContentType();
        String templateName = emailInfo.getTemplateName();
        String[] templateVars = emailInfo.getTemplateVars();

        notificationService.sendEmail(attachFiles,emailId,toEmails,title,content,contentType,templateName,templateVars);

        return "success";
    }


}
