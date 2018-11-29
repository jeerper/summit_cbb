package com.summit.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


@Service("cacheImpl")
public class CacheImpl implements CacheInf{
	
	@Autowired
	private CacheManager cacheManager;
	
	
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	/**
	 * 添加缓存区
	 * @param cacheName 缓存区名称
	 */
	public   void addCache(String cacheName){
		cacheManager.addCache(cacheName);
	}
	
	/**
	 * 给缓存区添加元素
	 * @param cacheName 缓存区名称
	 * @param key 元素key值
	 * @param value 元素value值
	 */
	public   void setCacheElement(String cacheName,Object key,Object value){
		Cache cache = cacheManager.getCache(cacheName);
		Element element = new Element(key, value);
		cache.put(element);
	}
	
	/**
	 * 从缓存区获取指定元素
	 * @param cacheName 缓存区名称
	 * @param key 元素key值
	 * @return
	 */
	public  Object getCacheElement(String cacheName,Object key){
		 Cache cache = cacheManager.getCache(cacheName);
		 Element element = cache.get(key);
		 if(element==null)return null ;
		 Object value = element.getObjectValue();
		return value;
	}
	
	/**
	 * 从指定缓存区删除指定元素
	 * @param cacheName 缓存区名称
	 * @param key 元素key值
	 */
	public  void removeCacheElement(String cacheName,Object key){
		Cache cache = cacheManager.getCache(cacheName);
		cache.remove(key);
	}
}
