package com.summit.common.api.userauth;

import com.summit.common.constant.ServiceNameConstant;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户管理组件远程通信接口
 */
@FeignClient(value = ServiceNameConstant.User_Auth_Service)
public interface RemoteUserAuthService {

    /**
     * 通过用户名查询用户、角色信息
     *
     * @param userName 用户名
     * @return RestFulEntityBySummit
     */
    @GetMapping("/user/queryUserRoleByUserName")
    RestfulEntityBySummit<UserInfo> queryUserRoleByUserName(@RequestParam("userName") String userName);

}
