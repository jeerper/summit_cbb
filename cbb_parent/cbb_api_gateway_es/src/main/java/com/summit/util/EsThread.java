package com.summit.util;

import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author yt
 */
public class EsThread implements Runnable {

    private String index;
    private Client client;
    private BoolQueryBuilder boolQueryBuilder;
    private Map map;
    private Integer stats;


    public EsThread() {
        super();
    }


    public EsThread(String index, Client client, BoolQueryBuilder boolQueryBuilder, Map map, Integer stats) {
        super();
        this.index = index;
        this.client = client;
        this.boolQueryBuilder = boolQueryBuilder;
        this.map = map;
        this.stats = stats;
    }


    @Override
    public void run() {
        Long time1 = System.currentTimeMillis();
        System.out.println("线程编号： " + this.toString() + "==============");

        JSONArray returnList = new JSONArray();
        SearchRequestBuilder sb = client.prepareSearch(index).setFrom(0).setSize(10000)
                .setPostFilter(boolQueryBuilder);
        SearchResponse response = sb.setVersion(true).execute().actionGet();
        Long time2 = System.currentTimeMillis() - time1;
        System.out.println("==========thread===============" + time2 / 1000f + "秒");
        JSONObject js;
        String lastOne = null;
        if (response.getHits().getHits().length > 0) {
            for (SearchHit hit : response.getHits().getHits()) {
                lastOne = hit.getSourceAsString();
                js = new JSONObject();
                if (null != lastOne) {
                    String[] lastOneArr = lastOne.split(",");
                    for (int i = 0; i < lastOneArr.length; i++) {
                        String abc = lastOneArr[i];
                        if (i == 0) {
                            abc = abc.substring(1, abc.length());
                        } else if (i == lastOneArr.length - 1) {
                            abc = abc.substring(0, abc.length() - 1);
                        }
                        String abcd = abc.split(":")[0];
                        String abcde = abc.split(":")[1];
                        js.put(abcd.replace("\"", ""), abcde.replace("\"", ""));
                    }
                    returnList.add(js);
                }
            }
        }
        map.put(String.valueOf(stats), returnList);

    }
}
