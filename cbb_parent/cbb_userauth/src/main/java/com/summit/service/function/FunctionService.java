package com.summit.service.function;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.domain.function.FunctionBean;
import com.summit.domain.function.FunctionBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.service.adcd.ADCDService;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import com.summit.util.TreeNode;
import net.sf.json.JSONArray;
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
	@Autowired
	private SummitTools st;
	@Autowired
	private FunctionBeanRowMapper fbrm;
	
	/**
	 * 
	 * 查询菜单树
	 * @return
	 */
	public JSONObject queryFunTree(String padcd) {
		JSONObject jSONOTree=null;
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT * from  sys_function fun where 1=1 ");
		if(padcd==null || "".equals(padcd)){
			sql.append(" and (pid is null  or pid='-1' )");
		}else{
			sql.append(" and pid =? ");
			linkedMap.put(1, padcd);
		}
		List<Object> rootList= null;
        try {
			rootList= ur.queryAllCustom(sql.toString(),linkedMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        if(rootList.size()>0){
			jSONOTree=(JSONObject)rootList.get(0);
			//logger.debug(" jSONOTree.getString: "+jSONOTree.getString("ADCD"));
			List<JSONObject> list=generateOrgMapToTree(null,jSONOTree.getString("ID"));
			logger.debug("list: "+list.size());
        	jSONOTree.put("CHILDREN", list);
		}
        logger.debug("0-2");
        logger.debug("jSONOTree0: "+jSONOTree);
		return jSONOTree;
	}
	
   public List<JSONObject> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid)  {
        if (null == orgMaps || orgMaps.size() == 0) {//a.ADLEVEL as LEVELa ,b.ADLEVEL as LEVELb
        	StringBuffer querySql = new StringBuffer(" SELECT A.ID, A.NAME,A.PID, B.ID AS CHILD_ID, B.NAME AS CHILD_NAME,B.FDESC,B.FURL,B.IMGULR,B.NOTE, B.SUPER_FUN FROM SYS_FUNCTION AS A ");
        	querySql.append("  JOIN SYS_FUNCTION AS B ON B.PID = A.ID ");
        	querySql.append("  where a.id!='root' ");
        	querySql.append("   ORDER BY  a.id asc  ");
        	com.alibaba.fastjson.JSONArray list=null;
			try {
				list = ur.queryAllCustomJsonArray(querySql.toString(),null);
			} catch (Exception e) {
				e.printStackTrace();
			}
        	logger.debug("1:"+list.size());
    		Map<String, List<Object>> map=new HashMap<String, List<Object>>();
    		List<Object> childrenList=new ArrayList();;
    		String adcd="";
    		int i=0;
    		for(Object o:list){
    			JSONObject jSONObject=(JSONObject)o;
    			if(!"".equals(adcd) && !adcd.equals(jSONObject.getString("ID"))){
    				map.put(adcd, childrenList);
    				childrenList=new ArrayList();
    			}
    			childrenList.add(jSONObject);
    			if(i==list.size()-1){
    				map.put(adcd, childrenList);
    			}
    			i++;
    			adcd=jSONObject.getString("ID");
    		}
    		orgMaps=map;
//            String json_list = JSONObject.toJSONString(list);
//            orgMaps = (List<Map<String, Object>>) JSONObject.parse(json_list);
        }
        logger.debug("2:"+orgMaps.size());
        List<JSONObject> orgList = new ArrayList<>();
        if (orgMaps != null && orgMaps.size() > 0) {
        	List parenList=orgMaps.get(pid);
        	if(parenList==null){
        		return orgList;
        	}
        	logger.debug("3:"+parenList.size());
        	int i=0;
            for (Object obj : parenList) {
            	logger.debug("3-1:"+i);
            	JSONObject jSONOTree=new JSONObject();
            	JSONObject json=(JSONObject)obj;
            	System.out.println(json);
            	jSONOTree.put("ID", json.getString("CHILD_ID"));
            	jSONOTree.put("NAME", json.getString("CHILD_NAME"));
            	jSONOTree.put("FDESC",json.getString("FDESC"));
            	jSONOTree.put("FURL",json.getString("FURL"));
            	jSONOTree.put("IMGULR",json.getString("IMGULR"));
            	jSONOTree.put("NOTE",json.getString("SUPER_FUN"));
            	
                List<JSONObject> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString());
                //将子结果集存入当前对象的children字段中
                jSONOTree.put("children", children);
                //添加当前对象到主结果集中
                orgList.add(jSONOTree);
                i++;
            }
            logger.debug("4:"+orgList.size());
        }
        logger.debug("5:"+orgList.size());
        return orgList;
    }



	public ResponseCodeBySummit add(FunctionBean fb) {
		String sql = "INSERT INTO SYS_FUNCTION (ID, PID, NAME, FDESC, IS_ENABLED, FURL, IMGULR, NOTE, SUPER_FUN) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
		jdbcTemplate.update(sql, st.getKey(), fb.getPid(), fb.getName(), fb
				.getFdesc(), fb.getIsEnabled(), fb.getFurl(), fb.getImgUlr(),
				fb.getNote(), 0);
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit del(String ids) {
		ids = ids.replaceAll(",", "','");
		String sql = "SELECT * FROM SYS_FUNCTION WHERE PID IN ('" + ids + "')";
		List<FunctionBean> l = ur.queryAllCustom(sql, fbrm);
		if (st.collectionNotNull(l)) {
			return ResponseCodeBySummit.CODE_9981;
		}
		sql = "DELETE FROM SYS_FUNCTION WHERE ID IN ('" + ids
				+ "') AND SUPER_FUN <> 1";
		jdbcTemplate.update(sql);

		sql = "DELETE FROM SYS_ROLE_FUNCTION WHERE FUNCTION_ID IN ('" + ids + "')";
		jdbcTemplate.update(sql);
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit edit(FunctionBean fb) {
		String sql = "UPDATE SYS_FUNCTION SET NAME = ?, FDESC = ?, IS_ENABLED = ?, FURL = ?, IMGULR = ?, NOTE = ? WHERE ID = ?";
		jdbcTemplate.update(sql, fb.getName(), fb.getFdesc(), fb
				.getIsEnabled(), fb.getFurl(), fb.getImgUlr(), fb.getNote(), fb
				.getId());
		return ResponseCodeBySummit.CODE_0000;
	}

	private boolean isSuperUser(String userName) {
		if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
			return true;
		}
		return false;
	}

	// 后续根据用户名查询用户ID还是沿用用户名授权，再斟酌
	public Map<String, Object> queryById(String id,String userName) {
		String sql;
		if (isSuperUser(userName)) {
			sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ?";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ? AND SUPER_FUN = 0";
		}
		List<FunctionBean> l = ur.queryAllCustom(sql, fbrm, id);
		if (st.collectionIsNull(l)) {
			return st.error("");
		}
		return st.success("", l.get(0));
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

	public Page<JSONObject> queryByPage(int start, int limit, String pId,String userName) {
		String sql;
		if (isSuperUser(userName)) {
			sql = "SELECT * FROM SYS_FUNCTION WHERE PID = ? ORDER BY FDESC";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE PID = ? AND SUPER_FUN = 0 ORDER BY FDESC";
		}
		return ur.queryByCustomPage(sql, start, limit, pId);
	}

//	public String getFunByUserName(String userName) {
//		List<TreeNode<JSONObject>> tn;
//		String sql;
//		if (isSuperUser(userName)) {
//			sql = "SELECT * FROM SYS_FUNCTION WHERE IS_ENABLED = '1' ORDER BY FDESC";
//		} else {
//			sql = "SELECT DISTINCT SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ? ORDER BY FDESC";
//		}
//		if (st.stringEquals(userName, SysConstants.SUPER_USERNAME)) {
//			tn = st.creatTreeNode(ur.queryAllCustom(sql, fbrm), null);
//		} else {
//			tn = st.creatTreeNode(ur.queryAllCustom(sql, fbrm, userName), null);
//		}
//		return JSONArray.fromObject(tn).toString();
//	}

	public Map<String, Collection<String>> getResourceMap() {
		Map<String, Collection<String>> resourceMap = new HashMap<String, Collection<String>>();
		String sql = "SELECT SRF.ROLE_CODE,SF.FURL FROM SYS_ROLE_FUNCTION SRF INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID)";
		List<JSONObject> l = ur.queryAllCustom(sql);
		String role, url;
		for (JSONObject o : l) {
			url = st.objJsonGetString(o, "FURL");
			if (st.stringIsNull(url)) {
				continue;
			}
			role = st.objJsonGetString(o, "ROLE_CODE");
			if (resourceMap.get(url) == null) {
				resourceMap.put(url, new ArrayList<String>());
				resourceMap.get(url).add(role);
			}
			resourceMap.get(url).add(role);
		}
		return resourceMap;
	}
}
