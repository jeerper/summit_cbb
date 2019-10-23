package com.summit.service.impl;

import com.summit.dao.ExcuteDao;
import com.summit.dao.QueryDao;
import com.summit.service.BaseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author yt
 */

public class BaseServiceImpl implements BaseService {
    @Autowired
    QueryDao queryDao;
    @Autowired
    ExcuteDao excuteDao;

    @Override
    public JSONObject add(String index, JSONObject jsonObject, JSONArray jsList) throws Exception {
        JSONObject jsObject = null;
        if (null != jsList && jsList.size() > 0) {
            JSONArray jsonArray = queryDao.findByField(index, jsList);
            if (null != jsonArray && jsonArray.size() > 1) {
                System.out.println("存在对象" + jsonArray.toString());
            } else {
                jsObject = excuteDao.saveOrUpdate(index, jsonObject);
                System.out.println("插入成功" + jsObject.toString());
            }
        }
        return jsObject;
    }

    @Override
    public JSONArray edit(String index, JSONObject jsonObject, JSONArray jsList) throws Exception {
        JSONArray returnList = null;
        JSONArray editList = new JSONArray();
        if (null != jsonObject && jsonObject.size() > 0) {
            // 根据条件查询 可得到一个或者多个对象
            JSONArray jsonArray = queryDao.findIdByField(index, jsList);
            if (null != jsonArray && jsonArray.size() > 1) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (i != 0) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        Iterator iterator = jsonObject.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            Object value = jsonObject.get(key);
                            if (js.containsKey(key)) {
                                js.put(key, value);
                            }
                        }
                        editList.add(js);
                    }
                }
                returnList = excuteDao.saveOrUpdateBatch(index, editList);
                System.out.println("更新成功" + returnList.toString());
            }
        }
        return returnList;
    }

    @Override
    public JSONArray query(String index, JSONArray jsonList) {
        JSONArray jsonArray = null;
        if (jsonList != null && jsonList.size() > 0) {
            jsonArray = queryDao.findByField(index, jsonList);
        }
        return jsonArray;
    }

    ;

    @Override
    public JSONArray pageQuery(String index, JSONArray jsonList, Integer currentPage, Integer pageSize,
                               Integer willPage) {
        JSONArray jsonArray = null;
        if (jsonList != null && jsonList.size() > 0) {
            jsonArray = queryDao.pageSearch(currentPage, pageSize, jsonList, willPage, index);
        }
        return jsonArray;
    }

    ;

    @Override
    public JSONArray queryAll(String index) {
        JSONArray jsonArray = queryDao.findAll(index);
        return jsonArray;
    }

    @Override
    public JSONArray delete(String index, JSONArray jsonList) throws Exception {
        List<String> idList = null;
        JSONArray jsArray = null;
        if (jsonList != null && jsonList.size() > 0) {
            JSONArray jsonArray = queryDao.findIdByField(index, jsonList);
            if (jsonArray != null && jsonArray.size() > 0) {
                idList = new ArrayList<String>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (i != 0) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        String id = js.getString("_id");
                        if (id != null) {
                            idList.add(id);
                        }
                    }

                }
            }
            if (idList != null && idList.size() > 0) {
                jsArray = excuteDao.delete(index, idList);
            }
        }
        return jsArray;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONArray saveBatch(String index, JSONArray jsonList) throws Exception {

        return excuteDao.saveOrUpdateBatch(index, jsonList);
    }
}
