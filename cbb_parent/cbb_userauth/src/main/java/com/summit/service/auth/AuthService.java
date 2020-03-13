package com.summit.service.auth;

import com.alibaba.fastjson.JSONObject;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.AuthBean;

public interface AuthService {
    Page<AuthBean> queryByPage(Integer page, Integer pageSize, JSONObject paramJson) throws Exception;


}
