package com.summit.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summit.common.Common;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.repository.UserRepository;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeptUtil  {

    @Autowired
    private UserRepository userRepository;

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
            StringBuffer sql=new StringBuffer("SELECT dept.ID,dept.DEPTNAME  from  sys_dept superDept INNER JOIN sys_dept dept on superDept.PID=dept.id  where superDept.PID=? ");
            LinkedMap lm = new LinkedMap();
            lm.put(1, pdept);
            JSONArray jsonArray = userRepository.queryAllCustomJsonArray(sql.toString(), lm);
            if(jsonArray!=null && jsonArray.size()>0){
                for (int i=0;i<jsonArray.size();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject !=null && !SummitTools.stringNotNull(jsonObject.getString("ID"))){
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
}
