package com.summit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


//@EnableZuulProxy
@EnableEurekaClient
//@ImportResource(locations= {"classpath:applicationContext.xml"})
@SpringBootApplication
public class MainAction {

    public static void main(String[] args) {
        SpringApplication.run(MainAction.class, args);
    }

}
