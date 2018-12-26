package com.summit.service.cache;

import com.summit.domain.dictionary.DictionaryBean;
import com.summit.util.SysConstants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("dictionaryCacheImpl")
public class DictionaryCacheImpl extends CacheImpl implements DictionaryCacheInf{
	
	/**
	 * 新增数据到缓存
	 * 1.添加到缓存本身
	 * 2.修改缓存all对象
	 * @param db
	 */
	@SuppressWarnings("unchecked")
	public void addDic(DictionaryBean db){
		setCacheElement(SysConstants.DICTIONARY, db.getCode(), db);
		List<DictionaryBean>  all = (List<DictionaryBean>)getCacheElement(SysConstants.DICTIONARY, "dictionaryAll");
		all.add(db);
		setCacheElement(SysConstants.DICTIONARY, "dictionaryAll", all);
	}
	
	/**
	 * 修改缓存数据
	 * 1.修改本身数据
	 * 2.修改缓存all对象
	 * @param db
	 */
	@SuppressWarnings("unchecked")
	public void editDic(DictionaryBean db){
		setCacheElement(SysConstants.DICTIONARY, db.getCode(), db);
		List<DictionaryBean>  all = (List<DictionaryBean>)getCacheElement(SysConstants.DICTIONARY, "dictionaryAll");
		for (DictionaryBean dictionaryBean : all) {
			if(db.getCode().equals(dictionaryBean.getCode())){
				dictionaryBean.setName(db.getName());
				dictionaryBean.setCkey(db.getCkey());
				dictionaryBean.setNote(db.getNote());
			}
		}
		setCacheElement(SysConstants.DICTIONARY, "dictionaryAll", all);
	}
	
	/**
	 * 删除缓存数据同时修改缓存all对象
	 * @param code
	 */
	@SuppressWarnings("unchecked")
	public void delDic(String code){
		removeCacheElement(SysConstants.DICTIONARY, code);
		List<DictionaryBean>  all = (List<DictionaryBean>)getCacheElement(SysConstants.DICTIONARY, "dictionaryAll");
		boolean flag = false;
		int i = 0;
		for (;i < all.size(); i++) {
			if(code.equals(all.get(i).getCode())){
				flag = true;
				break;
			}
		}
		if(flag){
			all.remove(i);
		}
		setCacheElement(SysConstants.DICTIONARY, "dictionaryAll", all);
	}
	
	/**
	 * 根据父级节点找到子节点集合
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<DictionaryBean> findChildList(String code) {
		List<DictionaryBean>  all = (List<DictionaryBean>)getCacheElement(SysConstants.DICTIONARY, "dictionaryAll");
		List<DictionaryBean> list = new ArrayList<DictionaryBean>();
		for (DictionaryBean sysDictionary : all) {
			if (code.equals(sysDictionary.getPcode())) {
				list.add(sysDictionary.clone());
			}
		}
		return list;
	}
	
	/**
	 * 根据code值返回ckey
	 * @param code
	 * @return
	 */
	public String queryCkeyByCode(String code){
		DictionaryBean dbBean = (DictionaryBean) getCacheElement(SysConstants.DICTIONARY, code);
		String ckeyString = "";
		if(dbBean != null){
			ckeyString = dbBean.getCkey();
		}
		return ckeyString;
	}
	
	/**
	 * 根据父级code和子节点的ckey获取字典名称
	 * @param pcode
	 * @param ckey
	 * @return
	 */
	public String queryNameByCkeyAndPcode(String pcode,String ckey){
		 List<DictionaryBean> cList =  findChildList(pcode);
		 String nameString = "";
		 if(cList.size() > 0){
			 for (DictionaryBean dictionaryBean : cList) {
				if(ckey.equals(dictionaryBean.getCkey())){
					nameString = dictionaryBean.getName();
					break;
				}
			}
		 }
		 return nameString;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.summit.frame.ehcache.DictionaryCacheInf#queryCodeByCkeyAndPcode(java.lang.String, java.lang.String)
	 */
	public String queryCodeByCkeyAndPcode(String pcode, String ckey) {
		List<DictionaryBean> cList =  findChildList(pcode);
		 String codeString = "";
		 if(cList.size() > 0){
			 for (DictionaryBean dictionaryBean : cList) {
				if(ckey.equals(dictionaryBean.getCkey())){
					codeString = dictionaryBean.getCode();
					break;
				}
			}
		 }
		 return codeString;
	}
	
	/**
	 * 根据编码转换名称
	 * @param code
	 * @return
	 */
	public String queryNameByCode(String code){
		DictionaryBean dbBean = (DictionaryBean) getCacheElement(SysConstants.DICTIONARY, code);
		String nameString = "";
		if(dbBean != null){
			nameString = dbBean.getName();
		}
		return nameString;
	}
	
	/**
	 * 从内从中拿出所有的字典项
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DictionaryBean> getAll(){
		List<DictionaryBean>   all = (List<DictionaryBean>)getCacheElement(SysConstants.DICTIONARY, "dictionaryAll");
		List<DictionaryBean> list = new ArrayList<DictionaryBean>();
		for (DictionaryBean db : all) {
			DictionaryBean dbBean = db.clone();
			if(dbBean.getPcode() == null){
				dbBean.setOpen(true);
			}
			list.add(dbBean);
		}
		return list;
	}
	
	/**
	 * 通过编码从内存中获取字典项
	 * @param code
	 * @return
	 */

	public DictionaryBean queryByCode(String code){
		DictionaryBean db=  (DictionaryBean) getCacheElement(SysConstants.DICTIONARY, code);
		if(db != null){
			return db.clone();
		}
		return db;
	}

}
