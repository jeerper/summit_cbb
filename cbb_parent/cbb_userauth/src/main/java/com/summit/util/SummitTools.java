package com.summit.util;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.summit.common.entity.LogBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;


/**
 * 提供一些常用的方法，框架级类请勿随意添加删除改动 ClassName: Tools
 */
@Component
public class SummitTools {
    private static final Logger log = LoggerFactory.getLogger(SummitTools.class);


    private XMLSerializer xmlSerializer = new XMLSerializer();
    private static Properties p;

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
    public static boolean stringEquals(String str1, String str2) {
        if (str1 == str2) {
            return true;
        } else if ((str1 != null && str2 == null)
                || (str1 == null && str2 != null)) {
            return false;
        } else if (str1.equals(str2)) {
            return true;
        } else {
            return false;
        }
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
    public static boolean stringIsNull(String s) {
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
    public static boolean stringNotNull(String s) {
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
    public boolean JSONObjectIsNull(JSONObject o) {
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
        if (JSONObjectIsNull(o)) {
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
        if (JSONObjectIsNull(o)) {
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
        if (JSONObjectIsNull(o)) {
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
        if (JSONObjectIsNull(o)) {
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
        if (JSONObjectIsNull(o)) {
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
        if (JSONObjectIsNull(o)) {
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
        if (JSONObjectIsNull(o)) {
            return null;
        }
        return o.get(key) == null ? null : o.getJSONArray(key);
    }


    /**
     * 返回数字id串的加引号格式
     * 如："100,101,102" 返回 "'100','101','102'"
     *
     * @param s
     * @return
     */
    public static String getIntIds(String s) {
        return s = "'" + s.replaceAll(",", "','") + "'";
    }


    public static Object setBeanNullStringToNull(Object obj) throws Exception {
        if (obj != null) {
            // 获取实体类的所有属性，返回Field数组
            Field[] field = obj.getClass().getDeclaredFields();
            // 获取属性的名字
            for (int i = 0; i < field.length; i++) {
                Field item = field[i];
                item.setAccessible(true); //设置些属性是可以访问的
//	        	String name = item.getName();    //获取属性的名字
                String type = item.getGenericType().toString();

                if (type.equals("class java.lang.String")) {
                    String value = (String) item.get(obj);
                    value = value == "" ? null : value;
                    item.set(obj, value);
                }
            }
        }

        return obj;
    }


    /***
     * 加载systemConfig.properties
     *
     * */
    public static Properties getSystemConfig() {
        if (p == null) {
            p = new Properties();
            try {
                p.load(SummitTools.class.getClassLoader().getResourceAsStream(
                        "systemConfig.properties"));
                if (log.isDebugEnabled()) {
                    log.debug("加载系统配置文件 systemConfig.properties 成功！");
                }

            } catch (Exception e) {
                log.error("加载 systemConfig.properties 失败！");
            }
        }

        return p;
    }

    /**
     * 给日期加秒数
     *
     * @param date
     * @param second
     * @return
     */
    public static Date addDateSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 截取adcd后面多余的0，并返回截取后的adcd
     *
     * @param adcd
     * @return
     */
    public static String getFirstAdcd(String adcd) {
        int i = adcd.lastIndexOf("0000000000") > 0 ? adcd.lastIndexOf("0000000000") : adcd.lastIndexOf("00000000") > 0 ? adcd.lastIndexOf("00000000") : adcd.lastIndexOf("000000") > 0 ? adcd.lastIndexOf("000000") : 0;
        return adcd = adcd.substring(0, i);
    }

    public static LogBean getLogBean(LogBean logBean, String funName, String operInfo, String operType) {
        if (logBean != null) {
            logBean.setSystemName("共享用户组件");
            logBean.setFunName(funName);
            logBean.setEtime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
            logBean.setOperInfo(operInfo);
            logBean.setOperType(operType);
        }
        return logBean;
    }
}
