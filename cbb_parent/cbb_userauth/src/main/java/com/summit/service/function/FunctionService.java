package com.summit.service.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.FunctionTreeBean;
import com.summit.domain.function.FunctionBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.summit.cbb.utils.page.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FunctionService {
	private static final Logger logger = LoggerFactory.getLogger(FunctionService.class);
	@Autowired
	private UserRepository ur;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private FunctionBeanRowMapper fbrm;
	
	/**
	 * 
	 * 查询菜单树
	 * @return
	 */
	public FunctionBean queryFunTree(String pid) throws Exception{
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT * from  sys_function fun where 1=1 ");
		if(pid==null || "".equals(pid)){
			sql.append(" and (pid is null  or pid='-1' )");
		}else{
			sql.append(" and id =? ");
			linkedMap.put(1, pid);
		}
		List<Object> rootList= ur.queryAllCustom(sql.toString(),linkedMap);
        
        if(rootList.size()>0){
        	String jsonTree=((JSONObject)rootList.get(0)).toString();
			FunctionBean functionTree = JSON.parseObject(jsonTree, new TypeReference<FunctionBean>() {});
			//logger.debug(" jSONOTree.getString: "+jSONOTree.getString("ADCD"));
			List<FunctionBean> list=generateOrgMapToTree(null,functionTree.getId());
			logger.debug("list: "+list.size());
			functionTree.setChildren(list);
			return functionTree;
		}
		return null;
	}
	
	
   public List<FunctionBean> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid)throws  Exception {
        if (null == orgMaps || orgMaps.size() == 0) {//a.ADLEVEL as LEVELa ,b.ADLEVEL as LEVELb
        	StringBuffer querySql = new StringBuffer(" SELECT A.ID, A.NAME,A.PID, B.ID AS CHILD_ID, B.NAME AS CHILD_NAME,B.FDESC,B.FURL,B.IMGULR,B.NOTE, B.SUPER_FUN FROM SYS_FUNCTION AS A ");
        	querySql.append("  JOIN SYS_FUNCTION AS B ON B.PID = A.ID ");
        	// querySql.append("  where a.id!='root' ");
        	querySql.append("  where 1=1 ");
        	querySql.append("   ORDER BY a.id,fdesc ");
        	com.alibaba.fastjson.JSONArray list= ur.queryAllCustomJsonArray(querySql.toString(),null);
    		Map<String, List<Object>> map=new HashMap<String, List<Object>>();
    		List<Object> childrenList=new ArrayList<Object>();;
    		String adcd="";
    		int i=0;
    		for(Object o:list){
    			JSONObject jSONObject=(JSONObject)o;
    			if(!"".equals(adcd) && !adcd.equals(jSONObject.getString("ID"))){
    				map.put(adcd, childrenList);
    				childrenList=new ArrayList<Object>();
    			}
    			childrenList.add(jSONObject);
    			adcd=jSONObject.getString("ID");
    			if(i==list.size()-1){
    				map.put(adcd, childrenList);
    			}
    			i++;
    			
    		}
    		orgMaps=map;
//            String json_list = JSONObject.toJSONString(list);
//            orgMaps = (List<Map<String, Object>>) JSONObject.parse(json_list);
        }
       
        List<FunctionBean> orgList = new ArrayList<>();
        if (orgMaps != null && orgMaps.size() > 0) {
        	List<Object> parenList=orgMaps.get(pid);
        	if(parenList==null){
        		return orgList;
        	}
        	FunctionBean functionBean=null;
            for (Object obj : parenList) {
            	JSONObject json=(JSONObject)obj;
            	functionBean=new FunctionBean();
            	functionBean.setId(json.getString("CHILD_ID"));
            	functionBean.setName(json.getString("CHILD_NAME"));
            	if(json.containsKey("FDESC")){
            		functionBean.setFdesc(json.getInt("FDESC"));	
            	}
            	functionBean.setFurl(json.containsKey("FURL")?json.getString("FURL"):"");
            	functionBean.setImgUlr(json.containsKey("IMGULR")?json.getString("IMGULR"):"");
            	functionBean.setNote(json.containsKey("SUPER_FUN")?json.getString("SUPER_FUN"):"");
                List<FunctionBean> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString());
                functionBean.setChildren(children);
                //添加当前对象到主结果集中
                orgList.add(functionBean);
            }
        }
        return orgList;
    }

   
   public FunctionTreeBean queryJsonFunctionTree(String pid) throws Exception {
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT ID as `key`,NAME as title,pid from  sys_function fun where 1=1 ");
		if(pid==null || "".equals(pid)){
			sql.append(" and (pid is null  or pid='-1' )");
		}else{
			sql.append(" and id =? ");
			linkedMap.put(1, pid);
		}
	  List<Object> rootList= ur.queryAllCustom(sql.toString(),linkedMap);
	  FunctionTreeBean functionTreeBean =null;
     if(rootList.size()>0){
     	String jsonTree=((JSONObject)rootList.get(0)).toString();
     	functionTreeBean = JSON.parseObject(jsonTree, new TypeReference<FunctionTreeBean>() {});
     
			List<FunctionBean> list=generateOrgMapToTree(null,functionTreeBean.getKey());
			if(list!=null && list.size()>0){
				functionTreeBean.setChildren(getFunctionTreeBean(list));
			}
		}
		return functionTreeBean;
	}
   
   private List<FunctionTreeBean> getFunctionTreeBean(List<FunctionBean> children){
   	if(children!=null && children.size()>0){
   		List<FunctionTreeBean> functionTreeBeanList=new ArrayList<FunctionTreeBean>();
   		FunctionTreeBean functionTreeBean1=null;
   		for (FunctionBean functionBean : children) {
   			functionTreeBean1=new FunctionTreeBean();
   			functionTreeBean1.setValue(functionBean.getId());
   			functionTreeBean1.setKey(functionBean.getId());
   			functionTreeBean1.setTitle(functionBean.getName());
			List<FunctionTreeBean> adcdChildren =getFunctionTreeBean(functionBean.getChildren());
			if(adcdChildren!=null && adcdChildren.size()>0){
				functionTreeBean1.setChildren(adcdChildren);
            }
			functionTreeBeanList.add(functionTreeBean1);
   		}
   		return functionTreeBeanList;
   	}
   	return null;
   }
   
   


	public void add(FunctionBean fb) {
		String sql = "INSERT INTO SYS_FUNCTION (ID, PID, NAME, FDESC, IS_ENABLED, FURL, IMGULR, NOTE, SUPER_FUN) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
		jdbcTemplate.update(sql, SummitTools.getKey(), fb.getPid(), fb.getName(), fb
				.getFdesc(), fb.getIsEnabled(), fb.getFurl(), fb.getImgUlr(),
				fb.getNote(), 0);
	}

	public void del(String ids) {
		ids = ids.replaceAll(",", "','");
		//String sql = "SELECT * FROM SYS_FUNCTION WHERE PID IN ('" + ids + "')";
		//List<FunctionBean> l = ur.queryAllCustom(sql, fbrm);
		String sql = "DELETE FROM SYS_FUNCTION WHERE ID IN ('" + ids+ "') AND SUPER_FUN <> 1";
		jdbcTemplate.update(sql);

		sql = "DELETE FROM SYS_ROLE_FUNCTION WHERE FUNCTION_ID IN ('" + ids + "')";
		jdbcTemplate.update(sql);
	}

	public void edit(FunctionBean fb) {
		String sql = "UPDATE SYS_FUNCTION SET NAME = ?, FDESC = ?, IS_ENABLED = ?, FURL = ?, IMGULR = ?, NOTE = ? WHERE ID = ?";
		jdbcTemplate.update(sql, fb.getName(), fb.getFdesc(), fb
				.getIsEnabled(), fb.getFurl(), fb.getImgUlr(), fb.getNote(), fb
				.getId());
	}

	private boolean isSuperUser(String userName) {
		if (SummitTools.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
			return true;
		}
		return false;
	}

	// 后续根据用户名查询用户ID还是沿用用户名授权，再斟酌
	public List<FunctionBean> queryById(String id,String userName) {
		String sql;
		if (isSuperUser(userName)) {
			sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ?";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ? AND SUPER_FUN = 0 order by fdesc ";
		}
		List<FunctionBean> l = ur.queryAllCustom(sql, fbrm, id);
		return l;
	}

	public List<FunctionBean> queryAll(String userName) {
		String sql;
		if (isSuperUser(userName)) {
			sql = "SELECT * FROM SYS_FUNCTION ORDER BY FDESC";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE SUPER_FUN = 0 ORDER BY FDESC";
		}
		return ur.queryAllCustom(sql, fbrm);
	}

	public Page<FunctionBean> queryByPage(int start, int limit, String pId,String userName) throws Exception {
		StringBuffer sql=new StringBuffer("SELECT * FROM SYS_FUNCTION where 1=1 ");
		 LinkedMap linkedMap=null;
          if(!"root".equals(pId)){ 
			if (isSuperUser(userName)) {
				sql.append(" and PID = ? ");
			}else{
			    sql.append(" and  PID = ? AND SUPER_FUN = 0 ");
			}
			linkedMap=new LinkedMap();
		    linkedMap.put(1,pId);
		  }
		sql.append(" ORDER BY FDESC");
		Page<Object> rs = ur.queryByCustomPage(sql.toString(), start, limit, linkedMap);
		if(rs!=null){
			 ArrayList<FunctionBean> functions = JSON.parseObject(rs.getContent().toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			// return new PageImpl(functions,rs.getPageable(),rs.getTotalElements());
		}
		return null;
	}
	
	public List<FunctionBean> getFunInfoByUserName(String userName){
		String sql = "SELECT  SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1'  AND SUR.USERNAME = ? and SF.id!='root' order by	pid desc,FDESC";
		List<JSONObject>list= ur.queryAllCustom(sql, userName);
		if(list!=null){
			 ArrayList<FunctionBean> functionBeans = JSON.parseObject(list.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			 ArrayList<FunctionBean> functionBeancChildren =null;
			 Map<String,FunctionBean> map=new LinkedHashMap<String,FunctionBean>();
			 if(functionBeans!=null && functionBeans.size()>0){
				 String pid="";
	        	 for(FunctionBean functionBean:functionBeans){
	        		 if("root".equals(functionBean.getPid())){
	        			 map.put(functionBean.getId(), functionBean);
	        		 }else{
	        			 if("".equals(pid) || !pid.equals(functionBean.getPid())){
	        				 functionBeancChildren=new ArrayList<FunctionBean>();
	        			 }
	        			 functionBeancChildren.add(functionBean);
	        			 FunctionBean functionBean1=map.get(functionBean.getPid());
	        			 functionBean1.setChildren(functionBeancChildren);
	        			 map.put(functionBean.getPid(), functionBean1);
	        			 pid=functionBean.getPid();
	        		 }
	        	 }
	         }
			 Collection<FunctionBean> valueCollection = map.values();
			 List<FunctionBean> valueList = new ArrayList<FunctionBean>(valueCollection);
//			 for(FunctionBean functionBean:valueList){
//				 System.out.println("====:"+functionBean.getId()+","+functionBean.getName()+","+functionBean.getPid());	
//			 }
			// return valueList;
		}
		return null;
	}
	

}
