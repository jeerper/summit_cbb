package com.summit.send.dao;

import com.summit.send.pojo.SmsTemplateEntity;

public interface SmsTemplateDao {

    int insertTemplate(SmsTemplateEntity template);

    String queryTmpalteCodeById(String templateId);

}
