package com.summit.service.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.FunctionTreeBean;
import com.summit.repository.UserRepository;
import com.summit.util.ListUtils;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class FunctionService {
	private static final Logger logger = LoggerFactory.getLogger(FunctionService.class);
	@Autowired
	private UserRepository ur;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * 查询菜单树
	 * @return
	 */
	public FunctionBean queryFunTree(String pid) throws Exception{
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT * from  sys_function fun where IS_ENABLED = '1' ");
		if(pid==null || "".equals(pid)){
			sql.append(" and (pid is null  or pid='-1' )");
		}else{
			sql.append(" and id =? ");
			linkedMap.put(1, pid);
		}
		
    	StringBuffer querySql = new StringBuffer(" SELECT A.ID, A.NAME,B.PID,B.IS_ENABLED, B.ID AS CHILD_ID, B.NAME AS CHILD_NAME,B.FDESC,B.FURL,B.IMGULR,B.NOTE, B.SUPER_FUN FROM SYS_FUNCTION AS A ");
    	querySql.append("  JOIN SYS_FUNCTION AS B ON B.PID = A.ID ");
    	// querySql.append("  where a.id!='root' ");
    	querySql.append("  where B.IS_ENABLED = '1' ");
    	querySql.append("   ORDER BY a.id,fdesc ");
    	Map<String, List<Object>> orgMaps=getMap(querySql.toString());
		
		List<Object> rootList= ur.queryAllCustom(sql.toString(),linkedMap);
        if(rootList.size()>0){
        	String jsonTree=((JSONObject)rootList.get(0)).toString();
			FunctionBean functionTree = JSON.parseObject(jsonTree, new TypeReference<FunctionBean>() {});
			//logger.debug(" jSONOTree.getString: "+jSONOTree.getString("ADCD"));
			List<FunctionBean> list=generateOrgMapToTree(orgMaps,functionTree.getId());
			// logger.debug("list: "+list.size());
			functionTree.setChildren(list);
			return functionTree;
		}
		return null;
	}
	
	private Map<String, List<Object>> getMap(String querySql) throws Exception{
		Map<String, List<Object>>  orgMaps=null;
		if (null == orgMaps || orgMaps.size() == 0) {
        	com.alibaba.fastjson.JSONArray list= ur.queryAllCustomJsonArray(querySql,null);
    		Map<String, List<Object>> map=new HashMap<String, List<Object>>();
    		List<Object> childrenList=new ArrayList<Object>();
    		String adcd="";
    		int i=0;
    		for(Object o:list){
	   			JSONObject jSONObject=(JSONObject)o;
	   			if(!"".equals(adcd) && !adcd.equals(jSONObject.getString("PID"))){
	   				map.put(adcd, childrenList);
	   				childrenList=new ArrayList<Object>();
	   			}
	   			if(jSONObject.containsKey("CID") && jSONObject.getString("CID")!=null && !"root".equals(jSONObject.getString("CID"))){
	   				childrenList.add(jSONObject);
	   				map.put(jSONObject.getString("CID"), childrenList);
   				}else{
	   			   childrenList.add(jSONObject);
   				}
	   			adcd=jSONObject.getString("PID");
	   			if(i==list.size()-1){
	   				if(jSONObject.containsKey("CID") && jSONObject.getString("CID")!=null && !"root".equals(jSONObject.getString("CID"))){
	   					map.put(jSONObject.getString("CID"), childrenList);
	   				}else{
	   					map.put(adcd, childrenList);
	   				}
	   				
	   			}
	   			i++;
	   			
	   		}
    		orgMaps=map;
        }
		return orgMaps;
	}
	
   public List<FunctionBean> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid)throws  Exception {
        
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
            	if(json.containsKey("PID")){
            		functionBean.setPid(json.getString("PID"));	
            	}
            	functionBean.setFurl(json.containsKey("FURL")?json.getString("FURL"):"");
            	functionBean.setIsEnabled(json.containsKey("IS_ENABLED")?json.getInt("IS_ENABLED"):null);
            	functionBean.setImgUlr(json.containsKey("IMGULR")?json.getString("IMGULR"):"");
            	functionBean.setNote(json.containsKey("NOTE")?json.getString("NOTE"):"");
            	functionBean.setSuperfun(json.containsKey("SUPER_FUN")?json.getString("SUPER_FUN"):"");
                List<FunctionBean> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString());
                if(children!=null && children.size()==0){
                	functionBean.setChildren(null);
                }else{
                   functionBean.setChildren(children);
                }
                //添加当前对象到主结果集中
                orgList.add(functionBean);
            }
        }
        return orgList;
    }
   
   public FunctionBean generateOrgMapTo(Map<String, FunctionBean>  orgMaps, String pid)throws  Exception {
      
       FunctionBean functionBean=null;
       if (orgMaps != null && orgMaps.size() > 0) {
    	FunctionBean paren=orgMaps.get(pid);
    	if(paren==null){
    		return null;
    	}
       	if("root".equals(paren.getPid())){
       		return paren;
       	}
       	if(paren!=null && !"root".equals(paren.getPid())){
       		functionBean=generateOrgMapTo(orgMaps,paren.getPid());
        } 
       }
       return functionBean;
   }
   
   public FunctionTreeBean queryJsonFunctionTree(String pid) throws Exception {
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT fun.ID as `key`,NAME as title,pid from  sys_function fun where IS_ENABLED = '1' ");
		if( pid==null || "".equals(pid)){
			sql.append(" and (fun.pid is null  or fun.pid='-1' )");
		}else{
			sql.append(" and fun.id =? ");
			linkedMap.put(1, pid);
		}
	  // System.out.println("sql:"+sql.toString()+"   pid:"+pid);
	  List<Object> rootList= ur.queryAllCustom(sql.toString(),linkedMap);
	  FunctionTreeBean functionTreeBean =null;
     if(rootList.size()>0){
     	String jsonTree=((JSONObject)rootList.get(0)).toString();
     	functionTreeBean = JSON.parseObject(jsonTree, new TypeReference<FunctionTreeBean>() {});
    	StringBuffer querySql = new StringBuffer(" SELECT A.ID, A.NAME,B.PID,B.IS_ENABLED, B.ID AS CHILD_ID, B.NAME AS CHILD_NAME,B.FDESC,B.FURL,B.IMGULR,B.NOTE, B.SUPER_FUN FROM SYS_FUNCTION AS A ");
    	querySql.append("  JOIN SYS_FUNCTION AS B ON B.PID = A.ID ");
    	// querySql.append("  where a.id!='root' ");
    	querySql.append("  where B.IS_ENABLED = '1' ");
    	querySql.append("   ORDER BY a.id,fdesc ");
    	Map<String, List<Object>> orgMaps=getMap(querySql.toString());
			List<FunctionBean> list=generateOrgMapToTree(orgMaps,functionTreeBean.getKey());
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
	public List<FunctionBean> queryById(String id,String userName) throws Exception {
		String sql;
		if (isSuperUser(userName)) {
			sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ? and IS_ENABLED = '1'";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ? AND SUPER_FUN = 0 and IS_ENABLED = '1' order by fdesc ";
		}
		LinkedMap linkedMap=new LinkedMap();
	    linkedMap.put(1,id);
		List<Object> dataList = ur.queryAllCustom(sql, linkedMap);
		if(dataList!=null &&  dataList.size()>0){
			ArrayList<FunctionBean> functionBeanList = JSON.parseObject(dataList.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			return functionBeanList;
		}
		return null;
	}

	public List<FunctionBean> queryAll(String userName)throws Exception {
		String sql;
		if (isSuperUser(userName)) {
			sql = "SELECT * FROM SYS_FUNCTION where IS_ENABLED = '1' ORDER BY FDESC";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE SUPER_FUN = 0  IS_ENABLED = '1' ORDER BY FDESC";
		}
		List<Object> dataList = ur.queryAllCustom(sql, new LinkedMap());
		if(dataList!=null &&  dataList.size()>0){
			ArrayList<FunctionBean> functionBeanList = JSON.parseObject(dataList.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			return functionBeanList;
		}
		return null;
	}

	public Page<FunctionBean> queryByPage(int start, int limit, String pId,String userName) throws Exception {
		StringBuffer sql=new StringBuffer("SELECT * FROM SYS_FUNCTION where 1=1 ");
		 LinkedMap linkedMap=null;
          if(SummitTools.stringNotNull(pId)){ 
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
			 return new Page<FunctionBean>(functions,rs.getPageable());
		}
		return null;
	}
	
	
	
	public List<FunctionBean> getFunInfoByUserName(String userName,boolean isSuroleCode) throws Exception{
		String rootSql = "SELECT  * FROM  SYS_FUNCTION WHERE IS_ENABLED = '1'   and pid='root'   order by  fdesc  ";
		List<Object> rootList= ur.queryAllCustom(rootSql, new LinkedMap());
		
		StringBuffer sql = new StringBuffer("select A.ID as PID,B.ID AS ID,B.IS_ENABLED,B.ID AS CHILD_ID,B.NAME AS CHILD_NAME,B.FDESC,B.FURL,B.IMGULR,B.NOTE, B.SUPER_FUN  ");
		LinkedMap linkedMap=new LinkedMap();
		if(isSuroleCode){
			sql.append(" FROM  (SELECT  DISTINCT * FROM SYS_FUNCTION WHERE IS_ENABLED = '1'  and id!='root'   order by fdesc)A  ");
			sql.append(" JOIN SYS_FUNCTION AS B ON B.PID = A.ID ORDER BY a.id,fdesc ");
		}else{
			sql.append(" ,c.id AS CID,c.name AS CNAME FROM (SELECT  DISTINCT SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1'  AND SUR.USERNAME = '"+userName+"' and SF.id!='root'    order by fdesc)B ");
        	// linkedMap.put(1, userName);
			sql.append(" JOIN SYS_FUNCTION AS A ON B.PID = A.ID  ");
			sql.append(" LEFT JOIN SYS_FUNCTION AS C ON a.pid=c.id ORDER BY a.id,fdesc ");
		}
		
		List menuList= ur.queryAllCustom(sql.toString(), linkedMap);
		Map<String,FunctionBean> mapFunctionBean=new HashMap<String,FunctionBean>();
		if(menuList!=null && menuList.size()>0){
			ArrayList<FunctionBean> functions = JSON.parseObject(menuList.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			for(FunctionBean functionBean:functions){
				mapFunctionBean.put(functionBean.getId(), functionBean);
			}
		}
		Map<String, List<Object>>  orgMaps=getMap(sql.toString());
		
		List<FunctionBean> funInfoList = new ArrayList<FunctionBean>();
		if(rootList!=null && rootList.size()>0){
			ArrayList<FunctionBean> functions = JSON.parseObject(rootList.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			for(FunctionBean functionBean:functions){
				List<FunctionBean> list=generateOrgMapToTree(orgMaps,functionBean.getId());
				if(list!=null && list.size()>0){
					 functionBean.setChildren(list);
					 funInfoList.add(functionBean);
				}else if(mapFunctionBean.get(functionBean.getId())!=null){
					funInfoList.add(functionBean);
				}
			}
			return funInfoList;
		}
		return null;
	}

	
	

}
