package com.summit.common.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class WebServerConfig {

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大(KB,MB)
        factory.setMaxFileSize("1024MB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("10240MB");
        String file = System.getProperty("user.dir") + File.separator + "tomcat.temp";
        File tempFile = new File(file);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        factory.setLocation(file);
        return factory.createMultipartConfig();
    }
    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }


}
