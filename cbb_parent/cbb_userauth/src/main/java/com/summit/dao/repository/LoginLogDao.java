package com.summit.dao.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summit.common.entity.LoginLogBean;
import org.apache.ibatis.annotations.Param;


public interface LoginLogDao extends BaseMapper<LoginLogBean> {
    /**
     * 查询最后一次登陆记录
     * @param loginUserName
     * @param loginIp
     * @return
     */
    LoginLogBean getLastLoginLog(@Param("loginUserName")String loginUserName,@Param("loginIp")String loginIp);
}
