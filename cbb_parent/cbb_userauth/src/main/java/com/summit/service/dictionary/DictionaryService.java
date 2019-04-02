package com.summit.service.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.summit.common.entity.DictionaryBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.domain.dictionary.DictionaryBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.service.cache.DictionaryCacheImpl;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import net.sf.json.JSONObject;


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

	public ResponseCodeEnum add(DictionaryBean db) {
		String sql = "SELECT * FROM SYS_DICTIONARY WHERE CODE = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, db.getCode());
		if (st.collectionNotNull(l)) {
			return ResponseCodeEnum.CODE_9992;
			//return st.error("编码" + db.getCode() + "已存在！");
		}
		sql = "INSERT INTO SYS_DICTIONARY (CODE, PCODE, NAME, CKEY, NOTE) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, db.getCode(), db.getPcode(), db.getName(),
				db.getCkey(), db.getNote());
//		SysDicMap.add(db);
		//新增字典对象加入缓存
		dictionaryCacheImpl.addDic(db);
		return null;
	}

	public void del(String codes) {
		String codeArr[] = codes.split(",");
		codes = codes.replaceAll(",", "','");
//		String sql = "SELECT * FROM SYS_DICTIONARY WHERE PCODE IN ('" + codes
//				+ "')";
//		List<DictionaryBean> l = ur.queryAllCustom(sql, dbrm);
//		if (st.collectionNotNull(l)) {
//			return ResponseCodeBySummit.CODE_9981;
//		}
		String sql = "DELETE FROM SYS_DICTIONARY WHERE CODE IN ('" + codes + "')";
		jdbcTemplate.update(sql);
		for (String code : codeArr) {
//			SysDicMap.reomve(code);
			dictionaryCacheImpl.delDic(code);
		}
	}

	public void edit(DictionaryBean db) {
		String sql = "UPDATE SYS_DICTIONARY SET NAME = ?, CKEY = ?, NOTE =? WHERE CODE = ?";
		jdbcTemplate.update(sql, db.getName(), db.getCkey(), db.getNote(),
				db.getCode());
//		SysDicMap.update(db);
		dictionaryCacheImpl.editDic(db);
	}

	public DictionaryBean queryByCode(String code) {
//		DictionaryBean db = SysDicMap.getByCode(code);
		return  dictionaryCacheImpl.queryByCode(code);
	}
	

	public List<DictionaryBean> queryAll() {
		return ur.queryAllCustom("SELECT * FROM SYS_DICTIONARY order by code ,pcode desc", dbrm);
	}
	
	
	public DictionaryBean queryTree(String padcd) throws Exception{
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT CODE,NAME,PCODE FROM SYS_DICTIONARY where 1=1 ");
		if(padcd==null || "".equals(padcd)){
			sql.append(" and (PCODE is null  or PCODE='-1' )");
		}else{
			sql.append(" and PCODE =? ");
			linkedMap.put(1, padcd);
			
		}
		List<Object> rootList=ur.queryAllCustom(sql.toString(),linkedMap);
		if(rootList.size()>0){
			String jsonTree=((JSONObject)rootList.get(0)).toString();
			DictionaryBean deptBeaneanTree = JSON.parseObject(jsonTree, new TypeReference<DictionaryBean>() {});
			List<DictionaryBean> list=generateOrgMapToTree(null,deptBeaneanTree.getCode());
			deptBeaneanTree.setChildren( list);
        	return deptBeaneanTree;
		}
		return null;
	}
	
   public List<DictionaryBean> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid)  {
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
    		List<Object> childrenList=new ArrayList<Object>();;
    		String adcd="";
    		int i=0;
    		for(Object o:list){
    			JSONObject jSONObject=(JSONObject)o;
    			if(!"".equals(adcd) && !adcd.equals(jSONObject.getString("CODE")) ){
    				map.put(adcd, childrenList);
    				childrenList=new ArrayList<Object>();
    			}
    			childrenList.add(jSONObject);
    			adcd=jSONObject.getString("CODE");
    			if(i==list.size()-1){
    				map.put(adcd, childrenList);
    			}
    			i++;
    		}
    		orgMaps=map;
        }
        List<DictionaryBean> orgList = new ArrayList<>();
        if (orgMaps != null && orgMaps.size() > 0) {
        	List parenList=orgMaps.get(pid);
        	if(parenList==null){
        		return orgList;
        	}
        	DictionaryBean dictionaryBean=null;
            for (Object obj : parenList) {
            	JSONObject json=(JSONObject)obj;
            	dictionaryBean=new DictionaryBean();
            	
            	dictionaryBean.setCode(json.getString("CHILD_ID"));
            	dictionaryBean.setName(json.getString("CHILD_NAME"));
            	dictionaryBean.setCkey(json.getString("CKEY"));
            	dictionaryBean.setPcode(pid);
                List<DictionaryBean> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString());
                //将子结果集存入当前对象的children字段中
                dictionaryBean.setChildren(children);
                //添加当前对象到主结果集中
                orgList.add(dictionaryBean);
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
		return dictionaryCacheImpl.findChildList(pId);
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
