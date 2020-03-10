package com.summit.common.api.userauth;

import com.summit.common.constant.ServiceNameConstant;
import com.summit.common.entity.LoginLogBean;
import com.summit.common.entity.RestfulEntityBySummit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户管理组件远程通信接口
 */
@FeignClient(value = ServiceNameConstant.User_Auth_Service)
public interface RemoteUserLogService {

     /**
     * 新增用户登录日志
     * @param loginId 登陆ID
     * @param loginUserName 登陆用户名
     * @param loginIp 登陆IP
      *@param logSuccessOrNot 是否登录成功
     * @return
     */
    @PostMapping("/log/login")
    RestfulEntityBySummit<String> addLoginLog(@RequestParam("loginId") String loginId,
                                              @RequestParam("loginUserName") String loginUserName,
                                              @RequestParam("loginIp") String loginIp,
                                              @RequestParam("logSuccessOrNot") String logSuccessOrNot);
    /**
     * 查询最后一次登陆记录
     * @param loginUserName 登陆用户名
     * @param loginIp 登陆IP
     * @return
     */
    @GetMapping("/log/last-login-log")
    RestfulEntityBySummit<LoginLogBean> getLastLoginLog(@RequestParam("loginUserName") String loginUserName,
                                                        @RequestParam("loginIp") String loginIp);


}
