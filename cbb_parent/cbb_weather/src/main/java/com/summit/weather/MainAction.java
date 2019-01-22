package com.summit.weather;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 
* @Title: MainApplication.java
* @Package com.summit
* @Description: TODO
* @author hyn  
* @date 2018年12月5日 下午3:47:58
* @version V1.0  
 */
@EnableJpaAuditing
@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class MainAction {

	public static void main(String[] args) {
		SpringApplication.run(MainAction.class, args);
	}
	
}
