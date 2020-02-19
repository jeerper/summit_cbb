package com.summit.send.dao;

import com.summit.send.pojo.SmsTemplateEntity;

import java.util.List;

public interface SmsTemplateDao {

    int insertTemplate(SmsTemplateEntity template);

    String queryTmpalteCodeById(String templateId);

    List<SmsTemplateEntity> queryTemplate();

}
