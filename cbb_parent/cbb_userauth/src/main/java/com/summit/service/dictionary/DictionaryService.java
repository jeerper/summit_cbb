package com.summit.service.dictionary;

import com.alibaba.fastjson.JSONArray;
import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.domain.dictionary.DictionaryBean;
import com.summit.domain.dictionary.DictionaryBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.service.cache.DictionaryCacheImpl;
import com.summit.util.Page;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional
@Service
public class DictionaryService {
	@Autowired
	private UserRepository ur;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private SummitTools st;
	@Autowired
	private DictionaryBeanRowMapper dbrm;
	@Autowired
	private DictionaryCacheImpl dictionaryCacheImpl;
	
	//private static Logger logger = LoggerFactory.getLogger(DictionaryService.class);

	public ResponseCodeBySummit add(DictionaryBean db) {
		String sql = "SELECT * FROM SYS_DICTIONARY WHERE CODE = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, db.getCode());
		if (st.collectionNotNull(l)) {
			return ResponseCodeBySummit.CODE_4033;
			//return st.error("编码" + db.getCode() + "已存在！");
		}
		sql = "INSERT INTO SYS_DICTIONARY (CODE, PCODE, NAME, CKEY, NOTE) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, db.getCode(), db.getPcode(), db.getName(),
				db.getCkey(), db.getNote());
//		SysDicMap.add(db);
		//新增字典对象加入缓存
		dictionaryCacheImpl.addDic(db);
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit del(String codes) {
		String codeArr[] = codes.split(",");
		codes = codes.replaceAll(",", "','");
		String sql = "SELECT * FROM SYS_DICTIONARY WHERE PCODE IN ('" + codes
				+ "')";
		List<DictionaryBean> l = ur.queryAllCustom(sql, dbrm);
		if (st.collectionNotNull(l)) {
			return ResponseCodeBySummit.CODE_9981;
		}
		sql = "DELETE FROM SYS_DICTIONARY WHERE CODE IN ('" + codes + "')";
		jdbcTemplate.update(sql);
		for (String code : codeArr) {
//			SysDicMap.reomve(code);
			dictionaryCacheImpl.delDic(code);
		}
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit edit(DictionaryBean db) {
		String sql = "UPDATE SYS_DICTIONARY SET NAME = ?, CKEY = ?, NOTE =? WHERE CODE = ?";
		jdbcTemplate.update(sql, db.getName(), db.getCkey(), db.getNote(),
				db.getCode());
//		SysDicMap.update(db);
		dictionaryCacheImpl.editDic(db);
		return ResponseCodeBySummit.CODE_0000;
	}

	public DictionaryBean queryByCode(String code) {
//		DictionaryBean db = SysDicMap.getByCode(code);
		return  dictionaryCacheImpl.queryByCode(code);
	}
	
	/**
	 * 
	 * 查询树
	 * @return
	 */
	public JSONObject queryDeptTree(String pid) {
		JSONObject jSONOTree=null;
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT ID,PID,DEPTCODE,DEPTNAME,REMARK FROM SYS_DEPT  where 1=1");
		if(pid==null || "".equals(pid)){
			sql.append(" and (pid is null  or pid='-1' )");
		}else{
			sql.append(" and PID =? ");
			linkedMap.put(1, pid);
			
		}
		List<Object> rootList=null;
		try {
			rootList=ur.queryAllCustom(sql.toString(),linkedMap);
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		if(rootList.size()>0){
			jSONOTree=(JSONObject)rootList.get(0);
			//logger.debug("jSONOTree.getString: "+jSONOTree.getString("ID"));
			List<JSONObject> list=null;
			list=generateOrgMapToTree(null,jSONOTree.getString("ID"));
			//logger.debug("list: "+list.size());
        	jSONOTree.put("children", list);
		}
		//logger.debug("jSONOTree0: "+jSONOTree);
		return jSONOTree;
	}
	

	public List<DictionaryBean> queryAll() {
		return ur.queryAllCustom("SELECT * FROM SYS_DICTIONARY order by code ,pcode desc", dbrm);
	}
	
	
	public JSONObject queryTree(String padcd) {
		JSONObject jSONOTree=null;
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT CODE,NAME,PCODE FROM SYS_DICTIONARY where 1=1 ");
		if(padcd==null || "".equals(padcd)){
			sql.append(" and (PCODE is null  or PCODE='-1' )");
		}else{
			sql.append(" and PCODE =? ");
			linkedMap.put(1, padcd);
			
		}
		List<Object> rootList= null;
        try {
			rootList= ur.queryAllCustom(sql.toString(),linkedMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        if(rootList!=null && rootList.size()>0){
			jSONOTree=(JSONObject)rootList.get(0);
			List<JSONObject> list=generateOrgMapToTree(null,jSONOTree.getString("CODE"));
        	jSONOTree.put("children", list);
		}
		return jSONOTree;
	}
	
   public List<JSONObject> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid)  {
        if (null == orgMaps || orgMaps.size() == 0) {
        	StringBuffer sql=new StringBuffer("SELECT A.CODE, A.NAME,A.PCODE, B.CODE AS CHILD_ID, B.NAME AS CHILD_NAME,b.CKEY  FROM SYS_DICTIONARY  AS A ");  
            sql.append(" JOIN SYS_DICTIONARY AS B ON B.PCODE = A.CODE ORDER BY  A.CODE ASC,b.CKEY asc ");
        	
        	JSONArray list=null;
			try {
				list = ur.queryAllCustomJsonArray(sql.toString(),null);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		Map<String, List<Object>> map=new HashMap<String, List<Object>>();
    		List<Object> childrenList=new ArrayList();;
    		String adcd="";
    		int i=0;
    		for(Object o:list){
    			JSONObject jSONObject=(JSONObject)o;
    			if(!"".equals(adcd) && !adcd.equals(jSONObject.getString("CODE")) ){
    				map.put(adcd, childrenList);
    				childrenList=new ArrayList();
    			}
    			childrenList.add(jSONObject);
    			if(i==list.size()-1){
    				map.put(adcd, childrenList);
    			}
    			i++;
    			adcd=jSONObject.getString("CODE");
    		}
    		orgMaps=map;
        }
        List<JSONObject> orgList = new ArrayList<>();
        if (orgMaps != null && orgMaps.size() > 0) {
        	List parenList=orgMaps.get(pid);
        	if(parenList==null){
        		return orgList;
        	}
        	
            for (Object obj : parenList) {
            	JSONObject jSONOTree=new JSONObject();
            	JSONObject json=(JSONObject)obj;
            	System.out.println(json);
            	jSONOTree.put("CODE", json.getString("CHILD_ID"));
            	jSONOTree.put("NAME", json.getString("CHILD_NAME"));
            	jSONOTree.put("CKEY", json.getString("CKEY"));
            	jSONOTree.put("PCODE",pid);
                List<JSONObject> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString());
                //将子结果集存入当前对象的children字段中
                jSONOTree.put("children", children);
                //添加当前对象到主结果集中
                orgList.add(jSONOTree);
            }
        }
        return orgList;
    }
	public JSONArray queryAllTree() {
		StringBuffer sql=new StringBuffer("SELECT a.code, a.name,a.PCODE, b.code AS CHILD_ID, b.name AS CHILD_NAME FROM sys_dictionary  AS a ");  
        sql.append(" JOIN sys_dictionary AS b ON b.pcode = a.code ORDER BY  a.code ASC,b.code asc");
        JSONArray list=null;
		try {
			list = ur.queryAllCustomJsonArray(sql.toString(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return list;
	}
	


	public Page<DictionaryBean> queryByPage(int start, int limit, String pId) {
//		List<DictionaryBean> list = SysDicMap.getChildList(pId);
		List<DictionaryBean> list = dictionaryCacheImpl.findChildList(pId);
		List<DictionaryBean> l = new ArrayList<DictionaryBean>();
		for (int i = start, j = 0; i < list.size(); i++, j++) {
			if (j >= limit) {
				break;
			}
			l.add(list.get(i));
		}
		return new Page<DictionaryBean>(l, list.size());
	}
	
	public List<DictionaryBean> queryByPid(String pId){
		List list=dictionaryCacheImpl.findChildList(pId);
		return list;
	}
	
	/**
	 * 初始化字典缓存加载
	 */
	public void initSysDic(){
		List<DictionaryBean> all = queryAll();
		for (DictionaryBean dictionaryBean : all) {
			dictionaryCacheImpl.setCacheElement(SysConstants.DICTIONARY, dictionaryBean.getCode(), dictionaryBean);
		}
		dictionaryCacheImpl.setCacheElement(SysConstants.DICTIONARY, "dictionaryAll", all);
		//logger.info("Dictionary Initialized...");
	}
	
	
	
}
