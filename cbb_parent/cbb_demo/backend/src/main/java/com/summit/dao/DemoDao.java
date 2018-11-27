package com.summit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.summit.domain.User;

@Mapper
public interface DemoDao {
    //使用xml配置形式查询
    public List<User> getAllUser();
}
