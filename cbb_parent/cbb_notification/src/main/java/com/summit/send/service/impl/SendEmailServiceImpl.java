package com.summit.send.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.summit.common.EmailSendResult;
import com.summit.common.EmailSendState;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.MailContentType;
import com.summit.common.entity.notification.SendEmail;
import com.summit.common.util.ResultBuilder;
import com.summit.send.dao.EmailDao;
import com.summit.send.dao.EmailResultDao;
import com.summit.send.pojo.EmailEntity;
import com.summit.send.pojo.EmailResultEntity;
import com.summit.send.pojo.FileInfo;
import com.summit.send.service.FileService;
import com.summit.send.service.SendEmailService;
import com.summit.send.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.writer.UpdaterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender; // = new JavaMailSenderImpl()
    private SimpleMailMessage message = new SimpleMailMessage();
    @Value("${spring.mail.username}")
    private String userNmae;
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    EmailDao emailDao;

    @Autowired
    EmailResultDao emailResultDao;

    private final String ALL_SUCCESS = "all mails sent successfully";
    private final String ALL_FAILURE = "all mails failed to send";

    /**
     * 发送邮件入口
     *
     * @param sendEmail 待发送邮件信息
     * @return 向哪些邮箱发送成功、哪些邮箱失败的消息，封号隔开。全部成功返回全部成功，全部失败返回全部失败
     */
    @Override
    public RestfulEntityBySummit sendMail(SendEmail sendEmail) {
        List<String> result = new ArrayList<String>();
        if (sendEmail == null) {
            log.error("sendEmail is null");
            result.add("mail info is null");
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }
        //判断邮件类型
        if (MailContentType.TEXT.value().equals(sendEmail.getContentType())) {
            return sendSimpleMail(sendEmail);
        } else if (MailContentType.HTML.value().equals(sendEmail.getContentType())) {
            return sendHtmlMail(sendEmail);
        } else if (MailContentType.TEMPLATE.value().equals(sendEmail.getContentType())) {
            return sendTemplateEmail(sendEmail);
        } else {
            log.error("unknown mail type");
            result.add("unknown mail type");
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }
    }

    /**
     * 发送纯文本邮件
     *
     * @param sendEmail 待发送邮件信息
     * @return 向哪些邮箱发送成功、哪些邮箱失败的消息，封号隔开。全部成功返回全部成功，全部失败返回全部失败
     */
    private RestfulEntityBySummit sendSimpleMail(SendEmail sendEmail) {
        List<String> result = new ArrayList<String>();
        message.setFrom(userNmae);
        message.setSubject(sendEmail.getTitle());
        message.setText(sendEmail.getContent());

        //群发
        String[] toEmails = sendEmail.getToEmails();
        //int susCount = 0;
        int failCount = 0;
        for (String to : toEmails) {
            message.setTo(to);
            try {
                javaMailSender.send(message);
                log.info("纯文本的邮件已经发送至【{}】", to);
                result.add("successfully sent text mail to  " + to);
                //susCount++;
            } catch (Exception e) {
                log.error("向【{}】发送纯文本邮件时发生异常{}", to, e);
                result.add("failed to send text mail to " + to);
                failCount++;
            }
        }
        /*if(susCount == toEmails.length){
            result = ALL_SUCCESS;
        }
        if(failCount == toEmails.length){
            result = ALL_FAILURE;
        }*/
        log.info("所有纯文本的邮件已发送");
        if (failCount > 0) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000, result);
    }

    /**
     * 发送html邮件
     *
     * @param sendEmail 待发送邮件信息
     * @return 向哪些邮箱发送成功、哪些邮箱失败的消息，封号隔开。全部成功返回全部成功，全部失败返回全部失败
     */
    private RestfulEntityBySummit sendHtmlMail(SendEmail sendEmail) {
        List<String> result = new ArrayList<String>();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            //true表示需要创建一个multipart message
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(userNmae);
            helper.setSubject(sendEmail.getTitle());
            helper.setText(sendEmail.getContent(), true);

            //若有图片则添加
            //Image[] images = sendEmail.getImages();
//            MultipartFile[] images = sendEmail.getImagesMultipartFiles();
//            if(images != null){
//                for (MultipartFile image : images) {
//                    //FileSystemResource img = new FileSystemResource(new File(image.getPath()));
//                    helper.addInline(image.getOriginalFilename(), image ,image.getContentType());
//                }
//            }
            //若有附件则添加
            //AttachFile[] attachFiles = sendEmail.getAttachFiles();
            MultipartFile[] attachFiles = sendEmail.getAttachFiles();
            if (attachFiles != null) {
                for (MultipartFile attachFile : attachFiles) {
                    //FileSystemResource file = new FileSystemResource(new File(attachFile.getPath()));
                    helper.addAttachment(attachFile.getOriginalFilename(), attachFile);
                }
            }

            log.info("html邮件参数设置成功。");
        } catch (MessagingException e) {
            log.error("设置html邮件参数时发生异常{}", e);
            result.add("error in mail parameters , " + ALL_FAILURE);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }
        //群发
        String[] toEmails = sendEmail.getToEmails();
        //int susCount = 0;
        int failCount = 0;
        for (String to : toEmails) {
            try {
                helper.setTo(to);
                javaMailSender.send(message);
                log.info("html邮件已经发送至【{}】。", to);
                result.add("successfully sent html mail to  " + to);
                //susCount++;
            } catch (MessagingException e) {
                log.error("向【{}】发送html邮件时发生异常{}！", to, e);
                result.add("failed to send html mail to " + to);
                failCount++;
            }

        }
        /*if(susCount == toEmails.length){
            result = ALL_SUCCESS;
        }
        if(failCount == toEmails.length){
            result = ALL_FAILURE;
        }*/
        log.info("所有html邮件已发送");
        if (failCount > 0) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000, result);
    }

    /**
     * 发送模板邮件
     *
     * @param sendEmail 待发送邮件信息
     * @return 向哪些邮箱发送成功、哪些邮箱失败的消息，封号隔开。全部成功返回全部成功，全部失败返回全部失败
     */
    private RestfulEntityBySummit sendTemplateEmail(SendEmail sendEmail) {
        List<String> result = new ArrayList<String>();
        final Context ctx = new Context(new Locale(""));
        String[] templateVars = sendEmail.getTemplateVars();
        if (null != templateVars && templateVars.length > 0) {
            for (String templateVar : templateVars) {
                Map<String, String> keyAndValue = getKeyAndValue(templateVar);
                if (keyAndValue == null)
                    continue;
                ctx.setVariable(keyAndValue.get("key"), keyAndValue.get("value"));
            }
        }
        String htmlContent = templateEngine.process(sendEmail.getTemplateName(), ctx);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(userNmae);
            helper.setSubject(sendEmail.getTitle());
            helper.setText(htmlContent, true);
            //若有图片则添加
            //Image[] images = sendEmail.getImages();
//            MultipartFile[] images = sendEmail.getImagesMultipartFiles();
//            if(images != null){
//                for (MultipartFile image : images) {
//                    //FileSystemResource img = new FileSystemResource(new File(image.getPath()));
//                    helper.addInline(image.getOriginalFilename(), image ,image.getContentType());
//                }
//            }
            //若有附件则添加
            //AttachFile[] attachFiles = sendEmail.getAttachFiles();
            MultipartFile[] attachFiles = sendEmail.getAttachFiles();
            if (attachFiles != null) {
                for (MultipartFile attachFile : attachFiles) {
                    //FileSystemResource file = new FileSystemResource(new File(attachFile.getPath()));
                    helper.addAttachment(attachFile.getOriginalFilename(), attachFile);
                }
            }
            log.info("模板邮件参数设置成功。");
        } catch (MessagingException e) {
            log.error("设置模板邮件参数时发生异常{}", e);
            result.add("error in mail parameters , " + ALL_FAILURE);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }
        String[] toEmails = sendEmail.getToEmails();
        //int susCount = 0;
        int failCount = 0;
        for (String to : toEmails) {
            try {
                helper.setTo(to);
                javaMailSender.send(mimeMessage);
                log.info("模板邮件已成功发送至【{}】。", to);
                result.add("successfully sent template mail to  " + to);
                //susCount++;
            } catch (MessagingException e) {
                log.error("向【{}】发送模板邮件时发生异常{}！", to, e);
                result.add("failed to send template mail to " + to);
                failCount++;
            }
        }
        /*if(susCount == toEmails.length){
            result = ALL_SUCCESS;
        }
        if(failCount == toEmails.length){
            result = ALL_FAILURE;
        }*/

        log.info("所有模板邮件已经发送完成");
        if (failCount > 0) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000, result);
    }

    /**
     * 用冒号分割模板变量，返回map
     *
     * @param templateVar 模板变量，格式为 xxx:yyy
     * @return 返回大小为2的map，key模板变量为模板变量的key，value为模板变量的value
     */
    private Map<String, String> getKeyAndValue(String templateVar) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(templateVar)) {
            log.error("template var is empty");
            return null;
        }

        String[] templateVarArr = templateVar.split(":", 2);
        if (templateVarArr.length != 2) {
            log.error("template var : {} format error", templateVar);
            return null;
        }
        map.put("key", templateVarArr[0]);
        map.put("value", templateVarArr[1]);
        return map;
    }


//----------------------------异步发送邮件------------------------------

    @Override
    public RestfulEntityBySummit sendMailAsynchronization(SendEmail sendEmail) {
        String result = CommonUtils.get32UUID();//生成邮件发送记录的ID
        sendEmail.setEmailId(result);
        if (sendEmail == null) {
            log.error("sendEmail is null");
            result = "mail info is null";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }

        List<FileInfo> fileInfos = null;
        try {
            //获取邮件发送数据(提供给异步发送)
            fileInfos = this.configFileInfo(sendEmail);
        }catch (Exception e){
            log.error("将邮件转换为内存数据时发生异常");
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }

        //判断邮件类型（异步）
        if (MailContentType.TEXT.value().equals(sendEmail.getContentType())) {
            sendSimpleMailAsynchronization(sendEmail);
        } else if (MailContentType.HTML.value().equals(sendEmail.getContentType())) {
            sendHtmlMailAsynchronization(sendEmail,fileInfos);
        } else if (MailContentType.TEMPLATE.value().equals(sendEmail.getContentType())) {
            sendTemplateEmailAsynchronization(sendEmail,fileInfos);
        } else {
            log.error("unknown mail type");
            result = "unknown mail type";
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025, result);
        }
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000, result);
    }

    /**
     * 发送纯文本邮件(异步)
     * @param sendEmail 待发送邮件信息
     */
    private void sendSimpleMailAsynchronization(SendEmail sendEmail) {
        Observable.just(sendEmail)
                .observeOn(Schedulers.io())
                .subscribe(info -> {
                    message.setFrom(userNmae);
                    message.setSubject(sendEmail.getTitle());
                    message.setText(sendEmail.getContent());

                    try {
                        //新增邮件发送记录
                        this.insertEmail(sendEmail);
                    }catch (Exception e){
                        log.error("新增邮件发送记录时发生异常");
                        return;
                    }

                    List<EmailResultEntity> emailResults = new ArrayList<>(sendEmail.getToEmails().length);

                    //群发
                    String[] toEmails = sendEmail.getToEmails();
                    for (String to : toEmails) {
                        Integer sendResult = EmailSendResult.SEND_SUCCESS.getCode();
                        message.setTo(to);
                        try {
                            javaMailSender.send(message);
                            log.info("纯文本的邮件已经发送至【{}】", to);
                        } catch (Exception e) {
                            log.error("向【{}】发送纯文本邮件时发生异常{}", to, e);
                            sendResult = EmailSendResult.SEND_FAIL.getCode();
                        }
                        emailResults.add(this.configEmailResult(sendEmail.getEmailId(),to,sendResult));
                    }

                    if (emailResults.size() > 0){
                        //新增发送结果
                        emailResultDao.insertBatch(emailResults);
                    }

                    //更新邮件发送状态为发送完毕
                    this.updateEmailSendState(sendEmail.getEmailId(),EmailSendState.SEND_END.getCode());
                    log.info("邮件已发送完毕");

        });
    }

    @Autowired
    FileService fileService;

    /**
     * 发送附件邮件
     * @param sendEmail 待发送邮件信息
     * @param fileInfos
     */
    private void sendHtmlMailAsynchronization(SendEmail sendEmail, List<FileInfo> fileInfos) {
        Observable.just(sendEmail)
                .observeOn(Schedulers.io())
                .subscribe(info -> {
                    try {
                        //新增邮件发送记录
                        this.insertEmail(sendEmail);
                    }catch (Exception e){
                        log.error("新增邮件发送记录时发生异常");
                        return;
                    }

                    //邮件参数配置
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = null;
                    try {
                        //true表示需要创建一个multipart message
                        helper = new MimeMessageHelper(message, true);
                        helper.setFrom(userNmae);
                        helper.setSubject(sendEmail.getTitle());
                        helper.setText(sendEmail.getContent(), true);

                        //若有附件则添加
                        if (fileInfos != null && fileInfos.size() > 0){
                            for (FileInfo fileInfo : fileInfos) {
                                String fileName = fileInfo.getFileName();
                                helper.addAttachment(fileInfo.getFileName(),fileInfo.getByteArrayResource());
                            }
                        }

                        log.info("邮件参数设置成功。");
                    } catch (MessagingException e) {
                        log.error("设置邮件参数时发生异常{}", e);
                        //更新邮件发送状态为发送异常
                        this.updateEmailSendState(sendEmail.getEmailId(),EmailSendState.SEND_EXCEPTION.getCode());
                        return;
                    }

                    //上传附件
                    String fileUrl = null;
                    try {
                        log.info("开始上传附件");
                        fileUrl = fileService.addAttachment(fileInfos);
                        log.info("上传附件成功");
                        this.updateEmailFileUrl(sendEmail.getEmailId(),fileUrl);
                    } catch (IOException e) {
                        log.error("上传附件时发生异常{}", e);
                        //更新邮件发送状态为发送异常
                        this.updateEmailSendState(sendEmail.getEmailId(),EmailSendState.SEND_EXCEPTION.getCode());
                        return;
                    }

                    List<EmailResultEntity> emailResults = new ArrayList<>(sendEmail.getToEmails().length);

                    //群发
                    String[] toEmails = sendEmail.getToEmails();
                    for (String to : toEmails) {
                        Integer sendResult = EmailSendResult.SEND_SUCCESS.getCode();
                        try {
                            helper.setTo(to);
                            javaMailSender.send(message);
                            log.info("邮件已经发送至【{}】。", to);
                        } catch (Exception e) {
                            log.error("向【{}】发送html邮件时发生异常{}！", to, e);
                            sendResult = EmailSendResult.SEND_FAIL.getCode();
                        }
                        emailResults.add(this.configEmailResult(sendEmail.getEmailId(),to,sendResult));
                    }

                    if (emailResults.size() > 0){
                        //新增发送结果
                        emailResultDao.insertBatch(emailResults);
                    }

                    //更新邮件发送状态为发送完毕
                    this.updateEmailSendState(sendEmail.getEmailId(),EmailSendState.SEND_END.getCode());
                    log.info("邮件已发送完毕");
                });
    }

    /**
     * 发送模板邮件
     * @param sendEmail 待发送邮件信息
     * @param fileInfos
     */
    private void sendTemplateEmailAsynchronization(SendEmail sendEmail, List<FileInfo> fileInfos) {
        Observable.just(sendEmail)
                .observeOn(Schedulers.io())
                .subscribe(info -> {
                    final Context ctx = new Context(new Locale(""));
                    String[] templateVars = sendEmail.getTemplateVars();
                    if (null != templateVars && templateVars.length > 0) {
                        for (String templateVar : templateVars) {
                            Map<String, String> keyAndValue = getKeyAndValue(templateVar);
                            if (keyAndValue == null)
                                continue;
                            ctx.setVariable(keyAndValue.get("key"), keyAndValue.get("value"));
                        }
                    }
                    String htmlContent = templateEngine.process(sendEmail.getTemplateName(), ctx);

                    try {
                        sendEmail.setContent(htmlContent);
                        //新增邮件发送记录
                        this.insertEmail(sendEmail);
                    }catch (Exception e){
                        log.error("新增邮件发送记录时发生异常");
                        return;
                    }

                    //邮件参数配置
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = null;
                    try {

                        helper = new MimeMessageHelper(mimeMessage, true);
                        helper.setFrom(userNmae);
                        helper.setSubject(sendEmail.getTitle());
                        helper.setText(htmlContent, true);

                        //若有附件则添加
                        if (fileInfos != null && fileInfos.size() > 0){
                            for (FileInfo fileInfo : fileInfos) {
                                String fileName = fileInfo.getFileName();
                                helper.addAttachment(fileInfo.getFileName(),fileInfo.getByteArrayResource());
                            }
                        }

                        log.info("模板邮件参数设置成功。");
                    } catch (MessagingException e) {
                        log.error("设置模板邮件参数时发生异常{}", e);
                        //更新邮件发送状态为发送异常
                        this.updateEmailSendState(sendEmail.getEmailId(),EmailSendState.SEND_EXCEPTION.getCode());
                        return;
                    }

                    //上传附件
                    String fileUrl = null;
                    try {
                        log.info("开始上传附件");
                        fileUrl = fileService.addAttachment(fileInfos);
                        log.info("上传附件成功");
                        this.updateEmailFileUrl(sendEmail.getEmailId(),fileUrl);
                    } catch (IOException e) {
                        log.error("上传附件时发生异常{}", e);
                        //更新邮件发送状态为发送异常
                        this.updateEmailSendState(sendEmail.getEmailId(),EmailSendState.SEND_EXCEPTION.getCode());
                        return;
                    }

                    List<EmailResultEntity> emailResults = new ArrayList<>(sendEmail.getToEmails().length);
                    //群发
                    String[] toEmails = sendEmail.getToEmails();
                    for (String to : toEmails) {
                        Integer sendResult = EmailSendResult.SEND_SUCCESS.getCode();
                        try {
                            helper.setTo(to);
                            javaMailSender.send(mimeMessage);
                            log.info("模板邮件已成功发送至【{}】。", to);
                        } catch (MessagingException e) {
                            log.error("向【{}】发送模板邮件时发生异常{}！", to, e);
                            sendResult = EmailSendResult.SEND_FAIL.getCode();
                        }
                        emailResults.add(this.configEmailResult(sendEmail.getEmailId(),to,sendResult));
                    }

                    if (emailResults.size() > 0){
                        //新增发送结果
                        emailResultDao.insertBatch(emailResults);
                    }

                    //更新邮件发送状态为发送完毕
                    this.updateEmailSendState(sendEmail.getEmailId(),EmailSendState.SEND_END.getCode());
                    log.info("所有模板邮件已经发送完成");
                });
    }

    //新增邮件发送记录
    private void insertEmail(SendEmail sendEmail){
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setId(sendEmail.getEmailId());
        emailEntity.setEmailTitle(sendEmail.getTitle());
        emailEntity.setEmailContent(sendEmail.getContent());
        emailEntity.setSendState(EmailSendState.SEND_ING.getCode());
        emailEntity.setCreateTime(new Date());
        emailEntity.setUpdateTime(new Date());
        emailDao.insert(emailEntity);
    }

    //组装邮件结果实体类
    private EmailResultEntity configEmailResult(String recordId,String to,Integer sendResult){
        EmailResultEntity emailResultEntity = new EmailResultEntity();
        emailResultEntity.setId(CommonUtils.get32UUID());
        emailResultEntity.setRecordId(recordId);
        emailResultEntity.setSendTo(to);
        emailResultEntity.setSendResult(sendResult);
        return emailResultEntity;
    }

    //更新邮件发送状态（即是否已发送完毕）
    private void updateEmailSendState(String emailiId,Integer sendState){
        emailDao.update(null, new UpdateWrapper<EmailEntity>()
                .lambda()
                .set(EmailEntity::getSendState,sendState)
                .eq(EmailEntity::getId,emailiId)
        );
    }

    //更新邮件存储路径
    private void updateEmailFileUrl(String emailiId,String fileUrl){
        emailDao.update(null, new UpdateWrapper<EmailEntity>()
                .lambda()
                .set(EmailEntity::getFileUrl,fileUrl)
                .eq(EmailEntity::getId,emailiId)
        );
    }

    //将附件内容转换至附件实体类中存储
    private List<FileInfo> configFileInfo(SendEmail sendEmail){
        if (sendEmail.getAttachFiles() == null){
            return null;
        }
        List<FileInfo> fileInfos = new ArrayList<>();
        for (MultipartFile multipartFile : sendEmail.getAttachFiles()) {
            try {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setByteArrayResource(new ByteArrayResource(multipartFile.getBytes()));
                fileInfo.setFileName(multipartFile.getOriginalFilename());
                fileInfos.add(fileInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileInfos;
    }



}
