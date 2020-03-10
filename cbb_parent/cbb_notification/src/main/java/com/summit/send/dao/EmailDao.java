package com.summit.send.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summit.send.pojo.EmailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName EmailDao
 * @Description 邮件发送持久层
 * @Author maoyuxuan
 * @Date 2020/3/2 16:18
 * @Version 1.0
 **/
public interface EmailDao extends BaseMapper<EmailEntity> {

}
