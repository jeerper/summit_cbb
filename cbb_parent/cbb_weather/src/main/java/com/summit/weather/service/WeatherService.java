package com.summit.weather.service;

import com.summit.bo.weather.WeatherDataBO;

public interface WeatherService {
	
	/**
	 * 根据经纬度获取天气情况
	 * @param lgtd
	 * @param lttd
	 * @return
	 *  例如：lgtd=114.371389,lttd=30.483611
	 */
	public WeatherDataBO getWeather(String lgtd,String lttd);
	
	
	/**
	 * 根据地名获取天气情况
	 * @param  cityName
	 * @return
	 * 例如：武汉
	 */
    public WeatherDataBO getWeather(String cityName);

}
