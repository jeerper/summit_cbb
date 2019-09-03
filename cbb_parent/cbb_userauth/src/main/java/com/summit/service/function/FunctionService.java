package com.summit.service.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.FunctionTreeBean;
import com.summit.repository.UserRepository;
import com.summit.util.ListUtils;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import net.sf.json.JSONObject;

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
		List<Object> rootList= ur.queryAllCustom(sql.toString(),linkedMap);
        
        if(rootList.size()>0){
        	String jsonTree=((JSONObject)rootList.get(0)).toString();
			FunctionBean functionTree = JSON.parseObject(jsonTree, new TypeReference<FunctionBean>() {});
			//logger.debug(" jSONOTree.getString: "+jSONOTree.getString("ADCD"));
			List<FunctionBean> list=generateOrgMapToTree(null,functionTree.getId());
			// logger.debug("list: "+list.size());
			functionTree.setChildren(list);
			return functionTree;
		}
		return null;
	}
	
   public List<FunctionBean> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid)throws  Exception {
        if (null == orgMaps || orgMaps.size() == 0) {//a.ADLEVEL as LEVELa ,b.ADLEVEL as LEVELb
        	StringBuffer querySql = new StringBuffer(" SELECT A.ID, A.NAME,B.PID,B.IS_ENABLED, B.ID AS CHILD_ID, B.NAME AS CHILD_NAME,B.FDESC,B.FURL,B.IMGULR,B.NOTE, B.SUPER_FUN FROM SYS_FUNCTION AS A ");
        	querySql.append("  JOIN SYS_FUNCTION AS B ON B.PID = A.ID ");
        	// querySql.append("  where a.id!='root' ");
        	querySql.append("  where A.IS_ENABLED = '1' ");
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
            	if(json.containsKey("PID")){
            		functionBean.setPid(json.getString("PID"));	
            	}
            	functionBean.setFurl(json.containsKey("FURL")?json.getString("FURL"):"");
            	functionBean.setIsEnabled(json.containsKey("IS_ENABLED")?json.getInt("IS_ENABLED"):null);
            	functionBean.setImgUlr(json.containsKey("IMGULR")?json.getString("IMGULR"):"");
            	functionBean.setNote(json.containsKey("NOTE")?json.getString("NOTE"):"");
            	functionBean.setSuperfun(json.containsKey("SUPER_FUN")?json.getString("SUPER_FUN"):"");
                List<FunctionBean> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString());
                functionBean.setChildren(children);
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
			 // return new PageImpl(functions,rs.getPageable(),rs.getTotalElements());
		}
		return null;
	}
	
	
	
	
	public List<FunctionBean> getFunInfoByUserName1(String userName,boolean isSuroleCode) throws Exception{
		String rootSql = "SELECT  * FROM  SYS_FUNCTION  where IS_ENABLED = '1' order by	FDESC";
		List<Object> functionList= ur.queryAllCustom(rootSql, new LinkedMap());
		Map<String,FunctionBean> mapFunctionBean=new HashMap<String,FunctionBean>();
		Map<String,FunctionBean> charildFunctionBean=new HashMap<String,FunctionBean>();
		if(functionList!=null && functionList.size()>0){
			ArrayList<FunctionBean> functions = JSON.parseObject(functionList.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			for(FunctionBean functionBean:functions){
				if("root".equals(functionBean.getPid())){
				   mapFunctionBean.put(functionBean.getId(), functionBean);
				}else{
					charildFunctionBean.put(functionBean.getId(), functionBean);
				}
			}
		}
		String[] sortnameArr=new String[]{"fdesc"};
        boolean[] typeArr=new boolean[]{true};
        String sql = null;
        LinkedMap linkedMap=new LinkedMap();
		Integer index = 1;
        if(isSuroleCode){
        	sql = "SELECT  DISTINCT * FROM SYS_FUNCTION WHERE IS_ENABLED = '1'  and id!='root'  order by  pid DESC";
        }else{
        	sql = "SELECT  DISTINCT SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1'  AND SUR.USERNAME = ? and SF.id!='root'  order by  pid DESC";
        	linkedMap.put(index, userName);
        }
		List list= ur.queryAllCustom(sql, linkedMap);
		if(list!=null){
			 ArrayList<FunctionBean> functionBeans = JSON.parseObject(list.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			 ArrayList<FunctionBean> functionBeancChildren =null;
			 Map<String,FunctionBean> map=new LinkedHashMap<String,FunctionBean>();
			 Map<String,FunctionBean> childrenmap=new LinkedHashMap<String,FunctionBean>();
			 if(functionBeans!=null && functionBeans.size()>0){
				 String pid="";

				 for(FunctionBean functionBean:functionBeans){
					// if("1e4e85ee2f3c4e9c8887a22a5cbfda30".equals(functionBean.getId())){
					//	 System.out.println("==========");
					// }
					 if("root".equals(functionBean.getPid())){
						 map.put(functionBean.getId(), functionBean); 
					 }else{
		        		 if("".equals(pid) || !pid.equals(functionBean.getPid())){
		        			 functionBeancChildren=new ArrayList<FunctionBean>();
		        		 }
		        		 functionBeancChildren.add(functionBean);
		        		 FunctionBean functionBean1=mapFunctionBean.get(functionBean.getPid());
		        		 if(functionBean1!=null){
		        			 ListUtils.sort(functionBeancChildren, sortnameArr, typeArr);
			        		 functionBean1.setChildren(functionBeancChildren);
			        		 if(childrenmap!=null && childrenmap.get(functionBean.getId())!=null){
			        			 List childrenList=childrenmap.get(functionBean.getId()).getChildren();
			        			
			        	        ListUtils.sort(childrenList, sortnameArr, typeArr);
			        			 
			        		    functionBean.setChildren(childrenList);
			        		 }
			        		
			        		 map.put(functionBean.getPid(), functionBean1);
		        		 }else{
		        			 if(functionBean!=null ){
		        				 functionBean1=charildFunctionBean.get(functionBean.getPid());
		        				 if(functionBean1!=null){
			        			   functionBean1.setChildren(functionBeancChildren);
		        				   childrenmap.put(functionBean.getPid(), functionBean1);	
		        				 }
		        			 }
		        			// map.put(functionBean.getId(), functionBean);
		        		 }
		        		 pid=functionBean.getPid();
					 }
	        			 
	        		}
	         }
			
			 Collection<FunctionBean> valueCollection = map.values();
			 List<FunctionBean> valueList = new ArrayList<FunctionBean>(valueCollection);
//			 for(FunctionBean functionBean:valueList){
//				 System.out.println("====:"+functionBean.getId()+","+functionBean.getName()+","+functionBean.getPid());	
//			 }
			 ListUtils.sort(valueList, sortnameArr, typeArr);
			 return valueList;
		}
		return null;
	}
	
	

	public List<FunctionBean> getFunInfoByUserName(String userName,boolean isSuroleCode) throws Exception{
		String rootSql = "SELECT  * FROM  SYS_FUNCTION WHERE IS_ENABLED = '1'  and id!='root'   order by  pid DESC ";
		List<Object> functionList= ur.queryAllCustom(rootSql, new LinkedMap());
		Map<String,FunctionBean> mapFunctionBean=new HashMap<String,FunctionBean>();
		if(functionList!=null && functionList.size()>0){
			ArrayList<FunctionBean> functions = JSON.parseObject(functionList.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			String pid;
			for(FunctionBean functionBean:functions){
				mapFunctionBean.put(functionBean.getId(), functionBean);
			}
		}
		String sql = null;
	    LinkedMap linkedMap=new LinkedMap();
	    Integer index = 1;
		if(isSuroleCode){
        	sql = "SELECT  DISTINCT * FROM SYS_FUNCTION WHERE IS_ENABLED = '1'  and id!='root'   order by fdesc ";
        }else{
        	sql = "SELECT  DISTINCT SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1'  AND SUR.USERNAME = ? and SF.id!='root'    order by fdesc ";
        	linkedMap.put(index, userName);
        }
		
		List<FunctionBean> menuList = new ArrayList<FunctionBean>();
		List list= ur.queryAllCustom(sql, linkedMap);
		if(list!=null){
			 Map<String,FunctionBean> map=new LinkedHashMap<String,FunctionBean>();
			 ArrayList<FunctionBean> functionBeans = JSON.parseObject(list.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			 for(FunctionBean functionBean:functionBeans){
//				 System.out.println(functionBean.getName()+"==================================================");
//				 if("430e1876e61045789a1e3d122ff4c962".equals(functionBean.getId())){
//					 System.out.println("========");
//				 }
				 if("root".equals(functionBean.getPid())){
					 if(map.get(functionBean.getId())==null){
						 map.put(functionBean.getId(), functionBean);
						 menuList.add(functionBean);	 
					 }
					
				 }else {
					 FunctionBean rootFunctionBean=mapFunctionBean.get(functionBean.getPid());
					 if(rootFunctionBean!=null && "root".equals(rootFunctionBean.getPid())){
						 if(rootFunctionBean.getChildren()!=null){
							 boolean falg=false;
							 for(FunctionBean functionBean1:rootFunctionBean.getChildren()){
								 if(functionBean1.getId().equals(functionBean.getId())){
									 falg=true;
								 }
							 }
							 if(!falg){
								 rootFunctionBean.getChildren().add(functionBean);
							 }
							
						 }else{
							List<FunctionBean> functionBeancChildren=new ArrayList<FunctionBean>(); 
							functionBeancChildren.add(functionBean);
							rootFunctionBean.setChildren(functionBeancChildren);
						 }
						 map.put(rootFunctionBean.getId(), rootFunctionBean);
					 }else{
						 FunctionBean rootFunctionBean1=getRoot(functionBean.getPid(),mapFunctionBean,functionBean, map,null);
						// System.out.println("=============="+rootFunctionBean+"   ========== :"+functionBean.getName());
						 if(rootFunctionBean1!=null){
						    map.put(rootFunctionBean1.getId(), rootFunctionBean1);
						    // menuList.add(functionBean);
						 }
					 }
				 }
				
//				 Collection<FunctionBean> valueCollection = map.values();
//				 List<FunctionBean> valueList = new ArrayList<FunctionBean>(valueCollection);
//				 for(FunctionBean functionBean11:valueList){
//					 System.out.println("====:"+functionBean11.getId()+","+functionBean11.getName()+","+functionBean11.getChildren());	
//				 }
			 }
			 Collection<FunctionBean> valueCollection = map.values();
			 List<FunctionBean> valueList = new ArrayList<FunctionBean>(valueCollection);
//			 for(FunctionBean functionBean:valueList){
//				 System.out.println("====:"+functionBean.getId()+","+functionBean.getName()+","+functionBean.getChildren());	
//			 }
			 String[] sortnameArr=new String[]{"fdesc"};
		        boolean[] typeArr=new boolean[]{true};
			 ListUtils.sort(valueList, sortnameArr, typeArr);
			 return valueList;
		}
		
		return null;
		
	}
	
	private FunctionBean getRoot(String id,Map<String,FunctionBean> mapFunctionBean,FunctionBean functionBean, Map<String,FunctionBean> map,Map<String,FunctionBean> childMap ) {
		
		   FunctionBean functionBeanInfo=mapFunctionBean.get(id);
		   if(childMap==null){
			   childMap=new HashMap<String,FunctionBean>();
		   }
			if(functionBeanInfo!=null && map.get(functionBeanInfo.getId())!=null){
				functionBeanInfo=map.get(functionBeanInfo.getId());
			}
			// System.out.println("functionBean: "+functionBean.getName());
			if(functionBean!=null && functionBeanInfo!=null && functionBean.getPid().equals(functionBeanInfo.getId())){
				if(functionBeanInfo.getChildren()!=null ){
					boolean falg=functionBeanInfo.getChildren().contains(functionBean);
					if(!falg){
						functionBeanInfo.getChildren().add(functionBean);
					}else{
						for(FunctionBean functionBean1:functionBeanInfo.getChildren()){
							if(functionBean1.getId().equals(functionBean.getId())){
								//functionBean1.getChildren().add(functionBean);
								// System.out.println("name:  "+functionBean1.getName());
								if(functionBean1.getChildren()==null){
									List<FunctionBean> functionBeancChildren=new ArrayList<FunctionBean>(); 
									functionBeancChildren.add(childMap.get(functionBean1.getId()));
									functionBean1.setChildren(functionBeancChildren);
								}
//								else{
//									functionBean1.getChildren().add(childMap.get(functionBean1.getId()));
//								}
								
							}
						}
					}
				
					// functionBeanInfo.getChildren().add(functionBean);
				}else{
					List<FunctionBean> functionBeancChildren=new ArrayList<FunctionBean>(); 
					functionBeancChildren.add(functionBean);
					functionBeanInfo.setChildren(functionBeancChildren);
					childMap.put(functionBeanInfo.getPid(), functionBeanInfo);
				}
			}
			
			if("root".equals(functionBeanInfo.getPid())){
				return functionBeanInfo; 
		    }else{
				if(!"root".equals(functionBeanInfo.getPid())){
					functionBeanInfo=getRoot(functionBeanInfo.getPid(),mapFunctionBean,functionBeanInfo,map,childMap);
				}
			}
			
			return functionBeanInfo;
	}
	
	
	
	
	

}
