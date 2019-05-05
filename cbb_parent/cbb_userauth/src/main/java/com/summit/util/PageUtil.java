package com.summit.util;

import java.util.Iterator;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import com.alibaba.fastjson.JSONObject;

/**
 * 分页工具辅助类
 * 
 * @author ChengHu
 * CreateTime:2014-3-31上午10:50:07
 */
public class PageUtil {

	//将分页参数转换成javabean对象返回
//	public static Object convertJsonToBean(String jsonStr,Class<?> targetClass){
//		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
//		Object object = JSONObject.parseObject(jsonObject,targetClass);
//		return object;
//	}
	
	//计算起始页码
	public static Integer computePageNum(Integer start,Integer limit){
		Integer rtnValue = 0;
		if(null != start && null != limit){
			rtnValue = Integer.valueOf(start)/Integer.valueOf(limit);
		}
		return rtnValue;
	}
	
	//设置排序字段
	public static Sort setSortColumn(String sortJsonStr){
		Sort sort = null;
		if(null != sortJsonStr){
			JSONObject jsonObject = JSONObject.parseObject(sortJsonStr);
			Integer i  = 0;
			if(null != jsonObject && jsonObject.keySet().size() > 0){
				Order[] order = new Order[jsonObject.keySet().size()];
				for (Iterator iterator =  jsonObject.keySet().iterator(); iterator.hasNext();) {
					Object key = (Object) iterator.next();
					if(jsonObject.get(key).toString().equalsIgnoreCase("ASC")){
						order[i] = new Order(Direction.ASC,key.toString());
					}else if(jsonObject.get(key).toString().equalsIgnoreCase("DESC")){
						order[i] = new Order(Direction.DESC,key.toString());
					}
					i++;
				}
				sort = new Sort(order);
			}
		}
		return sort;
	}
	
	public static PageRequest createPageRequest(int start, int limit, String orderJson){
		Sort sort = setSortColumn(null);
		if(null != orderJson && orderJson.length() > 0){
			sort = PageUtil.setSortColumn(orderJson);
		}
		int current = 0;
		if(limit > 1){
			current = start/limit;
		}
		PageRequest page = new PageRequest(current, limit, sort);
		return page;
	}
}
