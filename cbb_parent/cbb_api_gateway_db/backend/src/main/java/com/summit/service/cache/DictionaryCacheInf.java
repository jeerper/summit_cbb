package com.summit.service.cache;

import java.util.List;

import org.springframework.stereotype.Component;

import com.summit.domain.dictionary.DictionaryBean;


@Component
public interface DictionaryCacheInf {
	/**
	 * 获取ckey值
	 * @param code
	 * @return
	 */
	public String queryCkeyByCode(String code);
	/**
	 * 获取name
	 * @param pcode
	 * @param ckey
	 * @return
	 */
	public String queryNameByCkeyAndPcode(String pcode,String ckey);
	/**
	 * 获取code值
	 * @param pcode
	 * @param ckey
	 * @return
	 */
	public String queryCodeByCkeyAndPcode(String pcode,String ckey);
	
	/**
	 * 获取name
	 * @param code
	 * @return
	 */
	public String queryNameByCode(String code);
	/**
	 * 获取所有的字典项
	 * @return
	 */
	public List<DictionaryBean> getAll();
	/**
	 * 获取子类集合
	 * @param pcode
	 * @return
	 */
	public  List<DictionaryBean> findChildList(String pcode);
	/**
	 * 获取字典对象
	 * @param code
	 * @return
	 */
	public DictionaryBean queryByCode(String code);
}
