package com.summit.service.dept;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface DeptsService {


    //查找当前登录人所在的部门以及下级部门(一层关系)
    String  DeptsService() throws Exception;


     //查找当前登录人所在的部门
    String  getCurrentDeptService() throws Exception;

    //查找当前登录人所在的部门以及子部门(多层关系)
    String getDeptsByPdept(JSONObject paramJson);


    /**
     * 根据pdept查询下面所有的子节点(包括父节点、多级)
     * @param paramJson
     * @return
     */
    List<String> getAllDeptByPdept(JSONObject paramJson);



    /**
     * 根据pdept查询下面所有的子节点(不包括父节点、多级)
     * @param paramJson
     * @return
     */
    List<String> getAllDeptExcludeParentNodeByPdept(JSONObject paramJson);

}
