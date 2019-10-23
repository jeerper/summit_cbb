package com.summit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summit.domain.User;

public interface DemoDao extends BaseMapper<User> {

    /**
     * 使用xml配置形式查询
     *
     * @return
     */
    public User getUserInfoByUserName();


}
