package com.summit.service.auth;

import com.alibaba.fastjson.JSONObject;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.AuthBean;

import java.util.List;
import java.util.Map;

public interface AuthService {
    Page<AuthBean> queryByPage(Integer page, Integer pageSize, JSONObject paramJson) throws Exception;

    Map<String,Object> findById(String id) throws Exception;

    int authByIdBatch(List<String> authIds, String isAudited) throws Exception;

    void delAlarmByIdBatch(List<String> authIds) throws Exception;
}
