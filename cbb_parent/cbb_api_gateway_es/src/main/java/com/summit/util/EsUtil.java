package com.summit.util;

import java.io.IOException;
import java.util.Iterator;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
/**
 * 
 * @author yt
 *
 */
@Component
public class EsUtil {
	
	
	
	public void createIndexAndMapping(String index, JSONObject jsonObject, Client client) {
		try {
			XContentBuilder mapBuilder = XContentFactory.jsonBuilder();
			PutMappingResponse response = null;
			if (null != index) {
				CreateIndexRequest request = new CreateIndexRequest(index);
				client.admin().indices().create(request).actionGet();
			}
			if (null != jsonObject && jsonObject.size() > 0) {
				Iterator iterator = jsonObject.keys();
				int i = 0;
				while (iterator.hasNext()) {
					String keys = (String) iterator.next();
					String value = jsonObject.getString(keys);
					if (i == 0) {
						switch (value) {
						case "string":
							mapBuilder.startObject().startObject("type").field("fs").startObject("properties").startObject(keys)
									.field("type", "keyword").endObject();
							break;
						case "date":
							mapBuilder.startObject().startObject("doc").startObject("properties").startObject(keys)
									.field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS").endObject();
							break;
						case "integer":
							mapBuilder.startObject().startObject("doc").startObject("properties").startObject(keys)
									.field("type", "integer").endObject();
							break;
						case "long":
							mapBuilder.startObject().startObject("doc").startObject("properties").startObject(keys)
									.field("type", "long").endObject();
							break;
						case "double":
							mapBuilder.startObject().startObject("doc").startObject("properties").startObject(keys)
									.field("type", "double").endObject();
							break;
						default:
							mapBuilder.startObject().startObject("doc").startObject("properties").startObject(keys)
									.field("type", "keyword").endObject();
							break;
						}
					} else {
						switch (value) {
						case "string":
							mapBuilder.startObject(keys).field("type", "keyword").endObject();
							break;
						case "date":
							mapBuilder.startObject(keys).field("type", "date")
									.field("format", "yyyy-MM-dd HH:mm:ss.SSS").endObject();
							break;
						case "integer":
							mapBuilder.startObject(keys).field("type", "integer").endObject();
							break;
						case "long":
							mapBuilder.startObject(keys).field("type", "long").endObject();
							break;
						case "double":
							mapBuilder.startObject(keys).field("type", "double").endObject();
							break;
						default:
							mapBuilder.startObject(keys).field("type", "keyword").endObject();
							break;
						}
					}
					i++;
				}
				mapBuilder.endObject().endObject().endObject();
				PutMappingRequest putMappingRequest = Requests.putMappingRequest(index).type("doc").source(mapBuilder);
				Settings settings = Settings.builder().put("refresh_interval", "-1").build();
				UpdateSettingsRequest request = Requests.updateSettingsRequest(index).settings(settings);
				response = client.admin().indices().putMapping(putMappingRequest).actionGet();
				client.admin().indices().updateSettings(request).actionGet();
			}	
			if(null != response){
				response.toString();
			}
			
		} catch (IOException e) {
			System.out.println("创建索引失败");
		}
	}
	
	
	public void deleteIndex(String index , Client client){
		if (null != index) {
			DeleteIndexRequest request = new DeleteIndexRequest(index);
			client.admin().indices().delete(request).actionGet();
		}
	}
	
	
	/**
	 *  定义索引的映射类型
	 * @param client
	 */
	private void defineIndexTypeMapping(Client client) {
		try {
			XContentBuilder mapBuilder = XContentFactory.jsonBuilder();
			mapBuilder.startObject().startObject("doc")
					.startObject("properties")
					.startObject("stcd").field("type", "text").endObject().startObject("tm").field("type", "date")
					.field("format", "yyyy-MM-dd HH:mm:ss.SSS").endObject()
					.startObject("drp").field("type", "double").endObject().startObject("dyp").field("type", "double")
					.endObject()
					.endObject().endObject().endObject();
			PutMappingRequest putMappingRequest = Requests.putMappingRequest("pptn2").type("doc").source(mapBuilder);
			client.admin().indices().putMapping(putMappingRequest).actionGet();
		} catch (IOException e) {
			System.out.println("创建索引失败");
		}
	}
}