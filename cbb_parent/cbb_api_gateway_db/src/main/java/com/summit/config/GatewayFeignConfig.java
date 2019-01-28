package com.summit.config;


import com.summit.interceptor.GatewayHeaderInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign 拦截器配置
 *
 * @author Administrator
 */

@Configuration
public class GatewayFeignConfig {

    @Bean
    public RequestInterceptor feignHeaderInterceptor() {
        return new GatewayHeaderInterceptor();
    }
}
