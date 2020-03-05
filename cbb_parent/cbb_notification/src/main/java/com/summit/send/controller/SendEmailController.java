package com.summit.send.controller;

import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.EmailInfo;
import com.summit.common.entity.notification.SendEmail;
import com.summit.send.service.SendEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("/msg")
@Api(value = "/email", tags = "邮件服务")
public class SendEmailController {

    @Autowired
    private SendEmailService sendEmailService;

    @ApiOperation(value = "发送邮件，(可以添加附件，支持html格式)")
    @PostMapping(value = "/email", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestfulEntityBySummit sendEmail(
            @ApiParam(value = "邮件附件（支持163、qq、sina等常用邮箱）") @RequestPart(value = "attachFiles",required = false) MultipartFile[] attachFiles,
            @ApiParam(value = "邮件编号") @RequestParam(value = "emailId",required = false) String emailId,
            @ApiParam(value = "接收人邮件地址（一个或多个）", required = true) @RequestParam("toEmails") String[] toEmails,
            @ApiParam(value = "邮件标题", required = true) @RequestParam("title") String title,
            @ApiParam(value = "邮件内容", required = true) @RequestParam("content") String content,
            @ApiParam(value = "邮件类型 (文本text、模板template、附件html)", required = true) @RequestParam("contentType") String contentType,
            @ApiParam(value = "模板名称(模板邮件专用，其他类型邮件不填)") @RequestParam(value = "templateName",required = false) String templateName,
            @ApiParam(value = "模板变量(模板邮件专用，其他类型邮件不填)") @RequestParam(value = "templateVars",required = false) String[] templateVars) {
        log.info("###收到发送邮件请求");
        EmailInfo emailInfo = new EmailInfo(emailId, toEmails, title, content, contentType, templateName, templateVars);
        SendEmail sendEmail = assign(emailInfo, attachFiles);
        RestfulEntityBySummit result = sendEmailService.sendMail(sendEmail);
        return result;
    }

    @ApiOperation(value = "发送邮件，(可以添加附件，支持html格式)，异步发送")
    @PostMapping(value = "/email-asynchronization", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestfulEntityBySummit sendEmailAsynchronization(
            @ApiParam(value = "邮件附件（支持163、qq、sina等常用邮箱）") @RequestPart(value = "attachFiles",required = false) MultipartFile[] attachFiles,
            @ApiParam(value = "邮件编号") @RequestParam(value = "emailId",required = false) String emailId,
            @ApiParam(value = "接收人邮件地址（一个或多个）", required = true) @RequestParam("toEmails") String[] toEmails,
            @ApiParam(value = "邮件标题", required = true) @RequestParam("title") String title,
            @ApiParam(value = "邮件内容", required = true) @RequestParam("content") String content,
            @ApiParam(value = "邮件类型 (文本text、模板template、附件html)", required = true) @RequestParam("contentType") String contentType,
            @ApiParam(value = "模板名称(模板邮件专用，其他类型邮件不填)") @RequestParam(value = "templateName",required = false) String templateName,
            @ApiParam(value = "模板变量(模板邮件专用，其他类型邮件不填)") @RequestParam(value = "templateVars",required = false) String[] templateVars) {
        log.info("###收到发送邮件请求");
        EmailInfo emailInfo = new EmailInfo(emailId, toEmails, title, content, contentType, templateName, templateVars);
        SendEmail sendEmail = assign(emailInfo, attachFiles);
        RestfulEntityBySummit result = sendEmailService.sendMailAsynchronization(sendEmail);
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








//    //    @ApiOperation(value = "发送邮件，可以添加附件，支持html和thymeleaf模板格式")
//    @PostMapping(value = "/templateEmail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public RestfulEntityBySummit sendEmail(
//            @RequestPart("attachFiles") MultipartFile[] attachFiles,
//            @RequestParam("emailId") String emailId,
//            @RequestParam("toEmails") String[] toEmails,
//            @RequestParam("title") String title,
//            @RequestParam("content") String content,
//            @RequestParam("contentType") String contentType,
//            @RequestParam("templateName") String templateName,
//            @RequestParam("templateVars") String[] templateVars) {
//
//        log.info("###收到发送邮件请求");
//        EmailInfo emailInfo = new EmailInfo(emailId, toEmails, title, content, contentType, templateName, templateVars);
//        SendEmail sendEmail = assign(emailInfo, attachFiles);
//        RestfulEntityBySummit result = sendEmailService.sendMail(sendEmail);
//
//        return result;
//    }

    @ApiOperation(value = "附件下载")
    @RequestMapping(value = "/downFile", method = RequestMethod.GET)
    @ResponseBody
    public void downFile(
            @ApiParam(value = "文件路径", required = true) @RequestParam(name = "filePath", required = true) String filePath,
            HttpServletResponse response) {
        File file = null;
        FileInputStream inputStream = null;
        ServletOutputStream out = null;
        try {
            file = new File(filePath);

            String fileName = file.getName();
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
            inputStream = new FileInputStream(file);
            // 通过response获取ServletOutputStream对象(out)
            out = response.getOutputStream();
            int bt = 0;
            byte[] buffer = new byte[1024];
            while ((bt = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bt);
            }
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
