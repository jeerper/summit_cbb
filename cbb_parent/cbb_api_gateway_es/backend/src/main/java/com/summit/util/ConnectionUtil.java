package com.summit.util;

import java.sql.Connection;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
/**
 * 
 * @author yt
 *
 */
public class ConnectionUtil {

	public static Connection getConnection(String url) {
		Connection connection = null;
		try {
			Properties properties = new Properties();
			properties.put("url", url);
			DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
			connection = dds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return connection;
	}

}
