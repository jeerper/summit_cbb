package com.summit.dao;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 基础查询接口 包括基本查询，分页查询，分组查询
 * 
 * @author 叶腾
 * @version 1.0
 */
public interface QueryDao {

	/**
	 * 根据字段查询数据 字段可为多个字段组合查询 jsonList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ"); jsList.add(js);
	 * 此方法构成了一个js查询条件
	 * 
	 * @param index
	 *            索引 ，需要查询的表
	 * @param jsonList
	 *            查询条件
	 * @param client
	 *            client连接
	 * @return 返回查到的结果集 jsonObject数组
	 */
	public JSONArray findByField(String index, JSONArray jsonList);

	/**
	 * 根据字段查询id jsonList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ"); jsList.add(js);
	 * 此方法构成了一个js查询条件
	 * 
	 * @param index
	 *            索引 ，需要查询的表
	 * @param jsonList
	 *            查询条件
	 * @param client
	 *            client连接
	 * @return 返回查到的结果集 jsonObject数组
	 */
	public JSONArray findIdByField(String index, JSONArray jsonList);

	/**
	 * 分页方法 jsList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum 
	 * eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ");
	 *  jsList.add(js); 此方法构成了一个js查询条件
	 * 
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param jsList
	 *            查询条件
	 * @param willPage
	 *            将要跳转页面
	 * @param index
	 *            索引
	 * @param client
	 *            client连接
	 * @return 查询结果
	 * 
	 */
	public JSONArray pageSearch(Integer currentPage, Integer pageSize, JSONArray jsList, Integer willPage, String index);

	/**
	 * 无条件分页方法
	 * 
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param willPage
	 *            将要跳转页面
	 * @param index
	 *            索引
	 * @param client
	 *            client连接
	 * @return 查询结果
	 * 
	 */
	public JSONArray pageNormal(Integer currentPage, Integer pageSize, Integer willPage, String index);

	/**
	 * 有查询条件时分页方法 jsArray是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ"); jsArray.add(js);
	 * 此方法构成了一个js查询条件
	 * 
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param jsArray
	 *            查询条件
	 * @param willPage
	 *            将要跳转页面
	 * @param index
	 *            索引
	 * @param client
	 *            client连接
	 * @return 查询结果
	 * 
	 */
	public JSONArray pageCondition(Integer currentPage, Integer pageSize, JSONArray jsArray, Integer willPage,
                                   String index);

	/**
	 * 分组求最大值
	 * 
	 * @param index
	 *            索引
	 * @param groupField
	 *            按照某个字段分组
	 * @param maxField
	 *            求某个字段聚合函数最大值
	 * @param client
	 *            client 连接
	 * @return 查询结果
	 */
	public JSONArray groupMax(String index, String groupField, String maxField);

	/**
	 * 根据条件查询分组求最大值 jSONObjectList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ");
	 * jSONObjectList.add(js); 此方法构成了一个js查询条件
	 * 
	 * @param jSONObjectList
	 *            查询条件
	 * @param index
	 *            索引
	 * @param groupField
	 *            按照某个字段分组加排序 key: 字段 value:asc or desc
	 * @param maxField
	 *            求某个字段聚合函数最大值
	 * @param client
	 *            client 连接
	 * @return 查询结果
	 */
	public JSONArray groupMaxByCondition(JSONArray jSONObjectList, String index, String groupField, String maxField);

	/**
	 * 多个分组条件，查询条件组合查询 jSONObjectList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ");
	 * jSONObjectList.add(js); 此方法构成了一个js查询条件 groupFieldJson : key:为字段名 ：
	 * value："asc or desc" 根据字段分组并排序 ，若希望按照某个字段先排序 ，请放在第一个 eg: js.put("name"
	 * ,"asc"); aggFieldJson ：多个字段聚合条件 , key:为字段名： value为 "min,max...." 可参考枚举类：
	 * StatsEnum eg: js.put("age" ,"MAX");
	 * 
	 * @param jSONObjectList
	 *            查询条件
	 * @param index
	 *            索引
	 * @param groupFieldJson
	 *            多个字段分组加排序 key: 字段 value:asc or desc
	 * @param aggFieldJson
	 *            多个字段 按照需求求聚合结果 key:字段 value : MAX,MIN..etc
	 * @param client
	 *            client连接
	 * @return 查询结果
	 */
	public JSONArray groupConditions(JSONArray jSONObjectList, String index, JSONObject groupFieldJson,
                                     JSONObject aggFieldJson);

	/**
	 * 单独求聚合函数 jSONObjectList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ");
	 * jSONObjectList.add(js); 此方法构成了一个js查询条件
	 * 
	 * @param jSONObjectList
	 *            查询条件
	 * @param index
	 *            索引
	 * @param aggField
	 *            聚合字段
	 * @param client
	 *            client连接
	 * @param stats
	 *            聚合函数 : MAX,MIN..etc
	 * @return 查询结果
	 */
	public JSONObject aggCondition(JSONArray jSONObjectList, String index, String aggField,
                                   String stats);

	/**
	 * 分组求最小值
	 * 
	 * @param index
	 *            索引
	 * @param groupField
	 *            分组字段
	 * @param minField
	 *            求某个字段聚合函数最小值
	 * @param client
	 *            client连接
	 * @return 查询结果
	 */
	public JSONArray groupMin(String index, String groupField, String minField);

	/**
	 * 根据条件分组求最小值 jSONObjectList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ");
	 * jSONObjectList.add(js); 此方法构成了一个js查询条件
	 * 
	 * @param jSONObjectList
	 *            查询条件
	 * @param index
	 *            索引
	 * @param groupField
	 *            分组字段
	 * @param minField
	 *            求某个字段聚合函数最小值
	 * @param client
	 *            client连接
	 * @return 查询结果
	 */
	public JSONArray groupMinByCondition(JSONArray jSONObjectList, String index, String groupField, String minField);

	/**
	 * 分组求平均值
	 * 
	 * @param index
	 *            索引
	 * @param groupField
	 *            分组字段
	 * @param avgField
	 *            求某个字段聚合函数平均值
	 * @param client
	 *            client连接
	 * @return 查询结果
	 */
	public JSONArray groupAvg(String index, String groupField, String avgField);

	/**
	 * 根据条件分组求平均值 jSONObjectList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ");
	 * jSONObjectList.add(js); 此方法构成了一个js查询条件
	 * 
	 * @param jSONObjectList
	 *            查询条件
	 * @param index
	 *            索引
	 * @param groupField
	 *            分组字段
	 * @param avgField
	 *            求某个字段聚合函数平均值
	 * @param client
	 *            client连接
	 * @return 查询结果
	 */
	public JSONArray groupAvgByCondition(JSONArray jSONObjectList, String index, String groupField, String avgField);

	/**
	 * 分组后求sum
	 * @param index
	 *            索引
	 * @param groupField
	 *            分组字段
	 * @param sumField
	 *            求某个字段聚合函数总和
	 * @param client
	 *            求某个字段聚合函数总和
	 * @return 查询结果
	 */
	public JSONArray groupSum(String index, String groupField, String sumField);

	/**
	 * jSONObjectList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ");
	 * jSONObjectList.add(js); 此方法构成了一个js查询条件
	 * 
	 * @param jSONObjectList
	 *            查询条件
	 * @param index
	 *            索引
	 * @param groupField
	 *            分组字段
	 * @param sumField
	 *            求某个字段聚合函数总和
	 * @param client
	 *            client连接
	 * @return 查询结果
	 */
	public JSONArray groupSumByCondition(JSONArray jSONObjectList, String index, String groupField, String sumField);

	/**
	 * 分组后求count数
	 * @param index
	 *            索引
	 * @param groupField
	 *            分组字段
	 * @param countField
	 *            求某个字段聚合函数数量
	 * @param client
	 *            client连接
	 * @return 查询结果
	 */
	public JSONArray groupCount(String index, String groupField, String countField);

	/**
	 * jSONObjectList是每个字段的jsonObject的组合 jsonObject
	 * 格式:key:"COLUMNNAME",value:字段名;key:"SORT","asc or
	 * desc";key:"VALUE",value:查询字段的条件(值);key:"OPERATOR",value:"查询条件
	 * EQ,LIKE,IN...." jsonObject key可参考 枚举类ConditionEnum ，查询条件OPERATOR
	 * 参考枚举类：OperatorEnum eg: js.put("COLUMNNAME","name") ; js.put("SORT","asc")
	 * ;js.put("VALUE","zhangsan");js.put("OPERATOR","EQ");
	 * jSONObjectList.add(js); 此方法构成了一个js查询条件
	 * 
	 * @param jSONObjectList
	 *            查询条件
	 * @param index
	 *            索引
	 * @param groupField
	 *            分组字段
	 * @param countField
	 *            求某个字段聚合函数数量
	 * @param client
	 *            client连接
	 * @return 查询结果
	 */
	public JSONArray groupCountByCondition(JSONArray jSONObjectList, String index, String groupField, String countField);

	/**
	 * 分组聚合后求所有字段的值,并展示分页后的结果
	 * @param currentPage
	 * @param pageSize
	 * @param jsList
	 * @param willPage
	 * @param index
	 * @param groupFieldJson
	 * @param aggFieldJson
	 * @return
	 */
	public Map groupConditionPage(Integer currentPage, Integer pageSize, JSONArray jsList, Integer willPage, String index, JSONObject groupFieldJson, JSONObject aggFieldJson);
	
	/**
	 * 分组后求sum,并根据having过滤
	 * @param jSONObjectList
	 * @param index
	 * @param groupField
	 * @param sumField
	 * @param havingList
	 * @return
	 */
	public JSONArray groupSumHavingCondition(JSONArray jSONObjectList, String index, String groupField, String sumField, JSONArray havingList);
	/**
	 * sql
	 */
	public void test();

	/**
	 * 查询所有
	 * @param index
	 * @return
	 */
	JSONArray findAll(String index);
	
	/**
	 * 根据sql查询
	 * @param sql
	 * @return
	 */
	public JSONArray queryBySql(String sql);

	/**
	 * 根据id批量查询方法
	 * @param index
	 * @param idList
	 * @return
	 */
	JSONArray mGetSearch(String index, String idList);
}
