package com.summit.weather.service;

import java.util.Date;
import java.util.List;

import com.summit.weather.vo.WeatherForecastVO;
import com.summit.weather.vo.WeatherHistoryVO;

public interface IWeatherService {

    /**
     * 根据经纬度获取天气预报情况(包括：实时天气，当天天气，未来天气)
     *
     * @param lgtd
     * @param lttd
     * @return 例如：lgtd=114.371389,lttd=30.483611
     */
    public WeatherForecastVO getWeatherForecast(String lgtd, String lttd);


    /**
     * 根据地名获取天气预报情况(包括：实时天气，当天天气，未来天气)
     *
     * @param cityName
     * @return 例如：武汉
     */
    public WeatherForecastVO getWeatherForecast(String cityName);


    /**
     * 保存当天天气情况
     *
     * @return
     */
    public String save();

    /**
     * 根据城市名获取天气历史情况
     *
     * @param cityName
     * @return 例如：雁塔区
     */
    public List<WeatherHistoryVO> getWeatherHistory(String cityName, Date startTime, Date endTime);

}
