package com.summit.service.dept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.domain.adcd.ADCDBean;
import com.summit.domain.dept.DeptBean;
import com.summit.domain.dept.DeptBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;

import net.sf.json.JSONObject;

@Service
@Transactional
public class DeptService {
	private static final Logger logger = LoggerFactory.getLogger(DeptService.class);
	@Autowired
	private UserRepository ur;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private SummitTools st;
	@Autowired
	private DeptBeanRowMapper atm;
	/**
	 * 
	 * 查询部门树
	 * @return
	 * @throws Exception 
	 */
	public DeptBean queryDeptTree(String pid) throws Exception {
		//JSONObject jSONOTree=null;
		//DeptBean deptBean=null;
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT ID,PID,DEPTCODE,DEPTNAME,REMARK FROM SYS_DEPT  where 1=1");
		if(pid==null || "".equals(pid)){
			sql.append(" and (pid is null  or pid='-1' )");
		}else{
			sql.append(" and PID =? ");
			linkedMap.put(1, pid);
			
		}
		List<Object> rootList=ur.queryAllCustom(sql.toString(),linkedMap);
		if(rootList.size()>0){
			String jsonTree=((JSONObject)rootList.get(0)).toString();
			DeptBean deptBeaneanTree = JSON.parseObject(jsonTree, new TypeReference<DeptBean>() {});
			//logger.debug("jSONOTree.getString: "+jSONOTree.getString("ID"));
			List<DeptBean> children=generateOrgMapToTree(null,deptBeaneanTree.getId());
			//logger.debug("list: "+list.size());
			deptBeaneanTree.setChildren(children);
			return deptBeaneanTree;
		}
		//logger.debug("jSONOTree0: "+jSONOTree);
		return null;
	}
	
   public List<DeptBean> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid) throws Exception {
        if (null == orgMaps || orgMaps.size() == 0) {
        	StringBuffer sql = new StringBuffer("SELECT DEPT.ID,DEPT.DEPTNAME,DEPT.PID,FDEPT.ID AS CHILD_ID,FDEPT.DEPTCODE AS CHILD_CODE,FDEPT.DEPTNAME AS CHILD_NAME ");
        	sql.append(" ,FDEPT.DEPTCODE AS FDEPTCODE FROM SYS_DEPT DEPT INNER JOIN SYS_DEPT FDEPT ON FDEPT.PID= DEPT.ID  ");
        	sql.append(" ORDER BY  DEPT.ID ASC,FDEPT.ID ASC ");
        	//logger.debug(sql.toString());
        	List<Object> list=ur.queryAllCustom(sql.toString(),new LinkedMap());
    		Map<String, List<Object>> map=new HashMap<String, List<Object>>();
    		List<Object> childrenList=new ArrayList();;
    		String id="";
    		int i=0;
    		for(Object s:list){
    			JSONObject JSONObject=(JSONObject)s;
    			//logger.debug(JSONObject.getString("ID"));
    			if((!"".equals(id) && !id.equals(JSONObject.getString("ID"))) ){
    				map.put(id, childrenList);
    				childrenList=new ArrayList();
    			}
    			childrenList.add(JSONObject);
    			if(i==list.size()-1){
    				map.put(id, childrenList);
    			}
    			id=JSONObject.getString("ID");
    			i++;
    		}
    		orgMaps=map;
//            String json_list = JSONObject.toJSONString(list);
//            orgMaps = (List<Map<String, Object>>) JSONObject.parse(json_list);
        }
        List<DeptBean> orgList = new ArrayList<>();
        //logger.debug(" 数据:"+orgMaps.size()+"   pid:"+pid);
        if (orgMaps != null && orgMaps.size() > 0) {
        	List parenList=orgMaps.get(pid);
        	if(parenList==null){
        		return orgList;
        	}
        	DeptBean deptBean=null;
            for (Object obj : parenList) {
            	deptBean=new DeptBean();
            	JSONObject json=(JSONObject)obj;
            	deptBean.setId(json.getString("CHILD_ID"));
            	deptBean.setDeptCode(json.getString("CHILD_CODE"));
            	deptBean.setDeptName(json.getString("CHILD_NAME"));
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

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public DeptBean queryById(String id) {
		String sql = "SELECT ID,PID,DEPTCODE,DEPTNAME,REMARK FROM SYS_DEPT WHERE id = ?";
		List<DeptBean> l = ur.queryAllCustom(sql, atm, id);
		return l.get(0);
	}

	/**
	 * 
	 * 编辑（查询）
	 * @param start
	 * @param limit
	 * @param pId
	 * @return
	 */
	public Page<DeptBean> queryByPage(int start, int limit, JSONObject paramJson) {
		StringBuffer sql = new StringBuffer("SELECT dept.*,fdept.DEPTCODE as pdeptCode,fdept.DEPTNAME as pdeptName FROM SYS_DEPT dept left join SYS_DEPT fdept on dept.pid=fdept.DEPTCODE where 1=1 ");
		LinkedMap map = new LinkedMap();
        Integer index = 1;
        if(paramJson!=null && !paramJson.isEmpty()){
        	if(paramJson.containsKey("pid")   && !st.stringNotNull(paramJson.getString("pid")) ){
        		sql.append(" and dept.pid = ? ");
        		map.put(index,paramJson.get("pid") );
        		index++;
        	}
        	if(paramJson.containsKey("deptcode")   && !st.stringNotNull(paramJson.getString("deptcode")) ){
        		sql.append(" and dept.deptcode like ? ");
        		map.put(index,"%" + paramJson.get("deptcode") + "%");
        		index++;
        	}
        	if(paramJson.containsKey("deptname")   && !st.stringNotNull(paramJson.getString("deptname")) ){
        		sql.append(" and dept.deptname like ? ");
        		map.put(index,"%" + paramJson.get("deptname") + "%");
        		index++;
        	}
        }
		Page<JSONObject> rs = ur.queryByCustomPage(sql.toString(), start, limit, map);
		if(rs!=null){
			 Page<DeptBean> pageDeptBeanInfo=new Page<DeptBean>();
			 ArrayList<DeptBean> students = JSON.parseObject(rs.getContent().toString(), new TypeReference<ArrayList<DeptBean>>() {});
			 pageDeptBeanInfo.setContent(students);
			 pageDeptBeanInfo.setTotalElements(rs.getTotalElements());
			 return pageDeptBeanInfo;
		}
		return null;
	}
	

	/**
	 * 
	 * 编辑（保存）
	 * @param 
	 * @return
	 */
	public ResponseCodeBySummit edit(DeptBean ab) {
		String sql = "UPDATE SYS_DEPT SET  pid = ?, DEPTCODE = ?, DEPTNAME = ?, REMARK = ? where id = ?";
		jdbcTemplate.update(
				sql,
				ab.getPid(),
				ab.getDeptCode(),
				ab.getDeptName(),
				ab.getRemark(),
				ab.getId()
		);
		return ResponseCodeBySummit.CODE_0000;
	}
	/**
	 * 新增
	 */
	public ResponseCodeBySummit add(DeptBean ab) {
		String hasadcd="select * from SYS_DEPT where DEPTCODE='"+ab.getDeptCode()+"'";
		List l=ur.queryAllCustom(hasadcd);
		if(l.size()>0){
			return ResponseCodeBySummit.CODE_4033;
		}
		String sql = "INSERT INTO SYS_DEPT (ID, PID, DEPTCODE,DEPTNAME,REMARK) VALUES (?, ? ,?, ?,?)";
		jdbcTemplate.update(
				sql,
				st.getKey(),
				ab.getPid(),
				ab.getDeptCode(),
				ab.getDeptName(),
				ab.getRemark());
		return ResponseCodeBySummit.CODE_0000;
	}


	/**
	 * 
	 * 
	 * 删除
	 * @param ids
	 * @return
	 */
	public ResponseCodeBySummit del(String ids) {
		ids = ids.replaceAll(",", "','");
		String sql = "SELECT * FROM SYS_DEPT WHERE pid IN ('" + ids + "')";
		List<DeptBean> l = ur.queryAllCustom(sql, atm);
		if (st.collectionNotNull(l)) {
			return ResponseCodeBySummit.CODE_9981;
		}
		sql = "DELETE FROM SYS_DEPT WHERE id IN ('" + ids+ "') ";
		jdbcTemplate.update(sql);
		return ResponseCodeBySummit.CODE_0000;
	}




}
