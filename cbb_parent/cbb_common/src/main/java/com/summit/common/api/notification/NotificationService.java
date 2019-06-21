package com.summit.common.api.notification;


import com.summit.common.config.FeignMultipartSupportConfig;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.EmailInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 消息通知组件远程通信接口
 */
@FeignClient(value = "cbb-notification",configuration = FeignMultipartSupportConfig.class)
public interface NotificationService {


    @RequestMapping(value = "/msg/email",method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    RestfulEntityBySummit<String> sendMail(EmailInfo emailInfo, @RequestPart MultipartFile[] attachMultipartFiles, @RequestPart MultipartFile[] imagesMultipartFiles);

//    @PostMapping("/msg/email2")
//    RestfulEntityBySummit<String> sendMail(@RequestPart MultipartFile[] attachMultipartFiles);
//
//    @PostMapping("/msg/email3")
//    RestfulEntityBySummit<String> sendMail(@RequestPart MultipartFile attachMultipartFile);
//
//    @RequestMapping(value = "/sms", method = RequestMethod.POST)
//    RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms);
}
