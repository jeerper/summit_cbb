package com.summit.send.dao;


import com.summit.send.pojo.SmsEntity;

import java.util.List;

public interface SmsDao {

    int insertSms(SmsEntity sms);

    int updateSms(SmsEntity sms);

    String queryTmpalteCodeById(String templateId);

    List<SmsEntity> querySmsRecordByPhone(String resPhone);

    SmsEntity querySmsRecordByBizId(String bizId);

}
