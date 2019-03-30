package com.summit.common.api.demo;

import com.summit.common.constant.ServiceNameConstant;
import com.summit.common.entity.RestfulEntityBySummit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Demo组件远程通信接口
 */
@FeignClient(value = ServiceNameConstant.Demo_Service)
public interface RemoteDemoService {

    /**
     * demo接口,无参数传递，返回失败代码，返回用户信息
     *
     * @return RestFulEntityBySummit
     */
    @GetMapping("/demo/haveParamDemo")
    RestfulEntityBySummit<String> haveParamDemo();

}
