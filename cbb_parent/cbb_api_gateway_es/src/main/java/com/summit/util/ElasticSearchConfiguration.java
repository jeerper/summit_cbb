package com.summit.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 
 * @author yt
 *
 */
@Component
public class ElasticSearchConfiguration implements InitializingBean {
	static {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		//System.out.println("es.set.netty.runtime.available.processors:{}" +  System.getProperty("es.set.netty.runtime.available.processors")); 
		}
}
