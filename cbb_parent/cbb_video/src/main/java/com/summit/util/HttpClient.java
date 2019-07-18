package com.summit.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.summit.service.UBLockService;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

public class HttpClient {
    public UBLockService ubLockService;
    private String baseUrl;


    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
//                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ubLockService = retrofit.create(UBLockService.class);

    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
