package com.summit.dao.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.LoginLogBean;
import com.summit.dao.entity.LoginLogInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface LoginLogDao extends BaseMapper<LoginLogBean> {
    /**
     * 查询最后一次登陆记录
     *
     * @param loginUserName
     * @param loginIp
     * @return
     */
    LoginLogBean getLastLoginLog(@Param("loginUserName") String loginUserName, @Param("loginIp") String loginIp);

    List<LoginLogInfo> getLoginLog(Page<LoginLogInfo> page, @Param("nickName") String nickName,
                                   @Param("start") Date start,
                                   @Param("end") Date end);
}
