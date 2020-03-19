package com.summit.service.dept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.summit.common.Common;
import com.summit.common.entity.DeptAuditBean;
import com.summit.controller.DeptController;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.DeptBean;
import com.summit.common.entity.DeptTreeBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.repository.UserRepository;
import com.summit.util.SummitTools;

import net.sf.json.JSONObject;

@Service
@Transactional
public class DeptService {
    private static final Logger logger = LoggerFactory.getLogger(DeptService.class);
    @Autowired
    private UserRepository ur;
    @Autowired
    private SummitTools st;
    @Autowired
    public JdbcTemplate jdbcTemplate;

    /**
     * 查询部门树
     *
     * @return
     * @throws Exception
     */
    public DeptBean queryDeptTree(String pid) throws Exception {
        //JSONObject jSONOTree=null;
        //DeptBean deptBean=null;
        LinkedMap linkedMap = new LinkedMap();
        StringBuffer sql = new StringBuffer("SELECT ID,PID,DEPTCODE,DEPTNAME,REMARK FROM SYS_DEPT  where 1=1");
        if (pid == null || "".equals(pid)) {
            sql.append(" and (pid is null  or pid='-1' )");
        } else {
            sql.append(" and ID =? ");
            linkedMap.put(1, pid);

        }
        List<Object> rootList = ur.queryAllCustom(sql.toString(), linkedMap);
        if (rootList.size() > 0) {
            String jsonTree = ((JSONObject) rootList.get(0)).toString();
            DeptBean deptBeaneanTree = JSON.parseObject(jsonTree, new TypeReference<DeptBean>() {
            });
            //logger.debug("jSONOTree.getString: "+jSONOTree.getString("ID"));
            List<DeptBean> children = generateOrgMapToTree(null, deptBeaneanTree.getId());
            //logger.debug("list: "+list.size());
            deptBeaneanTree.setChildren(children);
            return deptBeaneanTree;
        }
        //logger.debug("jSONOTree0: "+jSONOTree);
        return null;
    }

    public List<DeptBean> generateOrgMapToTree(Map<String, List<Object>> orgMaps, String pid) throws Exception {
        if (null == orgMaps || orgMaps.size() == 0) {
            StringBuffer sql = new StringBuffer("SELECT DEPT.ID,DEPT.DEPTNAME,DEPT.PID,FDEPT.ID AS CHILD_ID,FDEPT.DEPTCODE AS CHILD_CODE,FDEPT.DEPTNAME AS CHILD_NAME ");
            sql.append(" ,FDEPT.DEPTCODE AS FDEPTCODE FROM SYS_DEPT DEPT INNER JOIN SYS_DEPT FDEPT ON FDEPT.PID= DEPT.ID  ");
            sql.append(" ORDER BY  DEPT.ID ASC,FDEPT.ID ASC ");
            //logger.debug(sql.toString());
            List<Object> list = ur.queryAllCustom(sql.toString(), new LinkedMap());
            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            List<Object> childrenList = new ArrayList<Object>();
            ;
            String id = "";
            int i = 0;
            for (Object s : list) {
                JSONObject JSONObject = (JSONObject) s;
                //logger.debug(JSONObject.getString("ID"));
                if ((!"".equals(id) && !id.equals(JSONObject.getString("ID")))) {
                    map.put(id, childrenList);
                    childrenList = new ArrayList<Object>();
                }
                childrenList.add(JSONObject);
                id = JSONObject.getString("ID");
                if (i == list.size() - 1) {
                    map.put(id, childrenList);
                }
                i++;
            }
            orgMaps = map;
//            String json_list = JSONObject.toJSONString(list);
//            orgMaps = (List<Map<String, Object>>) JSONObject.parse(json_list);
        }
        List<DeptBean> orgList = new ArrayList<>();
        //logger.debug(" 数据:"+orgMaps.size()+"   pid:"+pid);
        if (orgMaps != null && orgMaps.size() > 0) {
            List parenList = orgMaps.get(pid);
            if (parenList == null) {
                return orgList;
            }
            DeptBean deptBean = null;
            for (Object obj : parenList) {
                deptBean = new DeptBean();
                JSONObject json = (JSONObject) obj;
                deptBean.setId(json.containsKey("CHILD_ID") ? json.getString("CHILD_ID") : "");
                deptBean.setDeptCode(json.containsKey("CHILD_CODE") ? json.getString("CHILD_CODE") : "");
                deptBean.setDeptName(json.containsKey("CHILD_NAME") ? json.getString("CHILD_NAME") : "");
                deptBean.setPid(pid);
                //jSONOTree.put("id", json.getString("ID"));
                List<DeptBean> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString());
                //将子结果集存入当前对象的children字段中
                deptBean.setChildren(children);
                //添加当前对象到主结果集中
                orgList.add(deptBean);

            }
        }

        return orgList;
    }

    public DeptTreeBean queryJsonAdcdTree(String pid) throws Exception {
        LinkedMap linkedMap = new LinkedMap();
        StringBuffer sql = new StringBuffer("SELECT ID as value,ID as 'key',PID as pid,DEPTCODE,DEPTNAME as title,REMARK FROM SYS_DEPT  where 1=1");
        if (pid == null || "".equals(pid)) {
            sql.append(" and (pid is null  or pid='-1' )");
        } else {
            sql.append(" and ID =? ");
            linkedMap.put(1, pid);

        }
        List<Object> rootList = ur.queryAllCustom(sql.toString(), linkedMap);
        DeptTreeBean deptTreeBean = null;
        if (rootList.size() > 0) {
            String jsonTree = ((JSONObject) rootList.get(0)).toString();
            deptTreeBean = JSON.parseObject(jsonTree, new TypeReference<DeptTreeBean>() {
            });

            List<DeptBean> list = generateOrgMapToTree(null, deptTreeBean.getValue());
            if (list != null && list.size() > 0) {
                List<DeptTreeBean> detpTreeBeanList = new ArrayList<DeptTreeBean>();
                DeptTreeBean deptTreeBeanInfo = null;
                for (DeptBean deptBean1 : list) {
                    deptTreeBeanInfo = new DeptTreeBean();
                    deptTreeBeanInfo.setValue(deptBean1.getId());
                    deptTreeBeanInfo.setTitle(deptBean1.getDeptName());
                    deptTreeBeanInfo.setPid(deptBean1.getPid());
                    deptTreeBeanInfo.setChildren(getDeptTreeBean(deptBean1.getChildren()));
                    detpTreeBeanList.add(deptTreeBeanInfo);
                }
                deptTreeBean.setChildren(detpTreeBeanList);

            }
        }
        return deptTreeBean;
    }

    private List<DeptTreeBean> getDeptTreeBean(List<DeptBean> children) {
        if (children != null && children.size() > 0) {
            List<DeptTreeBean> DeptTreeBeanList = new ArrayList<DeptTreeBean>();
            DeptTreeBean DeptTreeBean1 = null;
            for (DeptBean deptBean : children) {
                DeptTreeBean1 = new DeptTreeBean();
                DeptTreeBean1.setValue(deptBean.getId());
                DeptTreeBean1.setTitle(deptBean.getDeptName());
                DeptTreeBean1.setPid(deptBean.getPid());
                if (deptBean.getChildren() != null && deptBean.getChildren().size() > 0) {
                    List<DeptTreeBean> adcdChildren = getDeptTreeBean(deptBean.getChildren());
                    if (adcdChildren != null && adcdChildren.size() > 0) {
                        DeptTreeBean1.setChildren(adcdChildren);
                    }
                }
                DeptTreeBeanList.add(DeptTreeBean1);
            }
            return DeptTreeBeanList;
        }
        return null;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     * @throws Exception
     */
    public DeptBean queryById(String id) throws Exception {
        String sql = "SELECT ID,PID,DEPTCODE,DEPTNAME,ADCD,REMARK FROM SYS_DEPT WHERE id = ?";
        LinkedMap linkedMap = new LinkedMap();
        linkedMap.put(1, id);
        List dataList = ur.queryAllCustom(sql, linkedMap);
        if (dataList != null && dataList.size() > 0) {
            DeptBean deptBean = JSON.parseObject(dataList.get(0).toString(), new TypeReference<DeptBean>() {
            });
            return deptBean;
        }
        return null;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     * @throws Exception
     */
    public DeptBean queryDeptAdcdById(String id) throws Exception {
        String sql = "SELECT ID,PID,DEPTCODE,DEPTNAME,DEPT.ADCD,AD.ADNM,DEPT.REMARK FROM SYS_DEPT DEPT LEFT JOIN  SYS_AD_CD AD ON DEPT.ADCD=AD.ADCD  WHERE id = ?";
        LinkedMap linkedMap = new LinkedMap();
        linkedMap.put(1, id);
        List dataList = ur.queryAllCustom(sql, linkedMap);
        if (dataList != null && dataList.size() > 0) {
            DeptBean deptBean = JSON.parseObject(dataList.get(0).toString(), new TypeReference<DeptBean>() {
            });
            return deptBean;
        }
        return null;
    }

    public List<DeptBean> queryDeptByAdcd(String adcd) throws Exception {
        String sql = "SELECT ID,PID,DEPTCODE,DEPTNAME,DEPT.ADCD,AD.ADNM,DEPT.REMARK FROM SYS_DEPT DEPT LEFT JOIN  SYS_AD_CD AD ON DEPT.ADCD=AD.ADCD  WHERE DEPT.adcd = ?";
        LinkedMap linkedMap = new LinkedMap();
        linkedMap.put(1, adcd);
        List dataList = ur.queryAllCustom(sql, linkedMap);
        if (dataList != null && dataList.size() > 0) {
            List<DeptBean> deptList = JSON.parseObject(dataList.toString(), new TypeReference<List<DeptBean>>() {
            });
            return deptList;
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param start
     * @param limit
     * @param paramJson
     * @return
     * @throws Exception
     */
    public Page<DeptBean> queryByPage(int start, int limit, JSONObject paramJson) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT dept.ID,dept.PID,dept.DEPTCODE,dept.DEPTNAME,dept.ADCD,dept.REMARK,us.NAME as deptHead, fdept.DEPTNAME as PDEPTNAME,AD.ADNM FROM SYS_DEPT dept left join SYS_DEPT fdept on dept.pid=fdept.id  ");
        sql.append(" LEFT JOIN sys_user us ON dept.DEPTHEAD=us.USERNAME  ");
        sql.append(" LEFT JOIN SYS_AD_CD AD ON AD.ADCD=DEPT.ADCD where 1=1 ");
        Integer index = 1;
        LinkedMap linkedMap = new LinkedMap();
        if (paramJson != null && !paramJson.isEmpty()) {
            if (paramJson.containsKey("pid") && SummitTools.stringNotNull(paramJson.getString("pid"))) {
                sql.append(" and dept.pid = ? ");
                linkedMap.put(index, paramJson.get("pid"));
                index++;
            }
            if (paramJson.containsKey("deptcode")) {
                sql.append(" and dept.deptcode  like ? ");
                linkedMap.put(index, "%" + paramJson.get("deptcode") + "%");
                index++;
            }
            if (paramJson.containsKey("deptname")) {
                sql.append(" and dept.deptname like ? ");
                linkedMap.put(index, "%" + paramJson.get("deptname") + "%");
                index++;
            }
            if (paramJson.containsKey("adnm")) {
                sql.append(" and AD.ADNM like ? ");
                linkedMap.put(index, "%" + paramJson.get("adnm") + "%");
                index++;
            }
            if (paramJson.containsKey("adcd")) {
                sql.append(" and AD.ADCD = ? ");
                linkedMap.put(index, paramJson.get("adcd"));
                index++;
            }
        }

        Page<Object> rs = ur.queryByCustomPage(sql.toString(), start, limit, linkedMap);
        if (rs != null) {
            ArrayList<DeptBean> depts = JSON.parseObject(rs.getContent().toString(), new TypeReference<ArrayList<DeptBean>>() {
            });
            return new Page<DeptBean>(depts, rs.getPageable());
        }
        return null;
    }


    /**
     * 编辑（保存）
     *
     * @param
     * @return
     */
    public void edit(DeptBean ab) {
        String sql = "UPDATE SYS_DEPT SET  pid = ?, DEPTCODE = ?, DEPTNAME = ?, ADCD=?,REMARK = ?,DEPTHEAD=?  where id = ?";
        jdbcTemplate.update(
                sql,
                ab.getPid(),
                ab.getDeptCode(),
                ab.getDeptName(),
                ab.getAdcd(),
                ab.getRemark(),
                ab.getDeptHead(),
                ab.getId()
        );
        //return ResponseCodeBySummit.CODE_0000;
    }

    /**
     * 新增
     */
    public ResponseCodeEnum add(DeptBean ab) {
        if (st.stringNotNull(ab.getDeptCode())) {
            String hasadcd = "select * from SYS_DEPT where DEPTCODE='" + ab.getDeptCode() + "'";
            List l = ur.queryAllCustom(hasadcd);
            if (l.size() > 0) {
                return ResponseCodeEnum.CODE_9992;
            }
        }

        String sql = "INSERT INTO SYS_DEPT (ID, PID, DEPTCODE,DEPTNAME,ADCD,REMARK,DEPTHEAD,deptType) VALUES (?, ? ,?, ?,?,?,?,?)";
        jdbcTemplate.update(
                sql,
                SummitTools.getKey(),
                ab.getPid(),
                ab.getDeptCode(),
                ab.getDeptName(),
                ab.getAdcd(),
                ab.getRemark(),
                ab.getDeptHead(),
                ab.getDeptType());
        return null;
    }


    /**
     * 删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    public ResponseCodeEnum del(String ids) throws Exception {
        ids = "'" + ids.replaceAll(",", "','") + "'";
        String sql = "select padcd, count(*) from SYS_AD_CD where padcd in (" + ids + ") group by padcd";
        LinkedMap linkedMap = new LinkedMap();
        // linkedMap.put(1,ids);
        List<Object> list = ur.queryAllCustom(sql, linkedMap);
        if (list == null || list.size() == 0) {
            String updateSql = "DELETE FROM SYS_DEPT WHERE id IN (" + ids + ") ";
            jdbcTemplate.update(updateSql);
            return null;
        } else {
            return ResponseCodeEnum.CODE_0000;
        }
    }

    @Transactional
    public ResponseCodeEnum editAudit(DeptAuditBean deptAuditBean) throws Exception {
        JSONObject old_dept=queryOldDeptByDeptId(deptAuditBean.getDeptIdAuth());
        JSONObject old_dept_record= queryDeptRecordByDeptId(deptAuditBean.getDeptIdAuth());
        if (null ==old_dept_record ){
            boolean b =insertSysDeptRecord(old_dept);
            if (!b){
                return ResponseCodeEnum.CODE_9999;
            }
        }else {
            boolean b =updateSysDeptRecord(old_dept);
            if (!b){
                return ResponseCodeEnum.CODE_9999;
            }
        }
        String sql="INSERT INTO sys_dept_auth(id,deptId_auth,pId_auth,deptcode_auth,deptName_auth,adcd_auth,auth_person,isAudited,auth_time,submitted_to,remark,apply_name,deptType_auth,deptHead_auth ) VALUES " +
                "(?,?,?,?,?,?,?,?,now(),?,?,?,?,?) ";
        //上级部门
        JSONObject jsonObject=queryBySuperDeptByDeptId(deptAuditBean.getDeptIdAuth());
        String  superDept=null;
        if (jsonObject !=null && !SummitTools.stringIsNull( jsonObject.getString("ID"))){
            superDept=jsonObject.getString("ID");
        }
        String idStr = IdWorker.getIdStr();
        try{
            jdbcTemplate.update(sql,
                    idStr,
                    deptAuditBean.getDeptIdAuth(),
                    deptAuditBean.getPIdAuth(),
                    deptAuditBean.getDeptcodeAuth(),
                    deptAuditBean.getDeptNameAuth(),
                    deptAuditBean.getAdcdAuth(),
                    null,
                    "0",
                    superDept,
                    deptAuditBean.getRemark(),
                    Common.getLogUser().getUserName(),
                    deptAuditBean.getDeptTypeAuth(),
                    deptAuditBean.getDeptHeadAuth()
            );
            //修改用户表中的audit字段为发起申请
            StringBuffer sql2=new StringBuffer("UPDATE sys_dept SET isAudited = ? where ID=? ");
            jdbcTemplate.update(sql2.toString(),"0", deptAuditBean.getDeptIdAuth());
        }catch (Exception e){
            logger.error("修改部门失败:", e);
            return ResponseCodeEnum.CODE_9999;
        }
        //保存审核表
        String sql_auth="INSERT INTO sys_auth(id,apply_name,apply_type,submitted_to,isAudited,apply_time,apply_Id ) VALUES (?,?,?,?,?,now(),?) ";
        try{
            jdbcTemplate.update(sql_auth,
                    IdWorker.getIdStr(),
                    Common.getLogUser().getUserName(),
                    "0",
                    superDept,
                    "0",
                    idStr
            );

        }catch (Exception e){
            logger.error("修改部门失败:", e);
            return ResponseCodeEnum.CODE_9999;
        }
        return null;

    }

    private boolean updateSysDeptRecord(JSONObject old_dept) {
        String sql = "UPDATE sys_dept_record SET  pid = ?, deptcode = ?, deptName = ?, adcd=?, deptHead = ?,deptType=?  where id = ?";
        try{
            jdbcTemplate.update(
                    sql,
                    old_dept.containsKey("PID") ? old_dept.getString("PID") : null,
                    old_dept.containsKey("DEPTCODE") ? old_dept.getString("DEPTCODE") : null,
                    old_dept.containsKey("DEPTNAME") ? old_dept.getString("DEPTNAME") : null,
                    old_dept.containsKey("ADCD") ? old_dept.getString("ADCD") : null,
                    old_dept.containsKey("DEPTHEAD") ? old_dept.getString("DEPTHEAD") : null,
                    old_dept.containsKey("deptType") ? old_dept.getString("deptType") : null,
                    old_dept.containsKey("ID") ? old_dept.getString("ID") : null
            );

        }catch (Exception e){
            logger.error("保存部门记录表失败:", e);
            return false;
        }
        return true;
    }

    //保存部门记录表
    private boolean insertSysDeptRecord(JSONObject old_dept) {
        String sql_dept_record="INSERT INTO sys_dept_record(id,pId,deptcode,deptName,adcd,deptHead,deptType ) VALUES (?,?,?,?,?,?,?) ";
        try{
            jdbcTemplate.update(sql_dept_record,
                    old_dept.containsKey("ID") ? old_dept.getString("ID") : null,
                    old_dept.containsKey("PID") ? old_dept.getString("PID") : null,
                    old_dept.containsKey("DEPTCODE") ? old_dept.getString("DEPTCODE") : null,
                    old_dept.containsKey("DEPTNAME") ? old_dept.getString("DEPTNAME") : null,
                    old_dept.containsKey("ADCD") ? old_dept.getString("ADCD") : null,
                    old_dept.containsKey("DEPTHEAD") ? old_dept.getString("DEPTHEAD") : null,
                    old_dept.containsKey("deptType") ? old_dept.getString("deptType") : null
            );

        }catch (Exception e){
            logger.error("保存部门记录表失败:", e);
            return false;
        }
        return true;

    }

    private JSONObject queryDeptRecordByDeptId(String deptIdAuth) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT * from sys_dept_record dr where dr.id=? ");
        LinkedMap lm=new LinkedMap();
        lm.put(1,deptIdAuth);
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return jsonObject;
    }

    private JSONObject queryOldDeptByDeptId(String deptIdAuth) throws Exception {
        StringBuffer queryOldDept_sql=new StringBuffer("SELECT  dept.ID,dept.PID,dept.DEPTCODE ,dept.DEPTNAME,dept.ADCD,dept.DEPTHEAD,dept.deptType from sys_dept dept where dept.ID=? ");
        LinkedMap lm=new LinkedMap();
        lm.put(1,deptIdAuth);
        JSONObject jsonObject = ur.queryOneCustom(queryOldDept_sql.toString(), lm);
        return jsonObject;
    }

    //查找上级部门
    private JSONObject queryBySuperDeptByDeptId(String deptIdAuth) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT superDept.ID,superDept.DEPTNAME from sys_dept dept INNER JOIN sys_dept superDept on dept.PID=superDept.ID where dept.ID=? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, deptIdAuth);
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return  jsonObject;

    }

}
