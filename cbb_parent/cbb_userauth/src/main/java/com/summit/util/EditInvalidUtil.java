package com.summit.util;

import com.summit.common.entity.DeptAuditBean;
import com.summit.repository.UserRepository;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditInvalidUtil {
    @Autowired
    private UserRepository ur;


    /**
     *
     * @param deptAuditBean
     * @return  true 说明是无效编辑
     * @throws Exception
     */
    public  boolean editInvalid(DeptAuditBean deptAuditBean) throws Exception {

        StringBuffer sql=new StringBuffer("select * from sys_dept dept where dept.DEPTCODE=? and dept.DEPTNAME=? and dept.DEPTHEAD=? and dept.deptType=? ");
        LinkedMap lm=new LinkedMap();
        lm.put(1,deptAuditBean.getDeptcodeAuth());
        lm.put(2,deptAuditBean.getDeptNameAuth());
        lm.put(3,deptAuditBean.getDeptHeadAuth());
        lm.put(4,deptAuditBean.getDeptTypeAuth());
        if(!SummitTools.stringIsNull(deptAuditBean.getPIdAuth())){
            sql.append(" and dept.PID=? ");
            lm.put(5,deptAuditBean.getPIdAuth());
        }
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        if (null !=jsonObject){
            return true;
        }
        return false;

    }
}
