package com.summit.send.service;

import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendEmail;


public interface SendEmailService {

    /**
     * 发送邮件
     *
     * @param sendEmail 邮件参数
     * @return 向哪些邮箱发送成功、哪些邮箱失败的消息，封号隔开。全部成功返回全部成功，全部失败返回全部失败
     */
    RestfulEntityBySummit sendMail(SendEmail sendEmail);

    /**
     * 发送短信（异步）
     * @param sendEmail
     * @return
     */
    RestfulEntityBySummit sendMailAsynchronization(SendEmail sendEmail);
}
