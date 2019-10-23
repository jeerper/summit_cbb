package com.summit.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.summit.dao.ExcuteDao;
import com.summit.exception.EsException;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * @author yt
 */
@Component
public class ExcuteDaoImpl implements ExcuteDao {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    int limit = 100000;

    @Override
    public JSONArray saveOrUpdateBatchTransfor(String index, JsonArray batchList) throws EsException {
        Client client = elasticsearchTemplate.getClient();
        BulkRequestBuilder bulkBuilder = null;
        BulkResponse bulkResponse = null;
        JSONArray jsonArray = null;
        try {


            if (null != batchList && batchList.size() > 0) {
                int size = batchList.size();
                if (limit < size) {
                    try {
                        throw new Exception("Json size is too big  , the size should be less than " + 100000);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    bulkBuilder = client.prepareBulk();
                    for (int i = 0; i < batchList.size(); i++) {
                        JsonObject j = batchList.get(i).getAsJsonObject();
                        JSONObject js = JSONObject.fromObject(j.toString());
                        IndexRequestBuilder in = null;
                        if (js.containsKey("_id")) {
                            String id = js.getString("_id");
                            Long version = js.getLong("version");
                            js.remove("_id");
                            js.remove("version");
                            in = client.prepareIndex(index, "doc").setId(id).setVersion(version).setSource(js.toString(),
                                    XContentType.JSON);
                        } else {
                            in = client.prepareIndex(index, "doc").setSource(js.toString(), XContentType.JSON);
                        }
                        bulkBuilder.add(in);
                    }
                    if (bulkBuilder.numberOfActions() > 0) {
                        bulkResponse = bulkBuilder.execute().actionGet();
                        refreshIndex(index);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != bulkResponse) {
            jsonArray = new JSONArray();
            Iterator<BulkItemResponse> it = bulkResponse.iterator();
            while (it.hasNext()) {
                BulkItemResponse be = it.next();
                JSONObject js = new JSONObject();
                if (null != be.getResponse()) {
                    js.put("index", be.getIndex());
                    js.put("_id", be.getId());
                    js.put("version", be.getVersion());
                    js.put("type", be.getType());
                    js.put("result", be.getResponse().getResult());
                    jsonArray.add(js);
                } else {
                    jsonArray.add(JSONNull.getInstance());
                }
            }
        }
        return jsonArray;
    }

    @Override
    public void refreshIndex(String index) throws EsException {
        // TODO Auto-generated method stub
        Client client = elasticsearchTemplate.getClient();
        client.admin().indices().prepareRefresh(index).execute().actionGet();
    }

    @Override
    public JSONArray delete(String index, List<String> idList) throws EsException {
        Client client = elasticsearchTemplate.getClient();
        JSONArray jsonArray = null;
        BulkRequestBuilder bulkBuilder = client.prepareBulk();
        BulkResponse bulkResponse = null;
        if (null != idList && idList.size() > 0) {
            int size = idList.size();
            if (limit < size) {
                try {
                    throw new Exception("Json size is too big  , the size should be less than " + 100000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                for (String id : idList) {
                    bulkBuilder.add(client.prepareDelete(index, "doc", id));
                }
            }
        }
        if (bulkBuilder.numberOfActions() > 0) {
            bulkResponse = bulkBuilder.execute().actionGet();
            refreshIndex(index);
        }
        if (null != bulkResponse) {
            jsonArray = new JSONArray();
            Iterator<BulkItemResponse> it = bulkResponse.iterator();
            while (it.hasNext()) {
                BulkItemResponse be = it.next();
                JSONObject js = new JSONObject();
                if (null != be.getResponse()) {
                    js.put("index", be.getIndex());
                    js.put("_id", be.getId());
                    js.put("version", be.getVersion());
                    js.put("type", be.getType());
                    js.put("result", be.getResponse().getResult());
                    jsonArray.add(js);
                } else {
                    jsonArray.add(JSONNull.getInstance());
                }
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray saveCustomId(String index, JsonArray batchList) throws EsException {
        Client client = elasticsearchTemplate.getClient();
        BulkRequestBuilder bulkBuilder = null;
        BulkResponse bulkResponse = null;
        JSONArray jsonArray = null;
        if (null != batchList && batchList.size() > 0) {
            int size = batchList.size();
            if (limit < size) {
                try {
                    throw new Exception("Json size is too big  , the size should be less than " + 100000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                bulkBuilder = client.prepareBulk();
                for (int i = 0; i < batchList.size(); i++) {
                    JsonObject j = batchList.get(i).getAsJsonObject();
                    JSONObject js = JSONObject.fromObject(j.toString());
                    IndexRequestBuilder in = null;
                    if (js.containsKey("_id")) {
                        String id = js.getString("_id");
                        js.remove("_id");
                        in = client.prepareIndex(index, "doc", id).setSource(js.toString(), XContentType.JSON);
                    } else {
                        in = client.prepareIndex(index, "doc").setSource(js.toString(), XContentType.JSON);
                    }
                    bulkBuilder.add(in);
                }
                if (bulkBuilder.numberOfActions() > 0) {
                    bulkResponse = bulkBuilder.execute().actionGet();
                    refreshIndex(index);
                }
            }
        }
        if (null != bulkResponse) {
            jsonArray = new JSONArray();
            Iterator<BulkItemResponse> it = bulkResponse.iterator();
            while (it.hasNext()) {
                BulkItemResponse be = it.next();
                JSONObject js = new JSONObject();
                if (null != be.getResponse()) {
                    js.put("index", be.getIndex());
                    js.put("_id", be.getId());
                    js.put("version", be.getVersion());
                    js.put("type", be.getType());
                    js.put("result", be.getResponse().getResult());
                    jsonArray.add(js);
                } else {
                    jsonArray.add(JSONNull.getInstance());
                }
            }
        }
        return jsonArray;
    }

    @Override
    public JSONObject saveOrUpdate(String index, JSONObject jsonObject) throws EsException {
        Client client = elasticsearchTemplate.getClient();
        BulkRequestBuilder bulkBuilder = null;
        BulkResponse bulkResponse = null;
        JSONObject js = null;
        if (null != jsonObject && jsonObject.size() > 0) {
            bulkBuilder = client.prepareBulk();
            IndexRequestBuilder in = null;
            String eId = "_id";
            if (jsonObject.containsKey(eId)) {
                String id = jsonObject.getString(eId);
                Long version = jsonObject.getLong("version");
                jsonObject.remove(eId);
                jsonObject.remove("version");
                in = client.prepareIndex(index, "doc").setId(id).setVersion(version).setSource(jsonObject.toString(),
                        XContentType.JSON);
            } else {
                in = client.prepareIndex(index, "doc").setSource(jsonObject.toString(), XContentType.JSON);
            }
            bulkBuilder.add(in);
            if (bulkBuilder.numberOfActions() > 0) {
                bulkResponse = bulkBuilder.execute().actionGet();
                refreshIndex(index);
            }
            if (null != bulkResponse) {
                Iterator<BulkItemResponse> it = bulkResponse.iterator();
                while (it.hasNext()) {
                    BulkItemResponse be = it.next();
                    js = new JSONObject();
                    if (null != be.getResponse()) {
                        js.put("index", be.getIndex());
                        js.put("_id", be.getId());
                        js.put("version", be.getVersion());
                        js.put("type", be.getType());
                        js.put("result", be.getResponse().getResult());
                    }
                }
            }
        }
        return js;
    }

    @Override
    public JSONArray saveOrUpdateBatch(String index, JSONArray jsonList) throws EsException {
        Client client = elasticsearchTemplate.getClient();
        BulkRequestBuilder bulkBuilder = null;
        BulkResponse bulkResponse = null;
        JSONArray jsonArray = null;
        if (null != jsonList && jsonList.size() > 0) {
            int size = jsonList.size();
            if (limit < size) {
                try {
                    throw new Exception("Json size is too big  , the size should be less than " + 100000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                bulkBuilder = client.prepareBulk();
                for (int i = 0; i < jsonList.size(); i++) {
                    JSONObject js = jsonList.getJSONObject(i);
                    IndexRequestBuilder in = null;
                    if (!js.containsKey("count")) {
                        if (js.containsKey("_id")) {
                            String id = js.getString("_id");
                            Long version = js.getLong("version");
                            js.remove("_id");
                            js.remove("version");
                            in = client.prepareIndex(index, "doc").setId(id).setVersion(version).setSource(js.toString(),
                                    XContentType.JSON);
                        } else {
                            in = client.prepareIndex(index, "doc").setSource(js.toString(), XContentType.JSON);
                        }

                        bulkBuilder.add(in);
                    }

                }
                if (bulkBuilder.numberOfActions() > 0) {
                    bulkResponse = bulkBuilder.execute().actionGet();
                    refreshIndex(index);
                }
            }
        }
        if (null != bulkResponse) {
            jsonArray = new JSONArray();
            Iterator<BulkItemResponse> it = bulkResponse.iterator();
            while (it.hasNext()) {
                BulkItemResponse be = it.next();
                JSONObject js = new JSONObject();
                if (null != be.getResponse()) {
                    js.put("index", be.getIndex());
                    js.put("_id", be.getId());
                    js.put("version", be.getVersion());
                    js.put("type", be.getType());
                    js.put("result", be.getResponse().getResult());
                    jsonArray.add(js);
                } else {
                    jsonArray.add(JSONNull.getInstance());
                }
            }
        }
        return jsonArray;
    }
}
