package com.summit.dao;

import java.util.List;

import org.elasticsearch.client.Client;

import net.sf.json.JSONArray;
/**
 * 
 * @author yt
 *
 */
public interface QueryRainDao {
	/**
	 * 按照day统计
	 * @param index
	 * @param time
	 * @param stcdList
	 * @param startTime
	 * @param groupField
	 * @param sumField
	 * @return
	 */
	public JSONArray groupDayTime(String index, Object time, List<String> stcdList, String startTime, String groupField,
                                  String sumField);
/**
 * 按照旬统计
 * @param index
 * @param timeInterval
 * @param stcdList
 * @param startTime
 * @param groupField
 * @param sumField
 * @return
 */
	public JSONArray groupTenDayTime(String index, Object timeInterval, List<String> stcdList, String startTime,
                                     String groupField, String sumField);

	/**
	 * 按照月统计
	 * @param index
	 * @param timeInterval
	 * @param stcdList
	 * @param startTime
	 * @param groupField
	 * @param sumField
	 * @return
	 */
	public JSONArray groupMonthTime(String index, Object timeInterval, List<String> stcdList, String startTime,
                                    String groupField, String sumField);

	/**
	 * 按照年统计
	 * @param index
	 * @param timeInterval
	 * @param stcdList
	 * @param startTime
	 * @param groupField
	 * @param sumField
	 * @return
	 */
	public JSONArray groupYearTime(String index, Object timeInterval, List<String> stcdList, String startTime,
                                   String groupField, String sumField);
}
