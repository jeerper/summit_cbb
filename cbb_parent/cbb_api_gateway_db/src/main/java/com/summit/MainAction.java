package com.summit;

import com.summit.common.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@EnableZuulProxy
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                FeignConfig.class
        }))
public class MainAction {

    public static void main(String[] args) {
        SpringApplication.run(MainAction.class, args);
    }

}
