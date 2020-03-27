package com.summit.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summit.common.Common;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.repository.UserRepository;
import com.summit.service.dept.DeptService;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeptUtil  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeptService ds;

    public  JSONObject getSubordinateDeptByPDept(JSONObject paramJson) throws Exception {
        List<String> deptData=new ArrayList<>();
        String pdept=null;
        if(paramJson!=null && paramJson.containsKey("dept") &&  !SummitTools.stringIsNull(paramJson.getString("dept")) ){
            pdept=paramJson.getString("dept");
        }else{
            if(Common.getLogUser().getDepts()!=null && Common.getLogUser().getDepts().length>0){
                pdept=Common.getLogUser().getDepts()[0];
            }
        }
        if(pdept!=null && !SummitTools.stringIsNull(pdept)){
            StringBuffer sql=new StringBuffer("SELECT dept.ID,dept.DEPTNAME  from  sys_dept dept INNER JOIN sys_dept superDept on dept.PID=superDept.id  where dept.PID=? ");
            LinkedMap lm = new LinkedMap();
            lm.put(1, pdept);
            JSONArray jsonArray = userRepository.queryAllCustomJsonArray(sql.toString(), lm);
            if(jsonArray!=null && jsonArray.size()>0){
                for (int i=0;i<jsonArray.size();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject !=null && SummitTools.stringNotNull(jsonObject.getString("ID"))){
                        deptData.add(jsonObject.getString("ID"));
                    }
                }
            }
        }
        JSONObject jsonOject=new JSONObject();
        jsonOject.put("pdept", pdept);
        jsonOject.put("deptData", deptData);
        return jsonOject;
    }

    public  JSONObject getCurrentDeptByPDept(JSONObject paramJson) throws Exception {
        String currentDept=null;
        if(paramJson!=null && paramJson.containsKey("dept") &&  !SummitTools.stringIsNull(paramJson.getString("dept")) ){
            currentDept=paramJson.getString("dept");
        }else{
            if(Common.getLogUser().getDepts()!=null && Common.getLogUser().getDepts().length>0){
                currentDept=Common.getLogUser().getDepts()[0];
            }
        }
        JSONObject jsonOject=new JSONObject();
        jsonOject.put("currentDept", currentDept);
        return jsonOject;
    }

    public JSONObject getAllDeptByPdept(JSONObject paramJson) throws Exception {
        List<String> deptData=null;
        String pdept="";
        if(paramJson!=null && paramJson.containsKey("pdept") &&  !StrUtil.isBlank(paramJson.getString("pdept")) ){
            pdept=paramJson.getString("pdept");
        }else{
            if(Common.getLogUser().getDepts()!=null && Common.getLogUser().getDepts().length>0){
                pdept=Common.getLogUser().getDepts()[0];
            }
        }
        if(pdept!=null && !StrUtil.isBlank(pdept)){
            deptData=ds.getDeptBean(pdept);
        }
        JSONObject jsonOject=new JSONObject();
        jsonOject.put("pdept", pdept);
        jsonOject.put("deptList", deptData);
        return jsonOject;

    }
}
