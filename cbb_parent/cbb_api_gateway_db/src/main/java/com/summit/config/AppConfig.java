package com.summit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("app")
public class AppConfig {


    private List<String> authWhiteList = new ArrayList<>();

    public String[] getAuthWhiteList() {
        return authWhiteList.toArray(new String[0]);
    }

    public void setAuthWhiteList(List<String> authWhiteList) {
        this.authWhiteList = authWhiteList;
    }
}
