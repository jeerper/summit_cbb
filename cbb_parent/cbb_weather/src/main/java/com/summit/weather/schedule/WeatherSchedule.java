package com.summit.weather.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.summit.weather.service.IWeatherService;

public class WeatherSchedule {

	private static final Logger log = LoggerFactory.getLogger(WeatherSchedule.class);

	@Autowired
	IWeatherService iWeatherService;

	/**
	 * 插入今天的天气信息
	 */
	@Scheduled(cron = "${weather.save.today.weather}")
	public void saveWeather() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		log.info("开始保存" + dateFormat.format(new Date()) + "的天气信息............................");
		iWeatherService.save();
		log.info("结束保存" + dateFormat.format(new Date()) + "的天气信息............................");

	}

}
