package com.summit.common.api.notification;

import com.summit.common.config.FeignMultipartSupportConfig;
import com.summit.common.entity.notification.EmailInfo;
import com.summit.common.entity.notification.VerificationSms;
import org.springframework.cloud.openfeign.FeignClient;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendSms;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * 消息通知组件远程通信接口
 */
@FeignClient(value = "cbb-notification", configuration = FeignMultipartSupportConfig.class)
public interface NotificationService {

    @RequestMapping(value = "/msg/sms", method = RequestMethod.POST)
    RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms);

    @PostMapping("/msg/verificationSms")
    RestfulEntityBySummit<Map<String, String>> sendSms(@RequestBody VerificationSms serificationSms);


    @GetMapping("/msg/isCorrectVCode")
    RestfulEntityBySummit<Boolean> isCorrectVCode(@RequestParam("phoneNumber") String phoneNumber,
                                                  @RequestParam(value = "vCode", required = false) String vCode);

    @PostMapping(value = "/msg/email", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestfulEntityBySummit sendEmail(@RequestPart("attachFiles") MultipartFile[] attachFiles,
                                    @RequestParam("emailId") String emailId,
                                    @RequestParam("toEmails") String[] toEmails,
                                    @RequestParam("title") String title,
                                    @RequestParam("content") String content,
                                    @RequestParam("contentType") String contentType);

    @PostMapping(value = "/msg/templateEmail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestfulEntityBySummit sendEmail(@RequestPart("attachFiles") MultipartFile[] attachFiles,
                                    @RequestParam("emailId") String emailId,
                                    @RequestParam("toEmails") String[] toEmails,
                                    @RequestParam("title") String title,
                                    @RequestParam("content") String content,
                                    @RequestParam("contentType") String contentType,
                                    @RequestParam("templateName") String templateName,
                                    @RequestParam("templateVars") String[] templateVars);

    @GetMapping("/msg/querySmsRecordByPhone")
    RestfulEntityBySummit querySmsRecordByPhone(@RequestParam("resPhone") String resPhone);

    @GetMapping("/msg/querySmsRecordByBizId")
    RestfulEntityBySummit querySmsRecordByBizId(@RequestParam("bizId") String bizId);

}
