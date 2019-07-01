package com.summit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class MainAction {

	public static void main(String[] args){
		System.setProperty("jna.debug_load", "true");
		System.setProperty("jna.debug_load.jna", "true");
		SpringApplication.run(MainAction.class, args);
	}

}
