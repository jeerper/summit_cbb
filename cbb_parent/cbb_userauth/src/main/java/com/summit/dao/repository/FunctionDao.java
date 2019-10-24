package com.summit.dao.repository;

import com.summit.common.entity.FunctionBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FunctionDao {

    List<FunctionBean> getFunctionInfoListByUserName(@Param("userName")String userName);

}
