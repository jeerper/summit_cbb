package com.summit.send.controller;

import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.EmailInfo;
import com.summit.common.entity.notification.SendEmail;
import com.summit.send.service.SendEmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/msg")
public class SendEmailController {

    @Autowired
    private SendEmailService sendEmailService;

    @ApiOperation(value = "发送邮件，可以添加附件，支持html格式")
    @PostMapping(value = "/email", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestfulEntityBySummit sendEmail(
            @ApiParam(value = "邮件附件", required = false) @RequestPart("attachFiles") MultipartFile[] attachFiles,
            @ApiParam(value = "邮件编号", required = false) @RequestParam("emailId") String emailId,
            @ApiParam(value = "一个或多个接受者邮件地址", required = true) @RequestParam("toEmails") String[] toEmails,
            @ApiParam(value = "邮件标题", required = true) @RequestParam("title") String title,
            @ApiParam(value = "邮件内容", required = true) @RequestParam("content") String content,
            @ApiParam(value = "邮件类型", required = true) @RequestParam("contentType") String contentType) {

        log.info("###收到发送邮件请求");
        EmailInfo emailInfo = new EmailInfo(emailId, toEmails, title, content, contentType, null, null);
        SendEmail sendEmail = assign(emailInfo, attachFiles);
        RestfulEntityBySummit result = sendEmailService.sendMail(sendEmail);

        return result;
    }


    //    @ApiOperation(value = "发送邮件，可以添加附件，支持html和thymeleaf模板格式")
    @PostMapping(value = "/templateEmail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestfulEntityBySummit sendEmail(
            @RequestPart("attachFiles") MultipartFile[] attachFiles,
            @RequestParam("emailId") String emailId,
            @RequestParam("toEmails") String[] toEmails,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("contentType") String contentType,
            @RequestParam("templateName") String templateName,
            @RequestParam("templateVars") String[] templateVars) {

        log.info("###收到发送邮件请求");
        EmailInfo emailInfo = new EmailInfo(emailId, toEmails, title, content, contentType, templateName, templateVars);
        SendEmail sendEmail = assign(emailInfo, attachFiles);
        RestfulEntityBySummit result = sendEmailService.sendMail(sendEmail);

        return result;
    }


    /**
     * 将emailInfo的各个属性赋值给sendEmail
     *
     * @param emailInfo   不包含附件和图片的邮件信息
     * @param attachFiles 邮件附件
     * @return 赋值后的SendEmail对象
     */
    private SendEmail assign(EmailInfo emailInfo, MultipartFile[] attachFiles) {
        SendEmail sendEmail = new SendEmail();
        sendEmail.setEmailId(emailInfo.getEmailId());
        sendEmail.setToEmails(emailInfo.getToEmails());
        sendEmail.setTitle(emailInfo.getTitle());
        sendEmail.setContent(emailInfo.getContent());
        sendEmail.setContentType(emailInfo.getContentType());
        sendEmail.setTemplateName(emailInfo.getTemplateName());
        sendEmail.setTemplateVars(emailInfo.getTemplateVars());
        sendEmail.setAttachFiles(attachFiles);
//        sendEmail.setImagesMultipartFiles(imagesMultipartFiles);
        return sendEmail;
    }

}
