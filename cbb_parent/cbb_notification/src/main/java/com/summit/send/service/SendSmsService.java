package com.summit.send.service;


import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.notification.SendSms;
import com.summit.send.pojo.SmsEntity;
import com.summit.send.pojo.SmsTemplateEntity;

import java.util.List;
import java.util.Map;

public interface SendSmsService {

    /**
     * 发送短信，新版
     *
     * @param sendSms 短信参数
     * @return 返回发送号码和bizId的键值对map
     */
    RestfulEntityBySummit<Map<String, String>> sendSms(SendSms sendSms);

    /**
     * 发送短信，旧版
     *
     * @param sendSms 短信参数
     * @return 向哪些号码发送成功、哪些号码失败的消息，封号隔开。全部成功返回全部成功，全部失败返回全部失败
     */
//    RestfulEntityBySummit sendSmsByOldVersion(SendSms sendSms);

    List<SmsEntity> querySmsRecordByPhone(String resPhone);

    SmsEntity querySmsRecordByBizId(String bizId);

    int directToAliQueryState(String bizId);

    /**
     * 查询所有短信模板信息
     * @return
     */
    List<SmsTemplateEntity> queryTemplate();

}
