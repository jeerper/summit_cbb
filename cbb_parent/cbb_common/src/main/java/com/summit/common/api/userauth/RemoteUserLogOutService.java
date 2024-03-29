package com.summit.common.api.userauth;

import com.summit.common.constant.ServiceNameConstant;
import com.summit.common.entity.RestfulEntityBySummit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户管理组件远程通信接口
 */
@FeignClient(value = ServiceNameConstant.Gate_Way_Service)
public interface RemoteUserLogOutService {

    /**
     * 退出登录
     * @return true退出登录
     */
    @PostMapping("/oauth/logout")
    RestfulEntityBySummit<Boolean> logout();

    /**
     * 通过用户名称退出登录
     * @param username
     * @return true退出登录
     */
    @PostMapping("/oauth/logout/{username}")
    RestfulEntityBySummit<Boolean> logout(@PathVariable(value = "username") String username);
}
