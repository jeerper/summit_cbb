package com.summit;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
@EnableAdminServer
public class MainAction {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainAction.class, args);
    }

}
