package com.summit.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * @author yt
 */
@Component("st")
public class SummitTools {
    private XMLSerializer xmlSerializer = new XMLSerializer();

    /**
     * 获得唯一Id(通用)
     *
     * @return
     */
    public static String getKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 比较两个字符串是否equals，当两个字符串都为null时返回true
     *
     * @param str1
     * @param str2
     * @return
     */
    public boolean stringEquals(String str1, String str2) {
        if (str1 == str2) {
            return true;
        } else if (str1 != null && str2 == null) {
            return false;
        } else if (str1 == null && str2 != null) {
            return false;
        } else if (str1.equals(str2)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 后台默认返回前台数据
     *
     * @param msg
     * @return
     */
/*	public JSONObject success(String msg , Object data){
		JSONObject js = new JSONObject();
		js.put("success", true);
		js.put("msg", msg);
		js.put("data", data);
		return js;
	}
	public JSONObject error(String msg , Object data){
		JSONObject js = new JSONObject();
		js.put("success", false);
		js.put("msg", msg);
		js.put("data", data);
		return js;
	}*/
    public Map<String, Object> success(String msg) {
        return success(msg, null);
    }

    public Map<String, Object> success(String msg, Object data) {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("success", true);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }

    public Map<String, Object> error(String msg, Object data) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("success", false);
        map.put("msg", msg);
        return map;
    }

    /**
     * 将xml转成JSONObject
     *
     * @param jsonXml
     * @return
     * @author zzy
     * @time 2015-1-20 下午08:34:32
     */
    public JSONObject xmlToJSONObject(String jsonXml) {
        if (jsonXml == null) {
            return null;
        }
        JSONObject json = null;
        try {
            json = JSONObject.fromObject(xmlSerializer.read(jsonXml));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * 判断集合是否为空，当集合为null或size()==0时返回true
     *
     * @param <T>
     * @param c
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:50:46
     */
    public <T extends Collection<?>> boolean collectionIsNull(T c) {
        if (c == null || c.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断集合是否不为空，当集合不为null且长度大于1时返回true
     *
     * @param <T>
     * @param c
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:51:06
     */
    public <T extends Collection<?>> boolean collectionNotNull(T c) {
        if (c != null && c.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 当obj数组为null或obj.length==0时返回true
     *
     * @param obj
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:51:17
     */
    public boolean arrayIsNull(Object obj[]) {
        if (obj == null || obj.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 当字符串为null或长度为0时返回true
     *
     * @param s
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:51:29
     */
    public boolean stringIsNull(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 当字符串不为null且长度大于0时返回true
     *
     * @param s
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:58:33
     */
    public boolean stringNotNull(String s) {
        if (s != null && !s.isEmpty() && s.trim().length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断JSONObject是否为空（null或不存在任何有效值）
     *
     * @param o
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:59:04
     */
    public boolean jSONObjectIsNull(JSONObject o) {
        if (o == null || o.isNullObject()) {
            return true;
        }
        return false;
    }

    /**
     * 防止key对应的值不存在时报空指针异常，当JSONObject为空或key所对应值不存在时返回null
     *
     * @param o
     * @param key
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:59:18
     */
    public String objJsonGetString(JSONObject o, String key) {
        if (jSONObjectIsNull(o)) {
            return null;
        }
        return o.get(key) == null ? null : o.getString(key);
    }

    /**
     * 防止key对应的值不存在时报空指针异常，当JSONObject为空或key所对应值不存在时返回null
     *
     * @param o
     * @param key
     * @return
     */
    public Boolean objJsonGetBoolean(JSONObject o, String key) {
        if (jSONObjectIsNull(o)) {
            return null;
        }
        return o.get(key) == null ? null : o.getBoolean(key);
    }

    /**
     * 防止key对应的值不存在时报空指针异常，当JSONObject为空或key所对应值不存在时返回null
     *
     * @param o
     * @param key
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:59:18
     */
    public Double objJsonGetDouble(JSONObject o, String key) {
        if (jSONObjectIsNull(o)) {
            return null;
        }
        return o.get(key) == null ? null : o.getDouble(key);
    }

    /**
     * 防止key对应的值不存在时报空指针异常，当JSONObject为空或key所对应值不存在时返回null
     *
     * @param o
     * @param key
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:59:18
     */
    public Integer objJsonGetInteger(JSONObject o, String key) {
        if (jSONObjectIsNull(o)) {
            return null;
        }
        return o.get(key) == null ? null : o.getInt(key);
    }

    /**
     * 防止key对应的值不存在时报空指针异常，当JSONObject为空或key所对应值不存在时返回null
     *
     * @param o
     * @param key
     * @return
     * @author zzy
     * @time 2014-12-26 上午11:53:34
     */
    public JSONObject objJsonGetJSONObject(JSONObject o, String key) {
        if (jSONObjectIsNull(o)) {
            return null;
        }
        return o.get(key) == null ? null : o.getJSONObject(key);
    }

    /**
     * 防止key对应的值不存在时报空指针异常，当JSONObject为空或key所对应值不存在时返回null
     *
     * @param o
     * @param key
     * @return
     * @author zzy
     * @time 2014-12-26 上午11:54:51
     */
    public Long objJsonGetLong(JSONObject o, String key) {
        if (jSONObjectIsNull(o)) {
            return null;
        }
        return o.get(key) == null ? null : o.getLong(key);
    }

    /**
     * 数据库是datetime格式的，通过sql查出来的是个复杂的jsonobject，此方法用于提取时间中的long值
     *
     * @param o
     * @param key
     * @return
     * @author zzy
     * @time 2015-1-12 下午04:27:29
     */
    public Long objJsonGetTimeLong(JSONObject o, String key) {
        JSONObject obj = objJsonGetJSONObject(o, key);
        if (obj == null) {
            return null;
        }
        return objJsonGetLong(obj, "time");
    }

    /**
     * 防止key对应的值不存在时报空指针异常，当JSONObject为空或key所对应值不存在时返回null
     *
     * @param o
     * @param key
     * @return
     * @author zzy
     * @time 2014-12-12 上午10:59:47
     */
    public JSONArray objJsonGetJSONArray(JSONObject o, String key) {
        if (jSONObjectIsNull(o)) {
            return null;
        }
        return o.get(key) == null ? null : o.getJSONArray(key);
    }


    /**
     * 创建ztree接口,典型应用可参考数据字典模块
     *
     * @param pId
     * @param id
     * @param name
     * @param checked
     * @param open
     * @param nocheck
     * @param nodeData
     * @return
     */
    public <T> ZtreeNodeClass<T> creaZTreeNodeClass(final String pId,
                                                    final String id, final String name, final Boolean checked,
                                                    final Boolean open, final Boolean nocheck, final T nodeData) {
        return new ZtreeNodeClass<T>() {
            @Override
            public String getpId() {
                return pId;
            }

            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Boolean getChecked() {
                return checked;
            }

            @Override
            public Boolean getOpen() {
                return open;
            }

            @Override
            public Boolean getNocheck() {
                return nocheck;
            }

            @Override
            public T getNodeData() {
                return nodeData;
            }
        };
    }

    /**
     * Ztree发送数据用接口
     *
     * @param <D>
     * @author zzy
     */
    public static interface ZtreeNodeClass<D> {
        /**
         * 节点id
         *
         * @return
         */
        public String getpId();

        /**
         * 节点父Id
         *
         * @return
         */
        public String getId();

        /**
         * 节点名称
         *
         * @return
         */
        public String getName();

        /**
         * 是否选中true 表示节点的输入框被勾选 false 表示节点的输入框未勾选
         *
         * @return
         */
        public Boolean getChecked();

        /**
         * 是否展开true 表示节点为 展开 状态 false 表示节点为 折叠 状态
         *
         * @return
         */
        public Boolean getOpen();

        /**
         * true 表示此节点不显示 checkbox / radio，不影响勾选的关联关系，不影响父节点的半选状态。false
         * 表示节点具有正常的勾选功能
         *
         * @return
         */
        public Boolean getNocheck();

        /**
         * 获取节点的附加属性
         *
         * @return
         */
        public D getNodeData();

    }

    /**
     * 用于将实体bean组装成一棵树与List<TreeNode> creatTreeNode(List<TreeNodeClass> data,
     * String pId,boolean expanded)方法配合使用。（给EXTtreepanel用）
     *
     * @author zzy
     * @time 2014-12-12 上午11:02:04
     */
    public static interface TreeNodeClass<D> {
        /**
         * 获取该节点ID
         *
         * @return
         */
        public String getNodeId();

        /**
         * 获取该节点父ID
         *
         * @return
         */
        public String getNodePid();

        /**
         * 获取该节点显示文字
         *
         * @return
         */
        public String getNodeText();

        /**
         * 获取节点的附加属性
         *
         * @return
         */
        public D getNodeData();

        /**
         * 获取节点是否有复选框
         *
         * @return
         */
        public Boolean getChecked();

        /**
         * 是否是叶子节点，如果不做强制要求，返回null即可。主要用于父节点没有子节点，但是还是要保持父节点的状态。
         *
         * @return
         * @author zzy
         * @time 2016-10-9 下午04:11:44
         */
        public Boolean getLeaf();

        /**
         * 是否展开true时展开
         *
         * @param @return
         * @return Boolean
         * @throws
         * @Description:
         * @author 张展弋
         * @date 2017-1-6 上午11:33:09
         */
        public Boolean getOpen();

    }

    public TreeNodeClass<Object> creaTreeNodeClass(final String nodeText,
                                                   final String nodePid, final String nodeId, final Object nodeData,
                                                   final Boolean checked) {
        return new TreeNodeClass<Object>() {
            @Override
            public String getNodeText() {
                return nodeText;
            }

            @Override
            public String getNodePid() {
                return nodePid;
            }

            @Override
            public String getNodeId() {
                return nodeId;
            }

            @Override
            public Object getNodeData() {
                return nodeData;
            }

            @Override
            public Boolean getChecked() {
                return checked;
            }

            @Override
            public Boolean getLeaf() {
                return null;
            }

            @Override
            public Boolean getOpen() {
                return null;
            }
        };
    }

    /**
     * 将实现接口的bean集合组装成一棵树（给EXTtreepanel用）
     *
     * @param <T>
     * @param data     实现TreeNodeClass接口的bean集合
     * @param pId      父ID(根据此父ID生成树)
     * @param expanded 整棵树是否展开
     * @return
     * @author 张展弋
     * @time 2014-12-12 上午11:03:31
     */
    public <T extends TreeNodeClass<D>, D> List<TreeNode<D>> creatTreeNode(
            Iterable<T> data, String pId) {
        List<TreeNode<D>> list = new ArrayList<TreeNode<D>>();
        if (data == null) {
            return list;
        }
        if (pId == null) {
            for (TreeNodeClass<D> treeNodeClass : data) {
                if (treeNodeClass.getNodePid() == null) {
                    list.add(creatTreeNode(treeNodeClass, data, pId));
                }
            }

        } else {
            for (TreeNodeClass<D> treeNodeClass : data) {
                if (pId.equals(treeNodeClass.getNodePid())) {
                    list.add(creatTreeNode(treeNodeClass, data, pId));
                }
            }
        }
        return list;
    }

    private <T extends TreeNodeClass<D>, D> TreeNode<D> creatTreeNode(
            TreeNodeClass<D> treeNodeClass, Iterable<T> data, String pId) {
        TreeNode<D> treeNode = new TreeNode<D>();
        treeNode.setId(treeNodeClass.getNodeId());
        treeNode.setText(treeNodeClass.getNodeText());
        treeNode.setData(treeNodeClass.getNodeData());
        treeNode.setChecked(treeNodeClass.getChecked());
        treeNode.setExpanded(treeNodeClass.getOpen());
        List<TreeNode<D>> children = creatTreeNode(data, treeNode.getId());
        if (treeNodeClass.getLeaf() != null) {
            treeNode.setLeaf(treeNodeClass.getLeaf());
        } else {
            if (collectionNotNull(children)) {
                treeNode.setLeaf(false);
            }
        }
        if (collectionNotNull(children)) {
            treeNode.setChildren(children);
        }
        return treeNode;
    }

    /**
     * 从实现接口的bean集合中取出指定父节点下的所有子节点（给EXTtreepanel用）
     *
     * @param <T>
     * @param data 实现TreeNodeClass接口的bean集合
     * @param pId  父ID(根据此父ID提取数据)
     * @return
     * @author zzy
     * @time 2014-4-10 下午07:53:12
     */
    public <T extends TreeNodeClass<D>, D> List<T> getTreeNodeList(
            Iterable<T> data, String pId) {
        List<T> list = new ArrayList<T>();
        if (data == null) {
            return list;
        }
        if (pId == null) {
            for (T t : data) {
                if (t.getNodePid() == null) {
                    list.add(t);
                    list.addAll(getTreeNodeList(data, t.getNodeId()));
                }
            }

        } else {
            for (T t : data) {
                if (pId.equals(t.getNodePid())) {
                    list.add(t);
                    list.addAll(getTreeNodeList(data, t.getNodeId()));
                }
            }
        }
        return list;
    }


    public static <T> T stringtobean(String str, T obj) {
        String c = str.substring(str.indexOf("["), str.lastIndexOf("]") + 1);
        c = c.replace(" ", "");
        c = c.replace("[", "{\"");
        c = c.replace("]", "\"}");
        c = c.replace("=", "\":\"");
        c = c.replace(",", "\",\"");
        JSONObject jsonObject = JSONObject.fromObject(c);
        T strbean = (T) JSONObject.toBean(jsonObject, obj.getClass());
        return strbean;
    }
}
