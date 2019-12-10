package com.summit.weather.controller;

import com.summit.weather.service.IWeatherService;
import com.summit.weather.vo.WeatherForecastVO;
import com.summit.weather.vo.WeatherHistoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * @author hyn  
 * @version V1.0  
 * @Title: WeatherController.java
 * @Package com.summit.homs.controller.weather
 * @Description: TODO
 * @date 2018年12月5日 下午3:33:53
 */
@Api(tags = "天气接口", value = "weather")
@Controller
@RequestMapping("weather")
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    IWeatherService iWeatherService;


    @ApiOperation(value = "依据经纬度获得实时天气，包括实时天气，当天天气，未来天气")
    @RequestMapping(value = "/{lgtd}/{lttd}", method = RequestMethod.GET)
    @ResponseBody
    public WeatherForecastVO findWeather(
            @ApiParam(value = "经度", required = true) @PathVariable(name = "lgtd") String lgtd,
            @ApiParam(value = "纬度", required = true) @PathVariable(name = "lttd") String lttd) {
        WeatherForecastVO weatherDataVo = iWeatherService.getWeatherForecast(lgtd, lttd);
        return weatherDataVo;
    }


    @ApiOperation(value = "依据城市名获得实时天气，包括实时天气，当天天气，未来天天气")
    @RequestMapping(value = "/{cityName}", method = RequestMethod.GET)
    @ResponseBody
    public WeatherForecastVO findWeather(
            @ApiParam(value = "城市名", required = true) @PathVariable(name = "cityName") String cityName) {
        WeatherForecastVO weatherDataVo = iWeatherService.getWeatherForecast(cityName);
        return weatherDataVo;
    }

    @ApiOperation(value = "依据城市名获取历史天气")
    @RequestMapping(value = "/history/{cityName}/{startTime}/{endTime}", method = RequestMethod.GET)
    @ResponseBody
    public List<WeatherHistoryVO> findHistoryWeather(
            @ApiParam(value = "城市名", required = true) @PathVariable(name = "cityName") String cityName,
            @ApiParam(value = "开始时间", required = true) @PathVariable(name = "startTime") Long startTime,
            @ApiParam(value = "结束时间", required = true) @PathVariable(name = "endTime") Long endTime) {
        Date sDate = new Date(startTime);
        Date eDate = new Date(endTime);
        List<WeatherHistoryVO> weatherHistoryVOs = iWeatherService.getWeatherHistory(cityName, sDate, eDate);
        return weatherHistoryVOs;
    }
}
