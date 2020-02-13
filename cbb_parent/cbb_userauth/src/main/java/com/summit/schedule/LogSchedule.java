package com.summit.schedule;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.summit.common.constant.CommonConstant;
import com.summit.common.entity.LoginLogBean;
import com.summit.dao.repository.LoginLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Slf4j
@Component
public class LogSchedule {

    @Autowired
    RedisTemplate<String, Object> genericRedisTemplate;
    @Autowired
    LoginLogDao loginLogDao;


    /**
     * 登陆用户在线时长计时器(一分钟执行一次)
     */
    @Scheduled(fixedDelay = 60000)
    public void loginLogOnlineTimeTiming() {
        Set<String> keys = genericRedisTemplate.keys(CommonConstant.LOGIN_LOG_PREFIX + "*");
        for (String key : keys) {
            String loginId = (String) genericRedisTemplate.opsForValue().get(key);
            LoginLogBean loginLogBean = loginLogDao.selectOne(Wrappers.<LoginLogBean>lambdaQuery().eq(LoginLogBean::getId, loginId));
            if(loginLogBean==null){
                continue;
            }
            long differMinute = DateUtil.between(loginLogBean.getLoginTime(), new Date(), DateUnit.MINUTE);
            loginLogDao.update(null, Wrappers.<LoginLogBean>lambdaUpdate()
                    .set(LoginLogBean::getOnlineTime, differMinute)
                    .eq(LoginLogBean::getId, loginId));
        }
    }
}
