package com.summit.send.controller;

import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.send.pojo.EmailInfo;
import com.summit.send.pojo.SendEmail;
import com.summit.send.service.SendEmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/msg")
public class SendEmailController {

    @Autowired
    private SendEmailService sendEmailService;

    @ApiOperation(value = "发送邮件，可以添加附件和图片")
    @PostMapping("/email")
    public RestfulEntityBySummit<String> sendEmail(
            EmailInfo emailInfo,
            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestPart MultipartFile[] attachMultipartFiles,
            @ApiParam(value = "邮件图片", allowMultiple = true) @RequestPart MultipartFile[] imagesMultipartFiles){
        log.info("###收到发送邮件请求");
        SendEmail sendEmail = assign(emailInfo,attachMultipartFiles,imagesMultipartFiles);
        System.out.println(emailInfo);
        System.out.println(sendEmail.getContentType());
        System.out.println(sendEmail.getTemplateName());
        System.out.println(sendEmail);
        System.out.println(attachMultipartFiles[0].getContentType());
        System.out.println(imagesMultipartFiles[0].getContentType());
        RestfulEntityBySummit<String> result = sendEmailService.sendMail(sendEmail);
        return result;
    }

    @PostMapping("/test")
    public RestfulEntityBySummit<String> testFile(
            EmailInfo emailInfo,
            @ApiParam(value = "邮件附件", allowMultiple = true) @RequestPart MultipartFile[] attachMultipartFiles,
            @ApiParam(value = "邮件图片", allowMultiple = true) @RequestPart MultipartFile[] imagesMultipartFiles){
        log.info("==========");
        System.out.println(emailInfo);
        System.out.println(attachMultipartFiles[0].getContentType() +" = " + attachMultipartFiles[0].getName() + " = " + attachMultipartFiles[0].getOriginalFilename());
        System.out.println(attachMultipartFiles[1].getContentType() +" = " + attachMultipartFiles[1].getName() +" = " + attachMultipartFiles[1].getOriginalFilename());
        System.out.println(imagesMultipartFiles[0].getContentType() +" = " + imagesMultipartFiles[0].getName() +" = " + imagesMultipartFiles[0].getOriginalFilename());
        return ResultBuilder.buildSuccess();
    }
    @PostMapping("/test2")
    public String testFile2(
            @ApiParam(value = "邮件附件", allowMultiple = true) MultipartFile[] attachMultipartFiles,
            @ApiParam(value = "邮件图片", allowMultiple = true) MultipartFile[] imagesMultipartFiles){
        log.info("==========");
        System.out.println(attachMultipartFiles[0].getContentType() +" = " + attachMultipartFiles[0].getName() + " = " + attachMultipartFiles[0].getOriginalFilename());
        System.out.println(attachMultipartFiles[1].getContentType() +" = " + attachMultipartFiles[1].getName() +" = " + attachMultipartFiles[1].getOriginalFilename());
        System.out.println(imagesMultipartFiles[0].getContentType() +" = " + imagesMultipartFiles[0].getName() +" = " + imagesMultipartFiles[0].getOriginalFilename());
        return "success";
    }
    @PostMapping("/test3")
    public String testFile2(
            @ApiParam(value = "邮件附件", allowMultiple = true) MultipartFile[] attachMultipartFiles){
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

    /**
     * 将emailInfo的各个属性赋值给sendEmail
     * @param emailInfo 不包含附件和图片的邮件信息
     * @param attachMultipartFiles 邮件附件
     * @param imagesMultipartFiles 邮件图片
     * @return 赋值后的SendEmail对象
     */
    private SendEmail assign(EmailInfo emailInfo,MultipartFile[] attachMultipartFiles,MultipartFile[] imagesMultipartFiles){
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
