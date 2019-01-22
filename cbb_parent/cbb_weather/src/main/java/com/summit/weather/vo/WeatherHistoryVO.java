package com.summit.weather.vo;

import java.util.Date;
import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
* @Title: WeatherDataBO.java
* @Package com.summit.bo.weather
* @Description: TODO
* @author hyn  
* @date 2018年11月28日 下午7:59:25
* @version V1.0  
 */
@ApiModel(value = "天气历史信息")
public class WeatherHistoryVO {

	@ApiModelProperty(value = "城市名", name = "cityName")
	String cityName;
	
	@ApiModelProperty(value = "时间", name = "time")
	Date time;
	
	@ApiModelProperty(value = "温度", name = "temperature")
	String temperature;
	
	@ApiModelProperty(value = "天气", name = "weather")
	String weather;
	
	@ApiModelProperty(value = "风力", name = "wind")
	String wind;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}
	
}
