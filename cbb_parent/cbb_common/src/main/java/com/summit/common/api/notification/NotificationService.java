package com.summit.common.api.notification;


import com.summit.common.config.FeignMultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 消息通知组件远程通信接口
 */
@FeignClient(value = "cbb-notification",configuration = FeignMultipartSupportConfig.class)
public interface NotificationService {


//    @PostMapping("/msg/email2")
//    RestfulEntityBySummit<String> sendMail(@RequestPart MultipartFile[] attachMultipartFiles);
//
//    @PostMapping("/msg/email3")
//    RestfulEntityBySummit<String> sendMail(@RequestPart MultipartFile attachMultipartFile);
//
//    @RequestMapping(value = "/sms", method = RequestMethod.POST)
//    RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms);
}
