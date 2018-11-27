package com.summit.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;
import org.elasticsearch.search.aggregations.metrics.stats.InternalStats;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.valuecount.InternalValueCount;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.bucketselector.BucketSelectorPipelineAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Repository;

import com.summit.config.ESConfig;
import com.summit.dao.QueryDao;
import com.summit.domain.ConditionEnum;
import com.summit.util.ConnectionUtil;
import com.summit.util.EsThread;
import com.summit.util.SummitThreadFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * 
 * @author yt
 *
 */
@Repository
public class QueryDaoImpl implements QueryDao {
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

	String url = ESConfig.url;

	@Override
	public JSONArray pageSearch(Integer currentPage, Integer pageSize, JSONArray jsList, Integer willPage,
			String index) {
		if (null == jsList || jsList.size() == 0) {
			return pageNormal(currentPage, pageSize, willPage, index);
		} else {
			return pageCondition(currentPage, pageSize, jsList, willPage, index);
		}
	}

	@Override
	public JSONArray pageNormal(Integer currentPage, Integer pageSize, Integer willPage, String index) {
		Client client = elasticsearchTemplate.getClient();
		int start = 0;
		SearchResponse response = null;
		// 如果不传任何条件,默认按照_id查询10000个
		QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
		start = (willPage - 1) * pageSize;
		JSONArray jsonList = new JSONArray();
		JSONObject js = null;
		JSONObject countJs = new JSONObject();
		// 默认按照_id排序
		response = client.prepareSearch(index).setFrom(start).setSize(pageSize).setQuery(queryBuilder).execute()
				.actionGet();
		String lastOne = null;
		// 取出总数
		countJs.put("count", response.getHits().totalHits);
		jsonList.add(countJs);
		if (response.getHits().getHits().length > 0) {
			for (SearchHit hit : response.getHits().getHits()) {
				lastOne = hit.getSourceAsString();
				if (null != lastOne) {
					js = JSONObject.fromObject(lastOne);
					jsonList.add(js);
				}
			}
		}
		return jsonList;
	}

	@Override
	public JSONArray pageCondition(Integer currentPage, Integer pageSize, JSONArray jsArray, Integer willPage,
			String index) {
		Client client = elasticsearchTemplate.getClient();
		int start = 0;
		SearchResponse response = null;
		Map<String, String> sortMap = null;
		JSONArray jsonList = null;
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (null != jsArray && jsArray.size() > 0) {
			sortMap = new LinkedHashMap<String, String>();
			for (int i = 0; i < jsArray.size(); i++) {
				JSONObject js = jsArray.getJSONObject(i);
				Iterator iterator = js.keys();
				while (iterator.hasNext()) {
					QueryBuilder mustQb = null;
					QueryBuilder shouldQb = null;
					String key = (String) iterator.next();
					if (ConditionEnum.OPERATOR.toString().equalsIgnoreCase(key)) {
						Object value = js.get(key);
						if (null != value) {
							String operator = value.toString();
							switch (operator) {
							case "EQ":
								eqBuilderQuery(js, mustQb, boolQueryBuilder);
								break;
							case "LIKE":
								likeBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "IN":
								inBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "NOT_IN":
								notInBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "GT":
								gtBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "GTE":
								gteBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "LT":
								ltBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "LTE":
								lteBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "NE":
								neBuilderQuery(js, mustQb, boolQueryBuilder);
								break;
							case "IS_NULL":
								isNullBuilderQuery(js, mustQb, boolQueryBuilder);
								break;
							case "NOT_NULL":
								isNotNullBuilderQuery(js, mustQb, boolQueryBuilder);
								break;
							default:
								return null;
							}
						}
					}
					if (ConditionEnum.SORT.toString().equalsIgnoreCase(key)) {
						sortMap.put(js.get(ConditionEnum.COLUMNNAME.toString()).toString(),
								js.get(ConditionEnum.SORT.toString()).toString());
					}
				}
			}
			start = (willPage - 1) * pageSize;
			SearchRequestBuilder sb = client.prepareSearch(index).setFrom(start).setSize(pageSize)
					.setPostFilter(boolQueryBuilder);
			if (null != sortMap && sortMap.size() > 0) {
				for (Map.Entry<String, String> entry : sortMap.entrySet()) {
					if ("asc".equalsIgnoreCase(entry.getValue())) {
						sb.addSort(entry.getKey(), SortOrder.ASC);
					} else {
						sb.addSort(entry.getKey(), SortOrder.DESC);
					}
				}
			}
			response = sb.setVersion(true).execute().actionGet();
			jsonList = new JSONArray();
			JSONObject countJson = new JSONObject();
			countJson.put("count", response.getHits().totalHits);
			jsonList.add(countJson);
			JSONObject js;
			String lastOne = "";
			if (response.getHits().getHits().length > 0) {
				for (SearchHit hit : response.getHits().getHits()) {
					Long version = hit.getVersion();
					lastOne = hit.getSourceAsString();
					if (null != lastOne) {
						js = JSONObject.fromObject(lastOne);
						Iterator it = js.keys();
						while (it.hasNext()) {
							String key = (String) it.next();
							Object value = js.get(key);
							if (value == null || String.valueOf(value).equalsIgnoreCase("null")) {
								js.put(key, "");
							} else {
								js.put(key, value);
							}
						}
						js.put("version", version);
						jsonList.add(js);
					}
				}
			}
		}
		return jsonList;
	}

	private void lteBuilderQuery(JSONObject js, QueryBuilder mustQb, QueryBuilder shouldQb,
			BoolQueryBuilder boolQueryBuilder) {
		// 表示 <= 需要用到范围查询
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			// 判断是否为时间参数 如果是时间参数 ，需要进行long转换
			/*
			 * if (DateUtil.valiDateTimeWithLongFormat(conditionValue)) { long
			 * time = Timestamp.valueOf(conditionValue).getTime(); mustQb =
			 * QueryBuilders.rangeQuery(js.get(ConditionEnum.COLUMNNAME.toString
			 * ()).toString()).to(time) .includeUpper(true); } else {
			 */
			mustQb = QueryBuilders.rangeQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString()).to(conditionValue)
					.includeUpper(true);
			// }
			boolQueryBuilder.must(mustQb);
		}
	}

	private void ltBuilderQuery(JSONObject js, QueryBuilder mustQb, QueryBuilder shouldQb,
			BoolQueryBuilder boolQueryBuilder) {
		// 表示 < 需要用到范围查询
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			// 判断是否为时间参数 如果是时间参数 ，需要进行long转换
			/*
			 * if (DateUtil.valiDateTimeWithLongFormat(conditionValue)) { long
			 * time = Timestamp.valueOf(conditionValue).getTime(); mustQb =
			 * QueryBuilders.rangeQuery(js.get(ConditionEnum.COLUMNNAME.toString
			 * ()).toString()).to(time) .includeUpper(false); } else {
			 */
			mustQb = QueryBuilders.rangeQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString()).to(conditionValue)
					.includeUpper(false);
			// }
			boolQueryBuilder.must(mustQb);
		}
	}

	private void gteBuilderQuery(JSONObject js, QueryBuilder mustQb, QueryBuilder shouldQb,
			BoolQueryBuilder boolQueryBuilder) {
		// 表示 >= 需要用到范围查询
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			// 判断是否为时间参数 如果是时间参数 ，需要进行long转换
			/*
			 * if (DateUtil.valiDateTimeWithLongFormat(conditionValue)) { long
			 * time = Timestamp.valueOf(conditionValue).getTime(); mustQb =
			 * QueryBuilders.rangeQuery(js.get(ConditionEnum.COLUMNNAME.toString
			 * ()).toString()).from(time) .includeLower(true); } else {
			 */
			mustQb = QueryBuilders.rangeQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString())
					.from(conditionValue).includeLower(true);
			// }
			boolQueryBuilder.must(mustQb);
		}
	}

	private void gtBuilderQuery(JSONObject js, QueryBuilder mustQb, QueryBuilder shouldQb,
			BoolQueryBuilder boolQueryBuilder) {
		// 表示 > 需要用到范围查询
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			// 判断是否为时间参数 如果是时间参数 ，需要进行long转换
			/*
			 * if (DateUtil.valiDateTimeWithLongFormat(conditionValue)) { long
			 * time = Timestamp.valueOf(conditionValue).getTime(); mustQb =
			 * QueryBuilders.rangeQuery(js.get(ConditionEnum.COLUMNNAME.toString
			 * ()).toString()).from(time) .includeLower(false); } else {
			 */
			mustQb = QueryBuilders.rangeQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString())
					.from(conditionValue).includeLower(false);
			// }
			boolQueryBuilder.must(mustQb);
		}
	}

	private void notInBuilderQuery(JSONObject js, QueryBuilder mustQb, QueryBuilder shouldQb,
			BoolQueryBuilder boolQueryBuilder) {
		// 如果用not_in, 代表一个或多个值 !=
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			String comma = ",";
			String symbol = "//,";
			if (conditionValue.contains(comma)) {
				// 判断可能会出现包含","，而逗号作为标识符需要特别标注
				if (conditionValue.contains(symbol)) {
					String[] values = conditionValue.toString().split(",");
					for (int j = 0; j < values.length; j++) {
						String valueOr = values[j];
						if (valueOr.equalsIgnoreCase("//")) {
						} else {
							if (null == shouldQb) {
								shouldQb = QueryBuilders.boolQuery().mustNot(QueryBuilders
										.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), valueOr));
							} else {
								shouldQb = QueryBuilders.boolQuery().should(shouldQb).mustNot(QueryBuilders
										.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), valueOr));
							}
						}
					}
					shouldQb = QueryBuilders.boolQuery().should(shouldQb).mustNot(
							QueryBuilders.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), ","));
					boolQueryBuilder.must(shouldQb);
				} else {
					String[] values = conditionValue.toString().split(",");
					for (int j = 0; j < values.length; j++) {
						String valueOr = values[j];
						if (null == shouldQb) {
							shouldQb = QueryBuilders.boolQuery().mustNot(QueryBuilders
									.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), valueOr));
						} else {
							shouldQb = QueryBuilders.boolQuery().should(shouldQb).mustNot(QueryBuilders
									.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), valueOr));
						}
					}
					boolQueryBuilder.must(shouldQb);
				}
			} else {
				// 如果只有一个值
				mustQb = QueryBuilders.boolQuery().mustNot(QueryBuilders
						.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), conditionValue));
				boolQueryBuilder.must(mustQb);
			}
		}
	}

	private void inBuilderQuery(JSONObject js, QueryBuilder mustQb, QueryBuilder shouldQb,
			BoolQueryBuilder boolQueryBuilder) {
		// 如果用in, 代表一个或多个值 =
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			String comma = ",";
			String symbol = "//,";
			if (conditionValue.contains(comma)) {
				// 判断可能会出现包含","，而逗号作为标识符需要特别标注
				if (conditionValue.contains(symbol)) {
					String[] values = conditionValue.toString().split(",");
					for (int j = 0; j < values.length; j++) {
						String valueOr = values[j];
						if (valueOr.equalsIgnoreCase("//")) {
						} else {
							if (null == shouldQb) {
								shouldQb = QueryBuilders.boolQuery().should(QueryBuilders
										.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), valueOr));
							} else {
								shouldQb = QueryBuilders.boolQuery().should(shouldQb).should(QueryBuilders
										.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), valueOr));
							}
						}
					}
					shouldQb = QueryBuilders.boolQuery().should(shouldQb).should(
							QueryBuilders.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), ","));
					boolQueryBuilder.must(shouldQb);
				} else {
					String[] values = conditionValue.toString().split(",");
					for (int j = 0; j < values.length; j++) {
						String valueOr = values[j];
						if (null == shouldQb) {
							shouldQb = QueryBuilders.boolQuery().should(QueryBuilders
									.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), valueOr));
						} else {
							shouldQb = QueryBuilders.boolQuery().should(shouldQb).should(QueryBuilders
									.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), valueOr));
						}
					}
					boolQueryBuilder.must(shouldQb);
				}
			} else {
				// 如果只有一个值
				mustQb = QueryBuilders.boolQuery().must(QueryBuilders
						.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), conditionValue));
				boolQueryBuilder.must(mustQb);
			}
		}
	}

	private void likeBuilderQuery(JSONObject js, QueryBuilder shouldQb, QueryBuilder mustQb,
			BoolQueryBuilder boolQueryBuilder) {
		// like模糊查询,支持一个或者多个相同字段,无需判断是否为时间字段，like只支持string
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			String comma = ",";
			String symbol = "//,";
			if (conditionValue.contains(comma)) {
				// 判断可能会出现包含","，而逗号作为标识符需要特别标注
				if (conditionValue.contains(symbol)) {
					String[] values = conditionValue.toString().split(",");
					for (int j = 0; j < values.length; j++) {
						String valueOr = values[j];
						if (valueOr.equalsIgnoreCase("//")) {
						} else {
							if (null == shouldQb) {
								shouldQb = QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery(
										js.get(ConditionEnum.COLUMNNAME.toString()).toString(), "*" + valueOr + "*"));
							} else {
								shouldQb = QueryBuilders.boolQuery().should(shouldQb)
										.should(QueryBuilders.wildcardQuery(
												js.get(ConditionEnum.COLUMNNAME.toString()).toString(),
												"*" + valueOr + "*"));
							}
						}
					}
					shouldQb = QueryBuilders.boolQuery().should(shouldQb).should(
							QueryBuilders.wildcardQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), "*,*"));
					boolQueryBuilder.must(shouldQb);
				} else {
					String[] values = conditionValue.toString().split(",");
					for (int j = 0; j < values.length; j++) {
						String valueOr = values[j];
						if (null == shouldQb) {
							shouldQb = QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery(
									js.get(ConditionEnum.COLUMNNAME.toString()).toString(), "*" + valueOr + "*"));
						} else {
							shouldQb = QueryBuilders.boolQuery().should(shouldQb).should(QueryBuilders.wildcardQuery(
									js.get(ConditionEnum.COLUMNNAME.toString()).toString(), "*" + valueOr + "*"));
						}
					}
					boolQueryBuilder.must(shouldQb);
				}
			} else {
				// 如果只有一个值
				mustQb = QueryBuilders.boolQuery().must(QueryBuilders.wildcardQuery(
						js.get(ConditionEnum.COLUMNNAME.toString()).toString(), "*" + conditionValue + "*"));
				boolQueryBuilder.must(mustQb);
			}
		}
	}

	/**
	 * 判断为空情况
	 * 
	 * @param js
	 * @param mustQb
	 * @param boolQueryBuilder
	 */
	private void isNullBuilderQuery(JSONObject js, QueryBuilder mustQb, BoolQueryBuilder boolQueryBuilder) {
		if (null != js && js.size() > 0) {
			mustQb = QueryBuilders.boolQuery()
					.mustNot(QueryBuilders.existsQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString()));
		}

		boolQueryBuilder.must(mustQb);
	}

	private void isNotNullBuilderQuery(JSONObject js, QueryBuilder mustQb, BoolQueryBuilder boolQueryBuilder) {
		if (null != js && js.size() > 0) {
			mustQb = QueryBuilders.boolQuery()
					.must(QueryBuilders.existsQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString()));
		}
		boolQueryBuilder.must(mustQb);
	}

	/**
	 * 判断不相等条件的方法
	 * 
	 * @param js
	 * @param mustQb
	 * @param boolQueryBuilder
	 */
	private void neBuilderQuery(JSONObject js, QueryBuilder mustQb, BoolQueryBuilder boolQueryBuilder) {
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			mustQb = QueryBuilders.boolQuery().mustNot(
					QueryBuilders.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), conditionValue));
		}
		boolQueryBuilder.must(mustQb);
	}

	/**
	 * 判断相等条件时候的方法
	 * 
	 * @param js
	 * @param mustQb
	 * @return
	 */
	private void eqBuilderQuery(JSONObject js, QueryBuilder mustQb, BoolQueryBuilder boolQueryBuilder) {
		if (null != js.get(ConditionEnum.VALUE.toString())) {
			String conditionValue = js.get(ConditionEnum.VALUE.toString()).toString();
			// 判断是否为时间参数 如果是时间参数 ，需要进行long转换
			/*
			 * if (DateUtil.valiDateTimeWithLongFormat(conditionValue)) { long
			 * time = Timestamp.valueOf(conditionValue).getTime(); mustQb =
			 * QueryBuilders.boolQuery()
			 * .must(QueryBuilders.matchQuery(js.get(ConditionEnum.COLUMNNAME.
			 * toString()).toString(), time)); } else {
			 */
			mustQb = QueryBuilders.boolQuery().must(
					QueryBuilders.matchQuery(js.get(ConditionEnum.COLUMNNAME.toString()).toString(), conditionValue));

			// }
		}
		boolQueryBuilder.must(mustQb);
	}

	/**
	 * 通用构建boolquery的方法
	 * 
	 * @param jsonList
	 * @param sortMap
	 * @param boolQueryBuilder
	 * @param index
	 * @param client
	 * @return
	 */
	private SearchRequestBuilder buildBoolQuery(JSONArray jsonList, Map<String, String> sortMap,
			BoolQueryBuilder boolQueryBuilder, String index, Client client) {
		SearchRequestBuilder sb = null;
		if (null != jsonList && jsonList.size() > 0) {
			sortMap = new LinkedHashMap<String, String>();
			for (int i = 0; i < jsonList.size(); i++) {
				JSONObject js = jsonList.getJSONObject(i);
				Iterator iterator = js.keys();
				while (iterator.hasNext()) {
					QueryBuilder mustQb = null;
					QueryBuilder shouldQb = null;
					String key = (String) iterator.next();
					if (ConditionEnum.OPERATOR.toString().equalsIgnoreCase(key)) {
						Object value = js.get(key);
						if (null != value) {
							String operator = value.toString();
							switch (operator) {
							case "EQ":
								eqBuilderQuery(js, mustQb, boolQueryBuilder);
								break;
							case "LIKE":
								likeBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "IN":
								inBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "NOT_IN":
								notInBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "GT":
								gtBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "GTE":
								gteBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "LT":
								ltBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "LTE":
								lteBuilderQuery(js, shouldQb, mustQb, boolQueryBuilder);
								break;
							case "NE":
								neBuilderQuery(js, mustQb, boolQueryBuilder);
								break;
							case "ISNULL":
								isNullBuilderQuery(js, mustQb, boolQueryBuilder);
								break;
							case "NOT_NULL":
								isNotNullBuilderQuery(js, mustQb, boolQueryBuilder);
								break;
							default:
								return null;
							}
						}
					}
					if (ConditionEnum.SORT.toString().equalsIgnoreCase(key)) {
						sortMap.put(js.get(ConditionEnum.COLUMNNAME.toString()).toString(),
								js.get(ConditionEnum.SORT.toString()).toString());
					}
				}

			}
			sb = client.prepareSearch(index).setQuery(boolQueryBuilder);
			if (null != sortMap && sortMap.size() > 0) {
				for (Map.Entry<String, String> entry : sortMap.entrySet()) {
					if ("asc".equalsIgnoreCase(entry.getValue())) {
						sb.addSort(entry.getKey(), SortOrder.ASC);
					} else {
						sb.addSort(entry.getKey(), SortOrder.DESC);
					}
				}
			}
		}
		return sb;
	}

	@Override
	public JSONArray groupMax(String index, String groupField, String maxField) {
		Client client = elasticsearchTemplate.getClient();
		BucketOrder order = BucketOrder.key(true);
		JSONArray jsonList = new JSONArray();
		AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000)
				.order(order);
		aggregationBuilder = maxAggBuilder(aggregationBuilder, maxField);
		SearchResponse sr = client.prepareSearch(index).setTypes("doc").addAggregation(aggregationBuilder).setSize(0)
				.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
		Terms teamAgg = (Terms) aggMap.get(groupField);
		if (null != teamAgg) {
			for (Terms.Bucket entry : teamAgg.getBuckets()) {
				JSONObject js = new JSONObject();
				InternalMax countTerm = entry.getAggregations().get(maxField);
				js.put(groupField, entry.getKey());
				js.put(maxField,
						String.valueOf(countTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : countTerm.getValue());
				jsonList.add(js);
			}
		}
		// System.out.println(jsonList);
		return jsonList;
	}

	@Override
	public JSONArray groupMaxByCondition(JSONArray jSONObjectList, String index, String groupField, String maxField) {

		Map<String, String> sortMap = null;
		JSONArray jsonList = new JSONArray();
		if (null != jSONObjectList && jSONObjectList.size() > 0) {
			Client client = elasticsearchTemplate.getClient();
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			SearchRequestBuilder sb = buildBoolQuery(jSONObjectList, sortMap, boolQueryBuilder, index, client);
			AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000);
			aggregationBuilder = maxAggBuilder(aggregationBuilder, maxField);
			SearchResponse sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
			Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
			Terms teamAgg = (Terms) aggMap.get(groupField);
			if (null != teamAgg) {
				for (Terms.Bucket entry : teamAgg.getBuckets()) {
					JSONObject jsonObject = new JSONObject();
					InternalMax countTerm = entry.getAggregations().get(maxField);
					jsonObject.put(groupField, entry.getKey());
					jsonObject.put(maxField, String.valueOf(countTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0
							: countTerm.getValue());
					jsonList.add(jsonObject);
				}
			}
		} else {
			jsonList = this.groupMax(index, groupField, maxField);
		}
		return jsonList;
	}

	@Override
	public JSONArray groupConditions(JSONArray jSONObjectList, String index, JSONObject groupFieldJson,
			JSONObject aggFieldJson) {
		Client client = elasticsearchTemplate.getClient();
		Map<String, String> sortMap = null;
		JSONArray jsonList = new JSONArray();
		SearchRequestBuilder sb = null;
		AggregationBuilder aggregationBuilder = null;
		if (null != jSONObjectList && jSONObjectList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			sb = buildBoolQuery(jSONObjectList, sortMap, boolQueryBuilder, index, client);
		} else {
			QueryBuilder boolQueryBuilder = QueryBuilders.matchAllQuery();
			sb = client.prepareSearch(index).setQuery(boolQueryBuilder);
		}
		if (null != groupFieldJson && groupFieldJson.size() > 0) {
			// 构建aggregationBuilder
			aggregationBuilder = assemableGroupJson(groupFieldJson, aggregationBuilder);
			aggregationBuilder(aggFieldJson, aggregationBuilder);
		}
		if (null != aggregationBuilder) {
			SearchResponse sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
			Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
			Terms teamAgg = null;
			if (aggMap != null && aggMap.size() > 0) {
				// 这里只需要判断groupFieldJson 如果不为空 则父节点肯定是Terms
				Iterator iterator = groupFieldJson.keys();
				int i = 0;
				while (iterator.hasNext()) {
					if (i == 0) {
						String aggField = (String) iterator.next();
						teamAgg = (Terms) aggMap.get(aggField);
					} else {
						iterator.next();
					}
					i++;
				}
				// 如果teamAgg不为空 则组装teamAgg中的数据
				if (null != teamAgg) {
					return teamAggJsonList(teamAgg, groupFieldJson, aggFieldJson, jsonList);
				}
				// case : 当groupJSon中不存在条件 只有聚合条件时 ,由于没有分组条件时 此方法只能求出一个json条件的聚合
				// ，所以此方法不提供单独聚合的情况
				/*
				 * if (null == teamAgg && null != aggFieldJson &&
				 * aggFieldJson.size() > 0) { return
				 * nullTeamAggJsonList(aggFieldJson, statsAgg, aggMap,
				 * jsonList); }
				 */
			}
		} else {
			// 没有groupby或者聚合函数 根据条件查询普通的数据
			SearchResponse sr = sb.setFrom(0).setSize(10000).execute().actionGet();
			return nullGroupJsonList(sr, jsonList);
		}
		return jsonList;
	}

	private JSONObject aggJsonList(Terms.Bucket entry, JSONObject aggFieldJson, JSONObject js) {
		// 根据查出的结果组装jsonList
		if (null != aggFieldJson && aggFieldJson.size() > 0) {
			Iterator iterator = aggFieldJson.keys();
			while (iterator.hasNext()) {
				String aggField = (String) iterator.next();
				String aggChoose = aggFieldJson.getString(aggField);
				switch (aggChoose) {
				case "MAX":
					InternalMax maxTerm = entry.getAggregations().get(aggField);
					js.put(aggField, String.valueOf(maxTerm.getValue()).equalsIgnoreCase("-Infinity")
							? JSONNull.getInstance() : maxTerm.getValueAsString());
					break;
				case "MIN":
					InternalMin minTerm = entry.getAggregations().get(aggField);
					js.put(aggField, String.valueOf(minTerm.getValue()).equalsIgnoreCase("-Infinity")
							? JSONNull.getInstance() : minTerm.getValueAsString());
					break;
				case "AVG":
					InternalAvg avgTerm = entry.getAggregations().get(aggField);
					js.put(aggField, String.valueOf(avgTerm.getValue()).equalsIgnoreCase("-Infinity")
							? JSONNull.getInstance() : avgTerm.getValueAsString());
					break;
				case "SUM":
					InternalSum sumTerm = entry.getAggregations().get(aggField);
					js.put(aggField, String.valueOf(sumTerm.getValue()).equalsIgnoreCase("-Infinity")
							? JSONNull.getInstance() : sumTerm.getValueAsString());
					break;
				default:
					System.out.println("没有此聚合函数");
					break;
				}
			}
		}
		return js;
	}

	private void aggregationBuilder(JSONObject aggFieldJson, AggregationBuilder aggregationBuilder) {
		if (null != aggFieldJson && aggFieldJson.size() > 0) {
			if (null != aggregationBuilder) {
				Iterator iterator = aggFieldJson.keys();
				while (iterator.hasNext()) {
					String aggField = (String) iterator.next();
					String aggValue = aggFieldJson.getString(aggField);
					switch (aggValue) {
					case "MAX":
						maxAggBuilder(aggregationBuilder, aggField);
						break;
					case "MIN":
						minAggBuilder(aggregationBuilder, aggField);
						break;
					case "AVG":
						avgAggBuilder(aggregationBuilder, aggField);
						break;
					case "SUM":
						sumAggBuilder(aggregationBuilder, aggField);
						break;
					default:
						System.out.println("没有此聚合函数");
						break;
					}
				}
			}
		}
	}

	private AggregationBuilder assemableGroupJson(JSONObject groupFieldJson, AggregationBuilder aggregationBuilder) {
		int i = 0;
		// 根据groupJson组装aggregationBuilder

		Iterator iterator = groupFieldJson.keys();
		while (iterator.hasNext()) {
			if (i == 0) {
				String groupField = (String) iterator.next();
				String value = (String) groupFieldJson.get(groupField);
				if ("asc".equalsIgnoreCase(value)) {
					BucketOrder order = BucketOrder.key(true);
					aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000)
							.order(order);
				} else {
					BucketOrder order = BucketOrder.key(false);
					aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000)
							.order(order);
				}
			} else {
				String groupField = (String) iterator.next();
				String value = (String) groupFieldJson.get(groupField);
				if ("asc".equalsIgnoreCase(value)) {
					BucketOrder order = BucketOrder.key(true);
					AggregationBuilder aggregationBuilder1 = AggregationBuilders.terms(groupField).size(10000)
							.field(groupField).order(order);
					aggregationBuilder.subAggregation(aggregationBuilder1);
				} else {
					BucketOrder order = BucketOrder.key(false);
					AggregationBuilder aggregationBuilder1 = AggregationBuilders.terms(groupField).size(10000)
							.field(groupField).order(order);
					aggregationBuilder.subAggregation(aggregationBuilder1);
				}
			}
			i++;
		}
		return aggregationBuilder;
	}

	/**
	 * 没有任何分组聚合条件
	 * 
	 * @param sr
	 * @param jsonList
	 * @return
	 */
	private JSONArray nullGroupJsonList(SearchResponse sr, JSONArray jsonList) {
		JSONObject countJson = new JSONObject();
		countJson.put("count", sr.getHits().totalHits);
		jsonList.add(countJson);
		JSONObject js;
		String lastOne = null;
		if (sr.getHits().getHits().length > 0) {
			for (SearchHit hit : sr.getHits().getHits()) {
				Long version = hit.getVersion();
				lastOne = hit.getSourceAsString();
				if (null != lastOne) {
					js = JSONObject.fromObject(lastOne);
					js.put("version", version);
					jsonList.add(js);
				}
			}
		}
		return jsonList;
	}

	/**
	 * 不需要分组 ，只要聚合
	 * 
	 * @param aggFieldJson
	 * @param statsAgg
	 * @param aggMap
	 * @param jsonList
	 * @return
	 */
	private JSONArray nullTeamAggJsonList(JSONObject aggFieldJson, InternalStats statsAgg,
			Map<String, Aggregation> aggMap, JSONArray jsonList) {
		Iterator iterator = aggFieldJson.keys();
		JSONObject jsonObject = new JSONObject();
		while (iterator.hasNext()) {
			String aggField = (String) iterator.next();
			String aggChoose = aggFieldJson.getString(aggField);
			switch (aggChoose) {
			case "MAX":
				InternalMax maxValue = (InternalMax) aggMap.get(aggField);
				jsonObject.put(aggField, String.valueOf(maxValue.getValue()).equalsIgnoreCase("-Infinity")
						? JSONNull.getInstance() : maxValue.getValueAsString());
				break;
			case "MIN":
				InternalMin minValue = (InternalMin) aggMap.get(aggField);
				jsonObject.put(aggField, String.valueOf(minValue.getValue()).equalsIgnoreCase("-Infinity")
						? JSONNull.getInstance() : minValue.getValueAsString());
				break;
			case "AVG":
				InternalAvg avgValue = (InternalAvg) aggMap.get(aggField);
				jsonObject.put(aggField, String.valueOf(avgValue.getValue()).equalsIgnoreCase("-Infinity")
						? JSONNull.getInstance() : avgValue.getValueAsString());
				break;
			case "SUM":
				InternalSum sumValue = (InternalSum) aggMap.get(aggField);
				jsonObject.put(aggField, String.valueOf(sumValue.getValue()).equalsIgnoreCase("-Infinity")
						? JSONNull.getInstance() : sumValue.getValueAsString());
				break;
			default:
				System.out.println("没有此聚合函数");
				break;
			}
		}
		jsonList.add(jsonObject);
		return jsonList;
	}

	private JSONArray teamAggJsonList(Terms teamAgg, JSONObject groupFieldJson, JSONObject aggFieldJson,
			JSONArray jsonList) {
		for (Terms.Bucket entry : teamAgg.getBuckets()) {
			JSONObject js = new JSONObject();
			int j = 0;
			if (null != groupFieldJson && groupFieldJson.size() > 0) {
				Iterator iterator = groupFieldJson.keys();
				while (iterator.hasNext()) {
					if (j == 0) {
						String groupField = (String) iterator.next();
						js.put(groupField, entry.getKeyAsString());
					} else {
						String groupField = (String) iterator.next();
						Terms groupTerm = (Terms) entry.getAggregations().get(groupField);
						if (null != groupTerm.getBuckets() && groupTerm.getBuckets().size() > 0) {
							for (Terms.Bucket entry1 : groupTerm.getBuckets()) {
								js.put(groupField, entry1.getKeyAsString());
							}
						} else {
							js.put(groupField, JSONNull.getInstance());
						}

					}
					j++;
				}
			}
			// 组装聚合json结果
			js = aggJsonList(entry, aggFieldJson, js);
			jsonList.add(js);
		}
		return jsonList;
	}

	public void statsAggBuilder(AggregationBuilder aggregationBuilder, String aggField) {
		AggregationBuilder aggregationBuilder1 = AggregationBuilders.stats(aggField).field(aggField);
		aggregationBuilder.subAggregation(aggregationBuilder1);
	}

	public AggregationBuilder countAggBuilder(AggregationBuilder aggregationBuilder, String aggField) {
		AggregationBuilder aggregationBuilder1 = AggregationBuilders.count(aggField).field(aggField);
		AggregationBuilder agg = aggregationBuilder.subAggregation(aggregationBuilder1);
		return agg;

	}

	public AggregationBuilder sumAggBuilder(AggregationBuilder aggregationBuilder, String aggField) {
		AggregationBuilder aggregationBuilder1 = AggregationBuilders.sum(aggField).field(aggField);
		AggregationBuilder agg = aggregationBuilder.subAggregation(aggregationBuilder1);
		return agg;

	}

	public AggregationBuilder avgAggBuilder(AggregationBuilder aggregationBuilder, String aggField) {
		AggregationBuilder aggregationBuilder1 = AggregationBuilders.avg(aggField).field(aggField);
		AggregationBuilder agg = aggregationBuilder.subAggregation(aggregationBuilder1);
		return agg;
	}

	public AggregationBuilder minAggBuilder(AggregationBuilder aggregationBuilder, String aggField) {
		AggregationBuilder aggregationBuilder1 = AggregationBuilders.min(aggField).field(aggField);
		AggregationBuilder agg = aggregationBuilder.subAggregation(aggregationBuilder1);
		return agg;
	}

	public AggregationBuilder maxAggBuilder(AggregationBuilder aggregationBuilder, String aggField) {
		AggregationBuilder aggregationBuilder1 = AggregationBuilders.max(aggField).field(aggField);
		AggregationBuilder agg = aggregationBuilder.subAggregation(aggregationBuilder1);
		return agg;
	}

	private JSONObject aggChoose(SearchRequestBuilder sb, String aggField, Client client, String stats) {
		AggregationBuilder aggregationBuilder = null;
		SearchResponse sr = null;
		JSONObject js = new JSONObject();
		Map<String, Aggregation> aggMap = null;
		switch (stats) {
		case "MAX":
			Long time1 = System.currentTimeMillis();
			aggregationBuilder = AggregationBuilders.max(aggField).field(aggField);
			Long time2 = System.currentTimeMillis();
			System.out.println("查询花费时间：" + (time2 - time1));

			sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();

			aggMap = sr.getAggregations().asMap();

			InternalMax maxValue = (InternalMax) aggMap.get(aggField);
			js.put(aggField, String.valueOf(maxValue.getValue()).equalsIgnoreCase("-Infinity") ? JSONNull.getInstance()
					: maxValue.getValueAsString());
			break;
		case "MIN":
			aggregationBuilder = AggregationBuilders.min(aggField).field(aggField);
			sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
			aggMap = sr.getAggregations().asMap();
			InternalMin minValue = (InternalMin) aggMap.get(aggField);
			js.put(aggField, String.valueOf(minValue.getValue()).equalsIgnoreCase("-Infinity") ? JSONNull.getInstance()
					: minValue.getValueAsString());
			break;
		case "AVG":
			aggregationBuilder = AggregationBuilders.avg(aggField).field(aggField);
			sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
			aggMap = sr.getAggregations().asMap();
			InternalAvg avgValue = (InternalAvg) aggMap.get(aggField);
			js.put(aggField, String.valueOf(avgValue.getValue()).equalsIgnoreCase("-Infinity") ? JSONNull.getInstance()
					: avgValue.getValueAsString());
			break;
		case "SUM":
			aggregationBuilder = AggregationBuilders.sum(aggField).field(aggField);
			sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
			aggMap = sr.getAggregations().asMap();
			InternalSum sumValue = (InternalSum) aggMap.get(aggField);
			js.put(aggField, String.valueOf(sumValue.getValue()).equalsIgnoreCase("-Infinity") ? JSONNull.getInstance()
					: sumValue.getValueAsString());
			break;
		default:
			System.out.println("没有此聚合函数");
			break;
		}

		return js;
	}

	@Override
	public JSONObject aggCondition(JSONArray jSONObjectList, String index, String aggField, String stats) {
		Client client = elasticsearchTemplate.getClient();
		Map<String, String> sortMap = null;
		JSONObject json = new JSONObject();
		SearchRequestBuilder sb = null;
		if (null != jSONObjectList && jSONObjectList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			sb = buildBoolQuery(jSONObjectList, sortMap, boolQueryBuilder, index, client);
		} else {
			sb = client.prepareSearch(index).setQuery(QueryBuilders.matchAllQuery());
		}
		json = aggChoose(sb, aggField, client, stats);
		return json;
	}

	@Override
	public JSONArray groupMin(String index, String groupField, String minField) {
		Client client = elasticsearchTemplate.getClient();
		BucketOrder order = BucketOrder.key(true);
		JSONArray jsonList = new JSONArray();
		AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000)
				.order(order);
		aggregationBuilder = minAggBuilder(aggregationBuilder, minField);
		SearchResponse sr = client.prepareSearch(index).setTypes("doc").addAggregation(aggregationBuilder).setSize(0)
				.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
		Terms teamAgg = (Terms) aggMap.get(groupField);
		if (null != teamAgg) {
			for (Terms.Bucket entry : teamAgg.getBuckets()) {
				JSONObject js = new JSONObject();
				InternalMin minTerm = entry.getAggregations().get(minField);
				js.put(groupField, entry.getKey());
				js.put(minField,
						String.valueOf(minTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : minTerm.getValue());
				jsonList.add(js);
			}
		}
		return jsonList;
	}

	@Override
	public JSONArray groupMinByCondition(JSONArray jSONObjectList, String index, String groupField, String minField) {
		Client client = elasticsearchTemplate.getClient();
		Map<String, String> sortMap = null;
		JSONArray jsonList = new JSONArray();
		if (null != jSONObjectList && jSONObjectList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			SearchRequestBuilder sb = buildBoolQuery(jSONObjectList, sortMap, boolQueryBuilder, index, client);
			AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000);
			aggregationBuilder = minAggBuilder(aggregationBuilder, minField);
			SearchResponse sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
			Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
			Terms teamAgg = (Terms) aggMap.get(groupField);
			if (null != teamAgg) {
				for (Terms.Bucket entry : teamAgg.getBuckets()) {
					JSONObject jsonObject = new JSONObject();
					InternalMin minTerm = entry.getAggregations().get(minField);
					jsonObject.put(groupField, entry.getKey());
					jsonObject.put(minField,
							String.valueOf(minTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : minTerm.getValue());
					jsonList.add(jsonObject);
				}
			}
		}

		return jsonList;
	}

	@Override
	public JSONArray groupAvg(String index, String groupField, String avgField) {
		Client client = elasticsearchTemplate.getClient();
		BucketOrder order = BucketOrder.key(true);
		JSONArray jsonList = new JSONArray();
		AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000)
				.order(order);
		aggregationBuilder = avgAggBuilder(aggregationBuilder, avgField);
		SearchResponse sr = client.prepareSearch(index).setTypes("doc").addAggregation(aggregationBuilder).setSize(0)
				.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
		Terms teamAgg = (Terms) aggMap.get(groupField);
		if (null != teamAgg) {
			for (Terms.Bucket entry : teamAgg.getBuckets()) {
				JSONObject js = new JSONObject();
				InternalAvg avgTerm = entry.getAggregations().get(avgField);
				js.put(groupField, entry.getKey());
				js.put(avgField,
						String.valueOf(avgTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : avgTerm.getValue());
				jsonList.add(js);
			}
		}
		// System.out.println(jsonList);
		return jsonList;
	}

	@Override
	public JSONArray groupAvgByCondition(JSONArray jSONObjectList, String index, String groupField, String avgField) {
		Client client = elasticsearchTemplate.getClient();
		Map<String, String> sortMap = null;
		JSONArray jsonList = new JSONArray();
		if (null != jSONObjectList && jSONObjectList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			SearchRequestBuilder sb = buildBoolQuery(jSONObjectList, sortMap, boolQueryBuilder, index, client);
			AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000);
			aggregationBuilder = avgAggBuilder(aggregationBuilder, avgField);
			SearchResponse sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
			Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
			Terms teamAgg = (Terms) aggMap.get(groupField);
			if (null != teamAgg) {
				for (Terms.Bucket entry : teamAgg.getBuckets()) {
					JSONObject jsonObject = new JSONObject();
					InternalAvg avgTerm = entry.getAggregations().get(avgField);
					jsonObject.put(groupField, entry.getKey());
					jsonObject.put(avgField,
							String.valueOf(avgTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : avgTerm.getValue());
					jsonList.add(jsonObject);
				}
			}
			// System.out.println(jsonList);
		}

		return jsonList;
	}

	@Override
	public JSONArray groupSum(String index, String groupField, String sumField) {
		Client client = elasticsearchTemplate.getClient();

		BucketOrder order = BucketOrder.key(true);
		JSONArray jsonList = new JSONArray();
		AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000)
				.order(order);
		aggregationBuilder = sumAggBuilder(aggregationBuilder, sumField);
		SearchResponse sr = client.prepareSearch(index).setTypes("doc").addAggregation(aggregationBuilder).setSize(0)
				.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
		Terms teamAgg = (Terms) aggMap.get(groupField);
		if (null != teamAgg) {
			for (Terms.Bucket entry : teamAgg.getBuckets()) {
				JSONObject js = new JSONObject();
				InternalSum sumTerm = entry.getAggregations().get(sumField);
				js.put(groupField, entry.getKey());
				js.put(sumField,
						String.valueOf(sumTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : sumTerm.getValue());
				jsonList.add(js);
			}
		}
		return jsonList;
	}

	@Override
	public JSONArray groupSumByCondition(JSONArray jSONObjectList, String index, String groupField, String sumField) {
		Client client = elasticsearchTemplate.getClient();
		Map<String, String> sortMap = null;
		JSONArray jsonList = new JSONArray();
		SearchRequestBuilder sb = null;
		if (null != jSONObjectList && jSONObjectList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			sb = buildBoolQuery(jSONObjectList, sortMap, boolQueryBuilder, index, client);

		} else {
			sb = client.prepareSearch(index).setQuery(QueryBuilders.matchAllQuery());
		}

		AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000);
		aggregationBuilder = sumAggBuilder(aggregationBuilder, sumField);

		/*
		 * JSONObject js = new JSONObject();
		 * js.put(ConditionEnum.COLUMNNAME.toString(), "dyp");
		 * js.put(ConditionEnum.VALUE.toString(), "80,452");
		 * js.put(ConditionEnum.OPERATOR.toString(), "NOT_IN");
		 * 
		 * JSONArray havingList = new JSONArray(); havingList.add(js);
		 * //BucketSelectorPipelineAggregationBuilder bs =
		 * havingAgg(aggregationBuilder, havingList); //
		 * 声明BucketPath，用于后面的bucket筛选 Map<String, String> bucketsPathsMap = new
		 * HashMap<>(); bucketsPathsMap.put(sumField, sumField); // 设置脚本 Script
		 * script = new Script(" 1 == 1 && params." + sumField +
		 * " != 80 && params.dyp != 452 ");
		 * 
		 * // 构建bucket选择器 BucketSelectorPipelineAggregationBuilder bs =
		 * PipelineAggregatorBuilders.bucketSelector("having", bucketsPathsMap,
		 * script);
		 * 
		 * aggregationBuilder.subAggregation(bs);
		 */

		SearchResponse sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
		Terms teamAgg = (Terms) aggMap.get(groupField);
		if (null != teamAgg) {
			for (Terms.Bucket entry : teamAgg.getBuckets()) {
				JSONObject jsonObject = new JSONObject();
				InternalSum sumTerm = entry.getAggregations().get(sumField);
				jsonObject.put(groupField, entry.getKey());
				jsonObject.put(sumField,
						String.valueOf(sumTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : sumTerm.getValue());
				jsonList.add(jsonObject);
			}
		}
		return jsonList;
	}

	@Override
	public JSONArray groupSumHavingCondition(JSONArray jSONObjectList, String index, String groupField, String sumField,
			JSONArray havingList) {
		Map<String, String> sortMap = null;
		JSONArray jsonList = new JSONArray();
		SearchRequestBuilder sb = null;
		Client client = elasticsearchTemplate.getClient();
		if (null != jSONObjectList && jSONObjectList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			sb = buildBoolQuery(jSONObjectList, sortMap, boolQueryBuilder, index, client);

		} else {
			sb = client.prepareSearch(index).setQuery(QueryBuilders.matchAllQuery());
		}

		AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000);
		aggregationBuilder = sumAggBuilder(aggregationBuilder, sumField);
		BucketSelectorPipelineAggregationBuilder bs = havingAgg(aggregationBuilder, havingList);
		aggregationBuilder.subAggregation(bs);
		SearchResponse sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
		Terms teamAgg = (Terms) aggMap.get(groupField);
		if (null != teamAgg) {
			for (Terms.Bucket entry : teamAgg.getBuckets()) {
				JSONObject jsonObject = new JSONObject();
				InternalSum sumTerm = entry.getAggregations().get(sumField);
				jsonObject.put(groupField, entry.getKey());
				jsonObject.put(sumField,
						String.valueOf(sumTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : sumTerm.getValue());
				jsonList.add(jsonObject);
			}
		}
		return jsonList;
	}

	private BucketSelectorPipelineAggregationBuilder havingAgg(AggregationBuilder aggregationBuilder,
			JSONArray havingList) {
		BucketSelectorPipelineAggregationBuilder bs = null;
		if (null != havingList && havingList.size() > 0) {
			StringBuffer havingString = new StringBuffer();
			Map<String, String> bucketsPathsMap = new HashMap<>(1);
			// 给定一个起始条件 类似于 where 1=1效果
			havingString.append("1 == 1");
			for (int i = 0; i < havingList.size(); i++) {
				JSONObject js = havingList.getJSONObject(i);
				Iterator iterator = js.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (ConditionEnum.OPERATOR.toString().equalsIgnoreCase(key)) {
						Object value = js.get(key);
						if (null != value) {
							String operator = value.toString();
							switch (operator) {
							case "EQ":
								eqHaving(js, bucketsPathsMap, havingString);
								break;
							case "IN":
								inHaving(js, bucketsPathsMap, havingString);
								break;
							case "NOT_IN":
								notInHaving(js, bucketsPathsMap, havingString);
								break;
							case "GT":
								gtHaving(js, bucketsPathsMap, havingString);
								break;
							case "GTE":
								gteHaving(js, bucketsPathsMap, havingString);
								break;
							case "LT":
								ltHaving(js, bucketsPathsMap, havingString);
								break;
							case "LTE":
								lteHaving(js, bucketsPathsMap, havingString);
								break;
							case "NE":
								neHaving(js, bucketsPathsMap, havingString);
								break;
							default:
								return null;
							}
						}
					}
				}
			}
			Script script = new Script(havingString.toString());
			bs = PipelineAggregatorBuilders.bucketSelector("having", bucketsPathsMap, script);
		}

		return bs;
	}

	/**
	 * eq时组织having 语句 类似于 having column = value ;
	 * 
	 * @param js
	 * @param bucketsPathsMap
	 * @param stringBuffer
	 */
	private void eqHaving(JSONObject js, Map<String, String> bucketsPathsMap, StringBuffer stringBuffer) {
		String columnName = js.getString(ConditionEnum.COLUMNNAME.toString());
		bucketsPathsMap.put(columnName, columnName);
		stringBuffer.append(" && (params." + columnName + " == " + js.get(ConditionEnum.VALUE.toString()) + ")");
	}

	private void neHaving(JSONObject js, Map<String, String> bucketsPathsMap, StringBuffer stringBuffer) {
		String columnName = js.getString(ConditionEnum.COLUMNNAME.toString());
		bucketsPathsMap.put(columnName, columnName);
		stringBuffer.append(" && (params." + columnName + " != " + js.get(ConditionEnum.VALUE.toString()) + ")");
	}

	private void gtHaving(JSONObject js, Map<String, String> bucketsPathsMap, StringBuffer stringBuffer) {
		String columnName = js.getString(ConditionEnum.COLUMNNAME.toString());
		bucketsPathsMap.put(columnName, columnName);
		stringBuffer.append(" && (params." + columnName + " > " + js.get(ConditionEnum.VALUE.toString()) + ")");
	}

	private void gteHaving(JSONObject js, Map<String, String> bucketsPathsMap, StringBuffer stringBuffer) {
		String columnName = js.getString(ConditionEnum.COLUMNNAME.toString());
		bucketsPathsMap.put(columnName, columnName);
		stringBuffer.append(" && (params." + columnName + " >= " + js.get(ConditionEnum.VALUE.toString()) + ")");
	}

	private void ltHaving(JSONObject js, Map<String, String> bucketsPathsMap, StringBuffer stringBuffer) {
		String columnName = js.getString(ConditionEnum.COLUMNNAME.toString());
		bucketsPathsMap.put(columnName, columnName);
		stringBuffer.append(" && (params." + columnName + " < " + js.get(ConditionEnum.VALUE.toString()) + ")");
	}

	private void lteHaving(JSONObject js, Map<String, String> bucketsPathsMap, StringBuffer stringBuffer) {
		String columnName = js.getString(ConditionEnum.COLUMNNAME.toString());
		bucketsPathsMap.put(columnName, columnName);
		stringBuffer.append(" && (params." + columnName + " <= " + js.get(ConditionEnum.VALUE.toString()) + ")");
	}

	private void notInHaving(JSONObject js, Map<String, String> bucketsPathsMap, StringBuffer stringBuffer) {
		String columnName = js.getString(ConditionEnum.COLUMNNAME.toString());
		String[] valueArray = js.getString(ConditionEnum.VALUE.toString()).split(",");
		System.out.println(js.getString(ConditionEnum.VALUE.toString()));
		bucketsPathsMap.put(columnName, columnName);
		int size = valueArray.length;
		if (size == 1) {
			stringBuffer.append(" && (params." + columnName + " != " + js.get(ConditionEnum.VALUE.toString()) + ")");
		}
		for (int i = 0; i < size; i++) {
			String value = valueArray[i];
			if (i == 0) {
				stringBuffer.append(" && (params." + columnName + " != " + value);
			} else if (i != size - 1) {
				stringBuffer.append(" && params." + columnName + " != " + value);
			} else if (i == size - 1) {
				stringBuffer.append(" && params." + columnName + " != " + value + ")");
			}
		}
	}

	private void inHaving(JSONObject js, Map<String, String> bucketsPathsMap, StringBuffer stringBuffer) {
		String columnName = js.getString(ConditionEnum.COLUMNNAME.toString());
		String[] valueArray = js.getString(ConditionEnum.VALUE.toString()).split(",");
		System.out.println(js.getString(ConditionEnum.VALUE.toString()));
		bucketsPathsMap.put(columnName, columnName);
		int size = valueArray.length;
		if (size == 1) {
			stringBuffer.append(" && (params." + columnName + " == " + js.get(ConditionEnum.VALUE.toString()) + ")");
		}
		for (int i = 0; i < size; i++) {
			String value = valueArray[i];
			if (i == 0) {
				stringBuffer.append(" && (params." + columnName + " == " + value);
			} else if (i != size - 1) {
				stringBuffer.append(" || params." + columnName + " == " + value);
			} else if (i == size - 1) {
				stringBuffer.append(" || params." + columnName + " == " + value + ")");
			}
		}
	}

	@Override
	public JSONArray groupCount(String index, String groupField, String countField) {
		Client client = elasticsearchTemplate.getClient();
		BucketOrder order = BucketOrder.key(true);
		JSONArray jsonList = new JSONArray();
		AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000)
				.order(order);
		aggregationBuilder = countAggBuilder(aggregationBuilder, countField);
		SearchResponse sr = client.prepareSearch(index).setTypes("doc").addAggregation(aggregationBuilder).setSize(0)
				.execute().actionGet();
		Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
		Terms teamAgg = (Terms) aggMap.get(groupField);
		if (null != teamAgg) {
			for (Terms.Bucket entry : teamAgg.getBuckets()) {
				JSONObject js = new JSONObject();
				InternalValueCount countTerm = entry.getAggregations().get(countField);
				js.put(groupField, entry.getKey());
				js.put(countField,
						String.valueOf(countTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0 : countTerm.getValue());
				jsonList.add(js);
			}
		}
		// System.out.println(jsonList);
		return jsonList;
	}

	@Override
	public JSONArray groupCountByCondition(JSONArray jSONObjectList, String index, String groupField,
			String countField) {
		Client client = elasticsearchTemplate.getClient();
		Map<String, String> sortMap = null;
		JSONArray jsonList = new JSONArray();
		if (null != jSONObjectList && jSONObjectList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			SearchRequestBuilder sb = buildBoolQuery(jSONObjectList, sortMap, boolQueryBuilder, index, client);
			AggregationBuilder aggregationBuilder = AggregationBuilders.terms(groupField).field(groupField).size(10000);
			aggregationBuilder = countAggBuilder(aggregationBuilder, countField);
			SearchResponse sr = sb.addAggregation(aggregationBuilder).setFrom(0).setSize(10000).execute().actionGet();
			Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
			Terms teamAgg = (Terms) aggMap.get(groupField);
			if (null != teamAgg) {
				for (Terms.Bucket entry : teamAgg.getBuckets()) {
					JSONObject jsonObject = new JSONObject();
					InternalValueCount countTerm = entry.getAggregations().get(countField);
					jsonObject.put(groupField, entry.getKey());
					jsonObject.put(countField, String.valueOf(countTerm.getValue()).equalsIgnoreCase("-Infinity") ? 0
							: countTerm.getValue());
					jsonList.add(jsonObject);
				}
			}
			// System.out.println(jsonList);
		}

		return jsonList;
	}

	@Override
	public JSONArray findByField(String index, JSONArray jsList) {
		Client client = elasticsearchTemplate.getClient();
		JSONArray jsonList = null;
		if (null != jsList && jsList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			Map<String, String> sortMap = new LinkedHashMap<String, String>();
			SearchRequestBuilder sb = buildBoolQuery(jsList, sortMap, boolQueryBuilder, index, client);
			SearchResponse response = sb.setFrom(0).setSize(10000).setVersion(true).execute().actionGet();
			jsonList = new JSONArray();
			JSONObject countJson = new JSONObject();
			countJson.put("count", response.getHits().totalHits);
			jsonList.add(countJson);
			JSONObject js;
			String lastOne = null;
			if (response.getHits().getHits().length > 0) {
				for (SearchHit hit : response.getHits().getHits()) {
					Long version = hit.getVersion();
					lastOne = hit.getSourceAsString();
					// System.out.println(lastOne + "=====================");
					if (null != lastOne) {
						js = JSONObject.fromObject(lastOne);
						Iterator it = js.keys();
						while (it.hasNext()) {
							String keys = (String) it.next();
							Object value = js.get(keys);
							if (value == null || String.valueOf(value).equalsIgnoreCase("null")) {
								js.put(keys, "");
							} else {
								js.put(keys, value);
							}
						}
						js.put("version", version);
						jsonList.add(js);
					}
				}
			}
		} else {
			jsonList = findAll(index);
		}
		return jsonList;
	}

	@Override
	public JSONArray findAll(String index) {
		Client client = elasticsearchTemplate.getClient();
		SearchResponse response = null;
		// 如果不传任何条件,默认按照_id查询10000个
		QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
		JSONArray jsonList = new JSONArray();
		JSONObject js = null;
		JSONObject countJs = new JSONObject();
		// 默认按照_id排序
		response = client.prepareSearch(index).setFrom(0).setSize(10000).setQuery(queryBuilder).execute().actionGet();
		String lastOne = null;
		// 取出总数
		countJs.put("count", response.getHits().totalHits);
		jsonList.add(countJs);
		if (response.getHits().getHits().length > 0) {
			for (SearchHit hit : response.getHits().getHits()) {
				lastOne = hit.getSourceAsString();
				if (null != lastOne) {
					js = JSONObject.fromObject(lastOne);
					jsonList.add(js);
				}
			}
		}
		return jsonList;
	}

	@Override
	public JSONArray findIdByField(String index, JSONArray jsList) {
		Client client = elasticsearchTemplate.getClient();
		JSONArray jsonList = null;
		if (null != jsList && jsList.size() > 0) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			Map<String, String> sortMap = new LinkedHashMap<String, String>();
			SearchRequestBuilder sb = buildBoolQuery(jsList, sortMap, boolQueryBuilder, index, client);
			SearchResponse response = sb.setFrom(0).setSize(10000).setVersion(true).execute().actionGet();
			jsonList = new JSONArray();
			JSONObject countJson = new JSONObject();
			countJson.put("count", response.getHits().totalHits);
			jsonList.add(countJson);
			JSONObject js;
			String lastOne = null;
			if (response.getHits().getHits().length > 0) {
				for (SearchHit hit : response.getHits().getHits()) {
					lastOne = hit.getSourceAsString();
					if (null != lastOne) {
						js = JSONObject.fromObject(lastOne);
						js.put("_id", hit.getId());
						js.put("version", hit.getVersion());
						jsonList.add(js);
					}
				}
			}
		}
		return jsonList;
	}

	@Override
	public Map groupConditionPage(Integer currentPage, Integer pageSize, JSONArray jsList, Integer willPage,
			String index, JSONObject groupFieldJson, JSONObject aggFieldJson) {
		// Long time1 = System.currentTimeMillis();
		// 先走分组逻辑
		JSONArray jsonList = this.groupConditions(jsList, index, groupFieldJson, aggFieldJson);
		Map map = null;
		// Long time2 = System.currentTimeMillis();
		/*
		 * System.out.println("分组耗时：" + (time2 - time1));
		 * System.out.println(jsonList);
		 */
		if (null != jsonList && jsonList.size() > 0) {
			Client client = elasticsearchTemplate.getClient();
			map = this.builder(jsonList, index, client, currentPage, pageSize, willPage);
			// System.out.println(boolQueryBuilder.toString());
			/*
			 * Long time4 = System.currentTimeMillis();
			 * System.out.println("组合条件耗时：" + (time4 - time2));
			 */
			// System.out.println(map.toString());
			// JSONArray list = JSONArray.fromObject(map);
			// System.out.println(list);
			/*
			 * Long time5 = System.currentTimeMillis();
			 * System.out.println("map 转换成jsonArray时间：" + (time5 - time4));
			 */
		}
		return map;
	}

	@Override
	public JSONArray mGetSearch(String index, String idList) {
		JSONArray jsonList = new JSONArray();
		MultiGetResponse multiGetItemResponses = elasticsearchTemplate.getClient().prepareMultiGet()
				.add(index, "doc", idList).execute().actionGet();
		for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
			GetResponse response = itemResponse.getResponse();
			if (response.isExists()) {
				String json = response.getSourceAsString();
				JSONObject js = JSONObject.fromObject(json);
				jsonList.add(js);
			}
		}
		return jsonList;
	}

	private JSONArray searchResonpse(String index, Integer start, Integer pageSize, BoolQueryBuilder boolQueryBuilder,
			Client client) {
		JSONArray returnList = new JSONArray();
		SearchRequestBuilder sb = client.prepareSearch(index).setFrom(start).setSize(pageSize)
				.setPostFilter(boolQueryBuilder);
		SearchResponse response = sb.setVersion(true).execute().actionGet();
		JSONObject js;
		JSONObject countJson = new JSONObject();
		countJson.put("count", response.getHits().totalHits);
		returnList.add(countJson);
		String lastOne = null;
		if (response.getHits().getHits().length > 0) {
			for (SearchHit hit : response.getHits().getHits()) {
				lastOne = hit.getSourceAsString();
				if (null != lastOne) {
					js = JSONObject.fromObject(lastOne);
					/*
					 * Iterator it = js.keys(); while (it.hasNext()) { String
					 * keys = (String) it.next(); Object value = js.get(keys);
					 * if (value == null ||
					 * String.valueOf(value).equalsIgnoreCase("null")) {
					 * js.put(keys, JSONNull.getInstance()); } else {
					 * js.put(keys, value); } }
					 */
					returnList.add(js);
				}
			}
		}
		return returnList;
	}

	/**
	 * 分组后查询所有结果构建builder方法 ,查询效率太低 -----修改为多线程处理
	 * 
	 * @param jsonList
	 * @param index
	 * @param client
	 * @param currentPage
	 * @param pageSize
	 * @param willPage
	 * @return
	 */
	private Map builder(JSONArray jsonList, String index, Client client, Integer currentPage, Integer pageSize,
			Integer willPage) {
		BoolQueryBuilder subBuilder = null;
		// 条件查询不得大于1024个 所以此处需要分批次构建
		int countSize = jsonList.size();
		// 根据分页条件拆分list
		List<JSONObject> jsList = null;

		Integer fromIndex = (willPage - 1) * pageSize;
		Integer toIndex = willPage * pageSize;
		if (fromIndex > countSize) {
			// 起始页超过总条数 ，无任何记录
			jsList = new JSONArray();
		} else {
			if (fromIndex <= countSize && countSize < toIndex) {
				toIndex = countSize;
			}
			jsList = jsonList.subList(fromIndex, toIndex);
		}

		int size = jsList.size();
		// 根据size大小创建线程
		int executorSize = size / 1000;
		Map map = new ConcurrentHashMap(10);
		if (executorSize == 0) {
			// 测站数量不超过1000，无需开启多线程
			for (int i = 0; i < jsList.size(); i++) {
				JSONObject js = jsList.get(i);
				// 开始构建querybuilder
				if (i == 0 && i != jsList.size() - 1) {
					// 创建querybuilder
					subBuilder = QueryBuilders.boolQuery();
					setBuild(js, subBuilder);
				}
				if (i != 0 && i < jsList.size() - 1) {
					// 当jsList的大小处于中间时，只拼接条件
					setBuild(js, subBuilder);
				}
				if (i == jsList.size() - 1 && i != 0) {
					// 当最后一条时 ,查询结果
					setBuild(js, subBuilder);
					JSONArray jy = this.searchResonpse(index, 0, size, subBuilder, client);
					// 将线程返回结果 放置在map中
					map.put(String.valueOf(i), jy);
				}
				if (i == 0 && i == jsList.size() - 1) {
					subBuilder = QueryBuilders.boolQuery();
					setBuild(js, subBuilder);
					JSONArray jy = this.searchResonpse(index, 0, size, subBuilder, client);
					map.put(String.valueOf(i), jy);
				}
			}
		} else {
			// size 大于1000 ， 需要开启多个线程 ,
			SummitThreadFactory sf = new SummitThreadFactory("builderThread");
			ThreadPoolExecutor executor = new ThreadPoolExecutor(executorSize + 1, 12, 5, TimeUnit.SECONDS,
					new LinkedBlockingDeque<Runnable>(), sf);

			for (int i = 0; i < size; i++) {
				JSONObject js = jsList.get(i);
				if (i == 0 && i != jsList.size() - 1) {
					subBuilder = QueryBuilders.boolQuery();
					setBuild(js, subBuilder);
				}
				if (i % 1000 == 0 && i != 0 && i < size) {
					// 1000个开启一个线程
					EsThread esThread = new EsThread(index, client, subBuilder, map, i);
					executor.execute(esThread);
					subBuilder = QueryBuilders.boolQuery();
					setBuild(js, subBuilder);
				}
				if (i > 0 && i % 1000 != 0 && i < jsList.size() - 1) {
					setBuild(js, subBuilder);
				}
				if (i == jsList.size() - 1 && i != 0) {
					setBuild(js, subBuilder);
					EsThread esThread = new EsThread(index, client, subBuilder, map, i);
					executor.execute(esThread);
				}
				if (i == 0 && i == jsList.size() - 1) {
					subBuilder = QueryBuilders.boolQuery();
					setBuild(js, subBuilder);
					EsThread esThread = new EsThread(index, client, subBuilder, map, i);
					executor.execute(esThread);
				}
			}
			executor.shutdown();
			while (true) {
				if (executor.isTerminated()) {
					System.out.println("任务结束,结束时间:" + new Date());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	@Override
	public void test() {

		Long time1 = System.currentTimeMillis();
		// String sql = "SELECT stcd ,max(tm),min(tm),avg(tm),sum(tm) from
		// st_pptn_r group by stcd ";

		String sql = "SELECT u.username as username, r.roleCode as roleCode  from user u join user_role r on u.username = r.username ";
		Connection conn = ConnectionUtil.getConnection(url);
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				System.out.println(rs.getObject(0) + ":" + rs.getObject(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Long time2 = System.currentTimeMillis();
		System.out.println(time2 - time1 + " : 毫秒");
	}

	@Override
	public JSONArray queryBySql(String sql) {
		Connection conn = ConnectionUtil.getConnection(url);
		PreparedStatement ps;
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray();
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();
			while (rs.next()) {
				JSONObject js = new JSONObject();
				for (int i = 1; i <= columnNum; i++) {
					js.put(rsmd.getColumnName(i),
							null == rs.getObject(i - 1) ? JSONNull.getInstance() : rs.getObject(i - 1));
				}
				jsonArray.add(js);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonArray;
	}

	private void setBuild(JSONObject js, BoolQueryBuilder subBuilder) {

		Iterator iterator = js.keys();
		BoolQueryBuilder mustQb = QueryBuilders.boolQuery();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Object value = js.get(key);
			if (null != value && !(value.toString().equalsIgnoreCase("null"))) {
				QueryBuilder queryBuilder = QueryBuilders.matchQuery(key, value);
				mustQb.must(queryBuilder);
			}
		}
		subBuilder.should(mustQb);

	}
}
