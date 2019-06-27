package com.summit.send.controller;

import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.EmailInfo;
import com.summit.common.entity.notification.MyIntFile;
import com.summit.common.entity.notification.SendEmail;
import com.summit.common.util.ResultBuilder;
import com.summit.send.service.SendEmailService;
import feign.Param;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/msg")
public class SendEmailController {

    @Autowired
    private SendEmailService sendEmailService;

//    @ApiOperation(value = "发送邮件，可以添加附件和图片")
//    @PostMapping("/email")
//    public RestfulEntityBySummit<String> sendEmail(
//            EmailInfo emailInfo,
//            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestPart MultipartFile[] attachMultipartFiles,
//            @ApiParam(value = "邮件图片", allowMultiple = true) @RequestPart MultipartFile[] imagesMultipartFiles){
//        log.info("###收到发送邮件请求");
//        SendEmail sendEmail = assign(emailInfo,attachMultipartFiles,imagesMultipartFiles);
//        System.out.println(emailInfo);
//        System.out.println(sendEmail.getContentType());
//        System.out.println(sendEmail.getTemplateName());
//        System.out.println(sendEmail);
//        System.out.println(attachMultipartFiles[0].getContentType());
//        System.out.println(imagesMultipartFiles[0].getContentType());
//        RestfulEntityBySummit<String> result = sendEmailService.sendMail(sendEmail);
//        return result;
//    }
    @ApiOperation(value = "发送邮件，可以添加附件和图片")
    @PostMapping("/email")
    public RestfulEntityBySummit<String> sendEmail(
            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestParam("attachMultipartFiles") MultipartFile[] attachMultipartFiles,
            @ApiParam(value = "邮件图片", allowMultiple = true) @RequestParam("imagesMultipartFiles") MultipartFile[] imagesMultipartFiles){
        log.info("###收到发送邮件请求");


        return  null;
    }

    @PostMapping("/test")
    public RestfulEntityBySummit<String> testFile(
            @RequestPart EmailInfo emailInfo,
            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestPart MultipartFile[] attachMultipartFiles,
            @ApiParam(value = "邮件图片", allowMultiple = true) @RequestPart MultipartFile[] imagesMultipartFiles){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        System.out.println(emailInfo);
        System.out.println(attachMultipartFiles[0].getContentType() +" = " + attachMultipartFiles[0].getName() + " = " + attachMultipartFiles[0].getOriginalFilename());
        System.out.println(attachMultipartFiles[1].getContentType() +" = " + attachMultipartFiles[1].getName() +" = " + attachMultipartFiles[1].getOriginalFilename());
        System.out.println(imagesMultipartFiles[0].getContentType() +" = " + imagesMultipartFiles[0].getName() +" = " + imagesMultipartFiles[0].getOriginalFilename());
        return ResultBuilder.buildSuccess();
    }
    @PostMapping("/test2")
    public String testFile(
            EmailInfo emailInfo,
            @ApiParam(value = "邮件附件", allowMultiple = true) MultipartFile[] attachMultipartFiles){
        log.info("==========");
        System.out.println(emailInfo);
        System.out.println(attachMultipartFiles[0].getContentType() +" = " + attachMultipartFiles[0].getName() + " = " + attachMultipartFiles[0].getOriginalFilename());
        System.out.println(attachMultipartFiles[1].getContentType() +" = " + attachMultipartFiles[1].getName() +" = " + attachMultipartFiles[1].getOriginalFilename());
        return "success";
    }
    @PostMapping("/test3")
    public String testFile(
            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestPart(value = "attachMultipartFiles") MultipartFile[] attachMultipartFiles){
        log.info("==========");
        System.out.println(attachMultipartFiles[0].getContentType() +" = " + attachMultipartFiles[0].getName() + " = " + attachMultipartFiles[0].getOriginalFilename());
        System.out.println(attachMultipartFiles[1].getContentType() +" = " + attachMultipartFiles[1].getName() +" = " + attachMultipartFiles[1].getOriginalFilename());
        try {
            InputStream inputStream = attachMultipartFiles[0].getInputStream();
            int len;
            byte[] arr = new byte[32];
            while((len = inputStream.read(arr)) != -1){
                System.out.println(new String(arr,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }
    @PostMapping("/test4")
    public String testFile(
            String emailInfo,
            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestPart(value = "attachMultipartFiles") MultipartFile[] attachMultipartFiles){
        log.info("==========");
        System.out.println(attachMultipartFiles[0].getContentType() +" = " + attachMultipartFiles[0].getName() + " = " + attachMultipartFiles[0].getOriginalFilename());
        System.out.println(attachMultipartFiles[1].getContentType() +" = " + attachMultipartFiles[1].getName() +" = " + attachMultipartFiles[1].getOriginalFilename());
        try {
            InputStream inputStream = attachMultipartFiles[0].getInputStream();
            int len;
            byte[] arr = new byte[32];
            while((len = inputStream.read(arr)) != -1){
                System.out.println(new String(arr,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }

    //@PostMapping("/test5")
    @RequestMapping(value = "/test5",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String testFile5(
            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestPart(value = "attachMultipartFiles") MultipartFile[] attachMultipartFiles){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        System.out.println(attachMultipartFiles);
        return "success";
    }
    //@PostMapping("/test6")
    @RequestMapping(value = "/test6",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String testFile(
            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestPart(value = "attachMultipartFiles") MultipartFile[] attachMultipartFiles,
            @RequestParam("emailId") String emailId){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        System.out.println(attachMultipartFiles);
        return "success";
    }

    @RequestMapping(value = "/test7",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendMail(@Param("emailInfoAndAttachFiles") Map<String, ?> emailInfoAndAttachFiles){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        System.out.println(emailInfoAndAttachFiles);
        return "success";
    }

    @RequestMapping(value = "/test9",method = RequestMethod.POST)
    public String  sendMail(@Param(value = "fileObj") MultipartFile fileObj){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        return "success";
    }
    @RequestMapping(value = "/test10",method = RequestMethod.POST)
    public String  sendMail(/*@RequestBody*/ MyIntFile fileObj){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        return "success";
    }
    @RequestMapping(value = "/test11",method = RequestMethod.POST)
    public String  sendMail(/*@RequestBody*/ int[] aaa){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        try {
            FileOutputStream fos = new FileOutputStream("F:\\testFile\\aaa\\aaa.png");
            byte[] bytes = new byte[aaa.length];
            for (int i = 0;i < aaa.length;i++){
                bytes[i] = (byte) aaa[i];
                fos.write(bytes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int len;


        return "success";
    }
    @RequestMapping(value = "/test12",method = RequestMethod.POST)
    public String  sendMail(/*@RequestBody*/ byte[] bytes){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        return "success";
    }
    @RequestMapping(value = "/test13",method = RequestMethod.POST)
    public String  sendMail(/*@RequestBody*/ String[] str){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        return "success";
    }
    @RequestMapping(value = "/test14",method = RequestMethod.POST)
    public String  sendMail(/*@RequestBody*/ String str){
        log.info("==========");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        return "success";
    }

    /**
     * 将emailInfo的各个属性赋值给sendEmail
     * @param emailInfo 不包含附件和图片的邮件信息
     * @param attachMultipartFiles 邮件附件
     * @param imagesMultipartFiles 邮件图片
     * @return 赋值后的SendEmail对象
     */
    private SendEmail assign(EmailInfo emailInfo, MultipartFile[] attachMultipartFiles, MultipartFile[] imagesMultipartFiles){
        SendEmail sendEmail = new SendEmail();
        sendEmail.setEmailId(emailInfo.getEmailId());
        sendEmail.setToEmails(emailInfo.getToEmails());
        sendEmail.setTitle(emailInfo.getTitle());
        sendEmail.setContent(emailInfo.getContent());
        sendEmail.setContentType(emailInfo.getContentType());
        sendEmail.setTemplateName(emailInfo.getTemplateName());
        sendEmail.setTemplateVars(emailInfo.getTemplateVars());
        sendEmail.setAttachMultipartFiles(attachMultipartFiles);
        sendEmail.setImagesMultipartFiles(imagesMultipartFiles);
        return sendEmail;
    }

}
