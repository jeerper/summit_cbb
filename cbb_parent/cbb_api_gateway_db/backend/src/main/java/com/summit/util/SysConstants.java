package com.summit.util;

public class SysConstants {
	//超级管理员账号SYS_USER表中存在的用户都可以配置为超级管理员，但是只能有一个
	public static final String SUPER_USERNAME = "admin";
	public static final String SUROLE_CODE = "ROLE_SUPERUSER";

	// 通用分页数据条数
	public static final int PAGE_SIZE = 25;
	
	// 系统字典缓存 name
	public static final String  DICTIONARY = "dictionary";
	
	// 连接池缓存 name
	public static final String CONNPOOL = "connPool";
	
	public static final String DB_SQLSERVER = "sqlserver";
	public static final String DB_MYSQL = "Mysql";
	public static final String DB_ORCLE = "oracle";
	
	public static final String JDBC_DRIVER_SQLSERVER= "net.sourceforge.jtds.jdbc.Driver";
	public static final String JDBC_DRIVER_MYSQL= "com.mysql.jdbc.Driver";
	public static final String JDBC_DRIVER_ORLCE= "oracle.jdbc.driver.OracleDriver";
	
	public static final String JDBC_URL_SQLSERVER= "jdbc:jtds:sqlserver://";
	public static final String JDBC_URL_MYSQL= "jdbc:mysql://";
	public static final String JDBC_URL_ORLCE= "jdbc:oracle:thin:@";
	
	
	// 任务缓存 name
	public static final String QUARTZ = "quartz";
	
	// 任务的存储key值
	
	public static final String JOB_MAP_KEY = "scheduleJob";
	public static final String JOB_MAP_ADMIN_KEY = "admin";
}
