package com.summit.service.dept;

import com.alibaba.fastjson.JSONObject;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.DeptAuditBean;
import com.summit.common.entity.ResponseCodeEnum;

public interface DeptAuditService {

    ResponseCodeEnum deptAudit(String id, String deptIdAuth, String isAudited) throws Exception;

    Page<DeptAuditBean> queryByPage(Integer page, Integer pageSize, JSONObject paramJson) throws Exception;

}
