package com.summit.common.api.userauth;

import com.summit.common.constant.ServiceNameConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户管理组件远程通信接口
 */
@FeignClient(value = ServiceNameConstant.User_Auth_Service)
public interface RemoteUserLogService {

    /**
     * 通过用户名查询用户、角色信息
     *
     * @param userName 用户名
     * @return RestFulEntityBySummit
     */
    @PostMapping("/login/log")
    void addLoginLog(@RequestBody String userName);

}
