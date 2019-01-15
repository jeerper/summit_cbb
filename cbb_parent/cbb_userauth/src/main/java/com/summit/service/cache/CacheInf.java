package com.summit.service.cache;

import org.springframework.stereotype.Component;


@Component
public interface CacheInf {
	/**
	 * 添加缓存区
	 * @param cacheName 缓存区名称
	 */
	public   void addCache(String cacheName);
	
	/**
	 * 给缓存区添加元素
	 * @param cacheName 缓存区名称
	 * @param key 元素key值
	 * @param value 元素value值
	 */
	public   void setCacheElement(String cacheName, Object key, Object value);

	/**
	 * 从缓存区获取指定元素
	 * @param cacheName 缓存区名称
	 * @param key 元素key值
	 * @return
	 */
	public  Object getCacheElement(String cacheName, Object key);

	/**
	 * 从指定缓存区删除指定元素
	 * @param cacheName 缓存区名称
	 * @param key 元素key值
	 */
	public  void removeCacheElement(String cacheName, Object key);
}
