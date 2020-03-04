package com.summit.send.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summit.send.pojo.EmailEntity;
import com.summit.send.pojo.EmailResultEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName EmailResultDao
 * @Description 邮件发送结果持久层
 * @Author maoyuxuan
 * @Date 2020/3/2 16:18
 * @Version 1.0
 **/
public interface EmailResultDao extends BaseMapper<EmailResultEntity> {

    /**
     * 批量新增
     * @param emailResultEntityList
     */
    void insertBatch(@Param("emailResults") List<EmailResultEntity> emailResultEntityList);

}
