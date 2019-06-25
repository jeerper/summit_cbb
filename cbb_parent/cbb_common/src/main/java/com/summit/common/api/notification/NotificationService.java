package com.summit.common.api.notification;

import org.springframework.cloud.openfeign.FeignClient;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.MyIntFile;
import com.summit.common.entity.notification.SendSms;
import feign.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 消息通知组件远程通信接口
 */
@FeignClient(value = "cbb-notification"/*,configuration = FeignMultipartSupportConfig.class*/)
public interface NotificationService {

    @RequestMapping(value = "/msg/sms",method = RequestMethod.POST)
    RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms);

//    @PostMapping("/msg/email2")
   /*@RequestMapping(value = "/msg/test",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    RestfulEntityBySummit<String> sendMail(@RequestPart("emailInfo") EmailInfo emailInfo, @RequestPart("attachMultipartFiles") MultipartFile[] attachMultipartFiles, @RequestPart("imagesMultipartFiles") MultipartFile[] imagesMultipartFiles);*/


    /*@RequestMapping(value = "/msg/email",method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    RestfulEntityBySummit<String> sendMail(@RequestParam("attachMultipartFiles") MultipartFile[] attachMultipartFiles, @RequestParam("imagesMultipartFiles") MultipartFile[] imagesMultipartFiles);*/
    /*@RequestMapping(value = "/msg/test2",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    RestfulEntityBySummit sendMail(EmailInfo emailInfo, @RequestParam("attachMultipartFiles") MultipartFile[] attachMultipartFiles);*/
    //     @RequestMapping(value = "/msg/emai3",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //     RestfulEntityBySummit sendMail(EmailInfo emailInfo, @RequestPart("attachMultipartFiles") MultipartFile[] attachMultipartFiles);

    @RequestMapping(value = "/msg/test3",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    RestfulEntityBySummit sendMail(@RequestPart("attachMultipartFiles") MultipartFile[] attachMultipartFiles);
    @RequestMapping(value = "/msg/test4",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    RestfulEntityBySummit sendMail(@RequestParam("emailInfo") String emailInfo, @RequestPart("attachMultipartFiles") MultipartFile[] attachMultipartFiles);

    @RequestMapping(value = "/msg/test5",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    RestfulEntityBySummit sendMail5(@RequestPart("attachMultipartFiles") MultipartFile[] attachMultipartFiles);

    @RequestMapping(value = "/msg/test6",method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestfulEntityBySummit sendMail(@RequestPart("attachMultipartFiles") MultipartFile[] attachMultipartFiles,@RequestParam("emailId") String emailId);

    @RequestMapping(value = "/msg/test7",method = RequestMethod.POST)
    RestfulEntityBySummit sendMail(@Param("emailInfoAndAttachFiles") Map<String,  ?> emailInfoAndAttachFiles);

    @RequestMapping(value = "/msg/test8",method = RequestMethod.POST)
    RestfulEntityBySummit sendMail8(@Param("emailInfoAndAttachFiles") Map<String,  ?> emailInfoAndAttachFiles);

    @RequestMapping(value = "/msg/test9",method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @RequestLine("POST /msg/test8")
    RestfulEntityBySummit sendMail(@RequestPart("fileObj") MultipartFile fileObj);

    @RequestMapping(value = "/msg/test10",method = RequestMethod.POST)
    RestfulEntityBySummit sendMail(@RequestBody MyIntFile fileObj);

    @RequestMapping(value = "/msg/test11",method = RequestMethod.POST)
    RestfulEntityBySummit sendMail(@RequestParam("aaa") int[] aaa);
    @RequestMapping(value = "/msg/test12",method = RequestMethod.POST)
    RestfulEntityBySummit sendMail(@RequestParam("bytes") byte[] bytes);

    @RequestMapping(value = "/msg/test13",method = RequestMethod.POST)
    RestfulEntityBySummit sendMail(/*@RequestBody */String[] str);

    @RequestMapping(value = "/msg/test14",method = RequestMethod.POST)
    RestfulEntityBySummit sendMail(@RequestParam("str") String str);


//    @RequestMapping(value = "/msg/test6",method = RequestMethod.POST)
//    RestfulEntityBySummit sendMail2(@Param("emailId") String emailId, @Param("attachMultipartFiles") MultipartFile attachMultipartFiles);

    //    @PostMapping("/msg/email2")
//    RestfulEntityBySummit<String> sendMail(@RequestPart MultipartFile[] attachMultipartFiles);
//
//    @PostMapping("/msg/email3")
//    RestfulEntityBySummit<String> sendMail(@RequestPart MultipartFile attachMultipartFile);
//
//    @RequestMapping(value = "/sms", method = RequestMethod.POST)
//    RestfulEntityBySummit sendSms(@RequestBody SendSms sendSms);
}
