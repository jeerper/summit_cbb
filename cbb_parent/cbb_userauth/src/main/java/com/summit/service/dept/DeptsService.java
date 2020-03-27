package com.summit.service.dept;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface DeptsService {

    String  DeptsService() throws Exception;

    String  currentDeptService() throws Exception;

    String getDeptsByPdept(JSONObject paramJson);

}
