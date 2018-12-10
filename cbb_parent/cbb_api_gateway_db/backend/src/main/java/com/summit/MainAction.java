package com.summit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableZuulProxy
//@EnableEurekaClient
//@EnableDiscoveryClient
//@ImportResource(locations= {"classpath:applicationContext.xml"})
public class MainAction {

    public static void main(String[] args) {
        SpringApplication.run(MainAction.class, args);
    }

//    @Bean
//    RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
    
}
