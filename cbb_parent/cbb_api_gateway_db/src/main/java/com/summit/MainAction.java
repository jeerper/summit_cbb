package com.summit;

import com.summit.common.config.FeignConfig;
import com.summit.common.web.filter.UserContextHolderFilter;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan(basePackages = "com.summit.dao.repository")
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                FeignConfig.class,
                UserContextHolderFilter.class
        }))
public class MainAction {

    public static void main(String[] args) {
        SpringApplication.run(MainAction.class, args);
    }

}
