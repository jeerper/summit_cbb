package com.summit.service.dept.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.summit.common.entity.DeptBean;
import com.summit.service.dept.DeptService;
import com.summit.service.dept.DeptsService;
import com.summit.util.DeptUtil;

import com.summit.util.SummitTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DeptsServiceImpl implements DeptsService {
    @Autowired
    private DeptUtil deptUtil;

    @Override
    public String DeptsService() throws Exception {
        JSONObject subordinate_dept = deptUtil.getSubordinateDeptByPDept(null);
        String pdept=subordinate_dept.getString("pdept");
        String deptData = subordinate_dept.getString("deptData");
        if (deptData !=null){
            String[] depts = deptData.split(",");
            List<String> deptList = Arrays.asList(depts);
           // String dept_string = "";
            String dept_string = "'"+pdept+"'";
            if (deptList.size()>0){
                for (int i=0;i<deptList.size();i++){
                    String dept = deptList.get(i);
                    String substring=null;
                    if (dept.contains("[")){
                        int j = dept.indexOf("[");
                        substring = dept.substring(j+1);
                    }
                    if (dept.contains("]")){
                        int j = dept.indexOf("]");
                        substring = dept.substring(0,j-1);
                    }
                    dept_string = dept_string+",'"+substring+"'";

                }
                String ss = dept_string.substring(0,1);
                String es = dept_string.substring(dept_string.length()-1,dept_string.length());
                if(ss.equals("'") && es.equals("'")){
                    return dept_string.substring(1,dept_string.length()-1);
                }else{
                    return null;
                }
            }

            /*if (deptData !=null){
                String adcds = StringUtils.join(deptList.toArray(), ",");
                adcds=adcds.replaceAll(",", "','");
                if (!SummitTools.stringIsNull(adcds)){
                    return  adcds;
                }
            }*/
        }

        return null;
    }

    @Override
    public String getCurrentDeptService() throws Exception {
        JSONObject current_dept = deptUtil.getCurrentDeptByPDept(null);
        String currentDept=current_dept.getString("currentDept");
        String dept_string  = null;
        if (currentDept !=null){
             dept_string = currentDept.replace("''","" );
            /*String ss = dept_string.substring(0,1);
            String es = dept_string.substring(dept_string.length()-1,dept_string.length());
            if(ss.equals("'") && es.equals("'")){
                return dept_string.substring(1,dept_string.length()-1);
            }else{
                return null;
            }*/
            return dept_string;
        }
        return null;
    }

    @Override
    public String getDeptsByPdept(JSONObject paramJson) {
        try{
            JSONObject objct= deptUtil.getAllDeptByPdept(paramJson);
            String pdept=objct.getString("pdept");
            List<DeptBean> deptData=(List)objct.getJSONArray("deptList");
            if(deptData!=null && deptData.size()>0) {
                String depts = "'"+pdept+"'";
                for(int i = 0; i < deptData.size() ; i++){
                    String dept = String.valueOf(deptData.get(i));
                    depts = depts+",'"+ dept+"'";
                }
                String ss = depts.substring(0,1);
                String es = depts.substring(depts.length()-1,depts.length());
                if(ss.equals("'") && es.equals("'")){
                    return depts.substring(1,depts.length()-1);
                }else{
                    return "";
                }
            }else{
                return "";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
