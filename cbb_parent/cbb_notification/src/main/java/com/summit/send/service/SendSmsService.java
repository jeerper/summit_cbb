package com.summit.send.service;


import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendSms;
import com.summit.send.pojo.SmsEntity;

import java.util.List;

public interface SendSmsService {

    /**
     * 发送短信，新版
     * @param sendSms 短信参数
     * @return 向哪些号码发送成功、哪些号码失败的消息，封号隔开。全部成功返回全部成功，全部失败返回全部失败
     */
    RestfulEntityBySummit sendSms(SendSms sendSms);

    /**
     * 发送短信，旧版
     * @param sendSms 短信参数
     * @return 向哪些号码发送成功、哪些号码失败的消息，封号隔开。全部成功返回全部成功，全部失败返回全部失败
     */
//    RestfulEntityBySummit sendSmsByOldVersion(SendSms sendSms);

    List<SmsEntity> querySmsRecordByPhone(String resPhone);

    SmsEntity querySmsRecordByBizId(String bizId);
}
