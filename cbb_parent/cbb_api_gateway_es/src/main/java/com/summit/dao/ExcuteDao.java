package com.summit.dao;

import java.util.List;

import org.elasticsearch.client.Client;

import com.google.gson.JsonArray;
import com.summit.exception.EsException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 基础增删改接口
 * @author 叶腾
 * @version 1.0
 */
public interface ExcuteDao {

	/**
	 * 基础增改接口 ,支持批量 和单个操作 ，批量操作数量建议维持在不超过5000一个批次
	 * jsonList 为多个jsObject组成 
	 * eg : js.addProperty("name","zhangsan") ; js.addProperty("age",10);js.addProperty("sex","male");jsonList.add(js);
	 * 修改和新增方法 区别 ：
	 * 新增方法中 ： js中不存在 es默认存储的_id 
	 * 修改方法中： js中必须包含 es 默认存储的_id  
	 * @param index 索引
	 * @param jsonList 批量更新的数据
	 * @param client client 连接
	 * @return 更新后的返回结果
	 * @throws EsException es异常  主要为更新异常
	 */
	public JSONArray saveOrUpdateBatchTransfor(String index, JsonArray jsonList) throws EsException;

	/**
	 * 基础删除接口， 支持批量和单个操作 ，批量操作数量建议维持在不超过5000一个批次
	 * 此处的idList 为es中默认存储的_id 
	 * @param index 索引
	 * @param idList _id  idList
	 * @param client client 连接
	 * @return 更新后的返回结果 
	 * @throws EsException es异常  主要为更新异常
	 */
	public JSONArray delete(String index, List<String> idList) throws EsException ;

	/**
	 * refresh index 方法，主要用于当执行完es 更新操作后 ，需要执行refresh ,将更新操作同步到服务器端
	 * 基础方法中默认集成refresh方法
	 * @param index 索引
	 * @param client client 连接
	 * @throws EsException es异常  主要为refresh异常
	 */
	public void refreshIndex(String index) throws EsException;

	
	/**
	 * 自定义新增接口 ,支持批量 和单个操作 ，批量操作数量建议维持在不超过5000一个批次
	 * jsonList 为多个jsObject组成
	 * eg : js.addProperty("name","zhangsan") ; js.addProperty("age",10);js.addProperty("sex","male");jsonList.add(js);
	 * @param index 索引
	 * @param jsonList  批量更新的数据
	 * @param client client 连接
	 * @return
	 * @throws EsException es异常  主要为更新异常
	 */
	public JSONArray saveCustomId(String index, JsonArray jsonList)  throws EsException ;
	
	/**
	 * 单个更新方法
	 * @param index
	 * @param jSONObject
	 * @return
	 * @throws EsException
	 */
	public JSONObject saveOrUpdate(String index, JSONObject jSONObject) throws EsException;
	
	/**
	 * 批量更新方法
	 * @param index
	 * @param jsonList
	 * @return
	 * @throws EsException
	 */

	public JSONArray saveOrUpdateBatch(String index, JSONArray jsonList) throws EsException;
}
