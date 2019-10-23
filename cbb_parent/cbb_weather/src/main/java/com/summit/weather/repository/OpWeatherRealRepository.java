package com.summit.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.summit.weather.dto.OpWeatherReal;

import java.lang.String;

/**
 * 查询天气历史情况
 *
 * @author hyn  
 * @version V1.0  
 * @Title: OpWeatherRealRepository.java
 * @Package com.summit.homs.repository.weather
 * @Description: TODO
 * @date 2018年12月3日 下午8:15:15
 */
public interface OpWeatherRealRepository extends JpaRepository<OpWeatherReal, String>, JpaSpecificationExecutor<OpWeatherReal> {


}
