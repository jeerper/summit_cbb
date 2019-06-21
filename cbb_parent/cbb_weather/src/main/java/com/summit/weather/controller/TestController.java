package com.summit.weather.controller;

import com.summit.common.api.notification.NotificationService;
import com.summit.common.entity.RestfulEntityBySummit;
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

    @PostMapping("/email")
    public RestfulEntityBySummit sendEmail(EmailInfo emailInfo, MultipartFile[] attachMultipartFiles, MultipartFile[] imagesMultipartFiles){
        RestfulEntityBySummit result = notificationService.sendMail(emailInfo,attachMultipartFiles,imagesMultipartFiles);

        return result;
    }

}
