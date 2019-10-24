package com.summit.dao.repository;

import com.summit.common.entity.FunctionBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FunctionDao {
    /**
     * 通过角色集合获取系统功能信息列表
     * @param roles 角色集合
     * @return
     */
    List<FunctionBean> getFunctionInfoListByRole(@Param("roles") List<String> roles);

}
