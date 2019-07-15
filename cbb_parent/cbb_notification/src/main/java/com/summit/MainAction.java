package com.summit;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients
@MapperScan(basePackages = "com.summit.send.dao")
@EnableCaching
public class MainAction {

	public static void main(String[] args) {
		SpringApplication.run(MainAction.class, args);
	}
	
	
	
}
