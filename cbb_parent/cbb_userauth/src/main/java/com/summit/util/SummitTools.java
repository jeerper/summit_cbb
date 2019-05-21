package com.summit.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;


/**
 * 提供一些常用的方法，框架级类请勿随意添加删除改动 ClassName: Tools
 * 
 *
 * 
 * 
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
	public static  boolean stringEquals(String str1, String str2) {
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

	

	public Map<String, Object> error(String msg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		map.put("msg", msg);
		return map;
	}

	/**
	 * 字符串转时间用到的枚举 date:"yyyy-MM-dd";time:"HH:mm:ss"; date1:"yyyyMMdd"
	 * dateTime:"yyyy-MM-dd HH:mm:ss" dateTime1"yyyy/MM/dd HH:mm:ss"
	 * dateTimeN:"yyyy-MM-dd HH:mm:ss.SSS"
	 * 
	 * @author Administrator
	 * 
	 */
	public enum DateTimeType {
		year, date, date1, time, dateTime, dateTime1, dateTimeN
	}


	/**
	 * 将时间转为字符串
	 * 
	 * @param dateTimeType
	 * @param date
	 * @return
	 */
	public  static String DTFormat(String aMask, Date date) {
		SimpleDateFormat yearFormat = new SimpleDateFormat(aMask);
		return yearFormat.format(date);   
	}



	/**
	 * 将xml转成JSONObject
	 * 
	 * @param jsonXml
	 * @return
	 * @author zzy
	 * @time 2015-1-20 下午08:34:32
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
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
	 * 
	 */
	public JSONArray objJsonGetJSONArray(JSONObject o, String key) {
		if (JSONObjectIsNull(o)) {
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
	public <T> ZTreeNodeClass<T> creaZTreeNodeClass(final String pId,
			final String id, final String name, final Boolean checked,
			final Boolean open, final Boolean nocheck, final T nodeData) {
		return new ZTreeNodeClass<T>() {

			public String getpId() {
				return pId;
			}

			public String getId() {
				return id;
			}

			public String getName() {
				return name;
			}

			public Boolean getChecked() {
				return checked;
			}

			public Boolean getOpen() {
				return open;
			}

			public Boolean getNocheck() {
				return nocheck;
			}

			public T getNodeData() {
				return nodeData;
			}
		};
	}

	/**
	 * Ztree发送数据用接口
	 * 
	 * @author zzy
	 * 
	 * @param <D>
	 */
	public static interface ZTreeNodeClass<D> {
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
		 */
		public D getNodeData();

	}

	/**
	 * 用于将实体bean组装成一棵树与List<TreeNode> creatTreeNode(List<TreeNodeClass> data,
	 * String pId,boolean expanded)方法配合使用。（给EXTtreepanel用）
	 * 
	 * @author zzy
	 * @time 2014-12-12 上午11:02:04
	 * 
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
		 * 
		 */
		public Boolean getLeaf();

		/**
		 * 是否展开true时展开
		 * 
		 * @Description: 
		 * @param @return
		 * @return Boolean
		 * @throws
		 * @author 张展弋
		 * @date 2017-1-6 上午11:33:09
		 */
		public Boolean getOpen();

	}

	public TreeNodeClass<Object> creaTreeNodeClass(final String nodeText,
			final String nodePid, final String nodeId, final Object nodeData,
			final Boolean checked) {
		return new TreeNodeClass<Object>() {
			public String getNodeText() {
				return nodeText;
			}

			public String getNodePid() {
				return nodePid;
			}

			public String getNodeId() {
				return nodeId;
			}

			public Object getNodeData() {
				return nodeData;
			}

			public Boolean getChecked() {
				return checked;
			}

			public Boolean getLeaf() {
				return null;
			}

			public Boolean getOpen() {
				return null;
			}
		};
	}

	
	/**
	 * 获取给定时间月的最大天数
	 * 
	 * @param date
	 * @return int
	 */
	public static int getMaxDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 返回数字id串的加引号格式
	 * 如："100,101,102" 返回 "'100','101','102'"
	 * @param s
	 * @return
	 */
	public static String getIntIds(String s){
		return s = "'" + s.replaceAll(",", "','") + "'";
	}
	
	
	public static Object setBeanNullStringToNull(Object obj) throws Exception {
		if(obj!=null){
			 // 获取实体类的所有属性，返回Field数组
	        Field[] field = obj.getClass().getDeclaredFields();
	        // 获取属性的名字
	        for (int i = 0; i < field.length; i++){
	        	Field item = field[i];
	        	item.setAccessible(true); //设置些属性是可以访问的  
//	        	String name = item.getName();    //获取属性的名字
	            String type = item.getGenericType().toString();
	            
	            if (type.equals("class java.lang.String")){
	                String value =  (String) item.get(obj);
	                value = value==""?null:value;
	                item.set(obj,value);
	            }
	        }
		}
		
		return obj;
	}
	
	
	/**
	 * 计算两个时间差，要精确到小时
	 * date1 开始时间
	 * date2 结束时间
	 * **/
	public static double jisuan(String date1, String date2) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
		Date start = sdf.parse(date1);
		Date end = sdf.parse(date2);
		long cha = end.getTime() - start.getTime();
		double result = cha * 1.0 / (1000 * 60 * 60);
		//logUtil.info("开始时间:"+date1+"结束时间:"+date2+"相差:"+result+"小时");
		return result;
	}
	
	/**
	 *从字符串中提取数字
	 */

	public static double get_StringNum (String a) {
		String ret = "";
		Double d = 0.0;
		String regEx="[0-9\\.]+";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(a); 
		while(m.find()){
			ret += m.group();
		}
		if(!"".equals(ret)){
			d = Double.parseDouble(ret);
		}
		return d;
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
	 * @param adcd
	 * @return
	 */
	public static String getFirstAdcd(String adcd) {
		int i = adcd.lastIndexOf("0000000000")> 0 ? adcd.lastIndexOf("0000000000") : adcd.lastIndexOf("00000000") > 0 ? adcd.lastIndexOf("00000000") : adcd.lastIndexOf("000000") > 0 ? adcd.lastIndexOf("000000") : 0;
		return adcd = adcd.substring(0, i);
	}
}
