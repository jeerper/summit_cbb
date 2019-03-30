package com.summit.common.config;

import com.summit.common.feign.interceptor.FeignHeaderInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign 拦截器配置
 *
 * @author Administrator
 */
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor feignHeaderInterceptor() {
        return new FeignHeaderInterceptor();
    }
}
