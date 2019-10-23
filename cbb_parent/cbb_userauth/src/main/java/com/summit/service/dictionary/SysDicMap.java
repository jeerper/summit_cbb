/*
 * (c) Copyright 2005-2013 summit,http://www.summit.com.cn
 *
 * @Name: 数据字典 code-实体 映射类
 * @Author: liug
 * @Time: 2013-06-07 12:00:00
 *
 */
package com.summit.service.dictionary;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.summit.common.entity.DictionaryBean;

/**
 * 数据字典的内存缓存类如果仅仅是查询，建议调用本类中的getByCode和getChildList方法，直接从内存中取数据加速系统运行。
 * ClassName: SysDicMap
 */
public class SysDicMap {
    /**
     * code和bean映射集合
     */
    private static Map<String, DictionaryBean> sdMap_code_bean = new ConcurrentHashMap<String, DictionaryBean>();
    /**
     * code和子集映射集合
     */
    private static Map<String, List<DictionaryBean>> sdMap_code_List = new ConcurrentHashMap<String, List<DictionaryBean>>();
    /**
     * 所有节点
     */
    private static List<DictionaryBean> all = null;

    public static void initSysDic(List<DictionaryBean> sdList) {
        for (DictionaryBean sysDictionary : sdList) {
            sdMap_code_bean.put(sysDictionary.getCode(), sysDictionary);
            sdMap_code_List.put(sysDictionary.getCode(), findChildList(
                    sysDictionary.getCode(), sdList));
        }
        all = sdList;
        if (all == null) {
            all = new ArrayList<DictionaryBean>();
        }
    }

    public static class SysDicMapResBean {
        private boolean success;
        private String msg;
        private int code;

        public SysDicMapResBean(boolean success, String msg, int code) {
            super();
            this.success = success;
            this.msg = msg;
            this.code = code;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMsg() {
            return msg;
        }

        public int getCode() {
            return code;
        }
    }

    /**
     * 根据父id返回所有子节点集合
     *
     * @param id
     * @param sysDictionaries
     * @return
     */
    private static List<DictionaryBean> findChildList(String code,
                                                      List<DictionaryBean> sysDictionaries) {
        List<DictionaryBean> list = new ArrayList<DictionaryBean>();
        for (DictionaryBean sysDictionary : sysDictionaries) {
            if (code.equals(sysDictionary.getPcode())) {
                list.add(sysDictionary);
            }
        }
        return list;
    }

    /**
     * @param @return
     * @return List<DictionaryBean>
     * @throws
     * @Description: 获取所有数据字典, 返回值为clone的副本，增删改操作不影响内存。
     * @author 张展弋
     * @date 2017-2-20 上午11:21:08
     */
    public static List<DictionaryBean> getAll() {
        List<DictionaryBean> list = new ArrayList<DictionaryBean>();
        for (DictionaryBean db : all) {
            list.add(db.clone());
        }
        return list;
    }

    /**
     * @param @param  code
     * @param @return
     * @return DictionaryBean
     * @throws
     * @Description: 根据code获取数据字典对象, 返回值为clone的副本，增删改操作不影响内存。
     * @author 张展弋
     * @date 2017-2-20 上午09:55:35
     */
    public static DictionaryBean getByCode(String code) {
        DictionaryBean db = sdMap_code_bean.get(code);
        if (db != null) {
            return db.clone();
        }
        return null;
    }

    /**
     * @param @param  code
     * @param @return
     * @return List<DictionaryBean>
     * @throws
     * @Description: 根据code值获取所有子节点。返回值为clone的副本，增删改操作不影响内存。
     * @author 张展弋
     * @date 2017-2-20 上午09:57:01
     */
    public static List<DictionaryBean> getChildList(String pcode) {
        List<DictionaryBean> l = new ArrayList<DictionaryBean>();
        List<DictionaryBean> list = sdMap_code_List.get(pcode);
        if (list == null) {
            return l;
        }
        for (DictionaryBean db : list) {
            l.add(db.clone());
        }
        return l;
    }

    /**
     * @param @param  sysDictionary
     * @param @return
     * @return SysDicMapResBean 成功返回0，-1表示code为空，-2表示节点已经存在
     * @throws
     * @Description: 新增
     * @author 张展弋
     * @date 2017-2-20 上午10:40:39
     */
    static SysDicMapResBean add(DictionaryBean sysDictionary) {
        String code = sysDictionary.getCode();
        if (code == null || "".equals(code.trim())) {
            return new SysDicMapResBean(false, "编码不能为空", -1);
        }
        // 如果code已经存在
        if (sdMap_code_bean.containsKey(code)) {
            return new SysDicMapResBean(false, "编码" + code + "已存在！", -2);
        }
        sdMap_code_bean.put(code, sysDictionary);
        List<DictionaryBean> list = sdMap_code_List.get(sysDictionary
                .getPcode());
        if (list != null) {
            list.add(sysDictionary);
        }
        sdMap_code_List.put(sysDictionary.getCode(),
                new ArrayList<DictionaryBean>());
        all.add(sysDictionary);
        return new SysDicMapResBean(true, "", 0);
    }

    private static void upDate(DictionaryBean from, DictionaryBean to) {
        to.setName(from.getName());
        to.setCkey(from.getCkey());
        to.setNote(from.getNote());
    }

    /**
     * @param @param  sysDictionary
     * @param @return
     * @return SysDicMapResBean 成功返回0，-1表示code为空，-2表示节点不存在
     * @throws
     * @Description: 修改
     * @author 张展弋
     * @date 2017-2-20 上午10:39:09
     */
    static SysDicMapResBean update(DictionaryBean sysDictionary) {
        String code = sysDictionary.getCode();
        if (code == null || "".equals(code.trim())) {
            return new SysDicMapResBean(false, "编码不能为空", -1);
        }
        // 如果code不存在
        DictionaryBean db = sdMap_code_bean.get(code);
        if (db == null) {
            return new SysDicMapResBean(false, "编码" + code + "不存在！", -2);
        }
        upDate(sysDictionary, db);
        List<DictionaryBean> list = sdMap_code_List.get(db.getPcode());
        if (list != null && list.size() > 0) {
            for (DictionaryBean dictionary : list) {
                if (code.equals(dictionary.getCode())) {
                    upDate(db, dictionary);
                    break;
                }
            }
        }
        for (DictionaryBean dictionary : all) {
            if (code.equals(dictionary.getCode())) {
                upDate(db, dictionary);
                break;
            }
        }
        return new SysDicMapResBean(true, "", 0);
    }

    /**
     * @param @param  code
     * @param @return
     * @return SysDicMapResBean 成功返回0，-1表示code为空，-2表示节点不存在，-3表示还有子节点
     * @throws
     * @Description: 删除
     * @author 张展弋
     * @date 2017-2-20 上午10:32:06
     */
    static SysDicMapResBean reomve(String code) {
        if (code == null || "".equals(code.trim())) {
            return new SysDicMapResBean(false, "code不能为空", -1);
        }
        // 如果code不存在
        DictionaryBean db = sdMap_code_bean.get(code);
        if (db == null) {
            return new SysDicMapResBean(false, "节点不存在", -2);
        }
        List<DictionaryBean> list = sdMap_code_List.get(code);
        if (list.size() > 0) {
            return new SysDicMapResBean(false, "不能删除包含子节点的数据", -3);
        }
        sdMap_code_bean.remove(code);
        sdMap_code_List.remove(code);
        list = sdMap_code_List.get(db.getPcode());
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (code.equals(list.get(i).getCode())) {
                    list.remove(i);
                    break;
                }
            }
        }
        for (int i = 0; i < all.size(); i++) {
            if (code.equals(all.get(i).getCode())) {
                all.remove(i);
                break;
            }
        }
        return new SysDicMapResBean(true, "", 0);
    }
}
