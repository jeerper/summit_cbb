package com.summit.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 基础业务层 ，增删改查
 * @author 叶腾
 *
 */
public interface BaseService {
	/**
	 * 通用新增方法 ，内部先查询在新增
	 * @param index
	 * @param jsonObject
	 * @param jsList
	 * @return
	 * @throws Exception
	 */
	public JSONObject add(String index, JSONObject jsonObject, JSONArray jsList) throws Exception ;
	
	/**
	 * 通用修改方法， 先查询在修改
	 * @param index
	 * @param jsonObject
	 * @param jsList
	 * @return
	 * @throws Exception
	 */
	public JSONArray edit(String index, JSONObject jsonObject, JSONArray jsList) throws Exception;
	/**
	 * 
	 * 通用条件查询方法
	 * @param index
	 * @param jsonList
	 * @return
	 */
	public JSONArray query(String index, JSONArray jsonList);
	/**
	 * 通用分页查询方法
	 * @param index
	 * @param jsonList
	 * @param currentPage
	 * @param pageSize
	 * @param willPage
	 * @return
	 */
	public JSONArray pageQuery(String index, JSONArray jsonList, Integer currentPage, Integer pageSize,
                               Integer willPage);
	/**
	 * 通用查询所有方法 ，最多10000
	 * @param index
	 * @return
	 */
	public JSONArray queryAll(String index) ;
	/**
	 * 通用删除方法，支持批量
	 * @param index
	 * @param jsonList
	 * @return
	 * @throws Exception
	 */
	public JSONArray delete(String index, JSONArray jsonList) throws Exception ;
	/**
	 * 通用批量新增方法
	 * @param index
	 * @param jsonList
	 * @return
	 * @throws Exception
	 */
	public JSONArray saveBatch(String index, JSONArray jsonList) throws Exception;
}
