package com.summit;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * 
* @Title: MainApplication.java
* @Package com.summit
* @Description: TODO
* @author hyn  
* @date 2018年12月5日 下午3:47:58
* @version V1.0  
 */
@EnableEurekaClient
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class MainAction {

	public static void main(String[] args) {
		SpringApplication.run(MainAction.class, args);
	}
	
}
