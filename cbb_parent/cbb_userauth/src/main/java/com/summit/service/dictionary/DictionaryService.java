package com.summit.service.dictionary;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.domain.dictionary.DictionaryBean;
import com.summit.domain.dictionary.DictionaryBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.service.cache.DictionaryCacheImpl;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

	public List<DictionaryBean> queryAll() {
		return ur.queryAllCustom("SELECT * FROM SYS_DICTIONARY", dbrm);
	}
	
	@SuppressWarnings("unchecked")
	public List<DictionaryBean> queryTree() {
		return  dictionaryCacheImpl.getAll();
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
