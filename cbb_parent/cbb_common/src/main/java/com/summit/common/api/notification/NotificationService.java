package com.summit.common.api.notification;

import com.summit.common.config.FeignMultipartSupportConfig;
import com.summit.common.entity.notification.EmailInfo;
import org.springframework.cloud.openfeign.FeignClient;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendSms;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 消息通知组件远程通信接口
 */
@FeignClient(value = "cbb-notification",configuration = FeignMultipartSupportConfig.class)
public interface NotificationService {

    @RequestMapping(value = "/msg/sms",method = RequestMethod.POST)
    RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms);

    @PostMapping(value = "/msg/email", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestfulEntityBySummit<String> sendEmail(@RequestPart("attachFiles") MultipartFile[] attachFiles ,
                                           @RequestParam("emailId") String emailId ,
                                           @RequestParam("toEmails") String[] toEmails ,
                                           @RequestParam("title") String title ,
                                           @RequestParam("content") String content ,
                                           @RequestParam("contentType") String contentType);

    @PostMapping(value = "/msg/templateEmail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestfulEntityBySummit<String> sendEmail(@RequestPart("attachFiles") MultipartFile[] attachFiles ,
                                           @RequestParam("emailId") String emailId ,
                                           @RequestParam("toEmails") String[] toEmails ,
                                           @RequestParam("title") String title ,
                                           @RequestParam("content") String content ,
                                           @RequestParam("contentType") String contentType ,
                                           @RequestParam("templateName") String templateName ,
                                           @RequestParam("templateVars") String[] templateVars);




}
