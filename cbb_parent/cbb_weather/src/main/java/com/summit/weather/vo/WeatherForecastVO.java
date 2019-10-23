package com.summit.weather.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hyn  
 * @version V1.0  
 * @Title: WeatherDataBO.java
 * @Package com.summit.bo.weather
 * @Description: TODO
 * @date 2018年11月28日 下午7:59:25
 */
@ApiModel(value = "天气预报信息")
public class WeatherForecastVO {

    // 实时天气状况
    @ApiModelProperty(value = "实时天气状况", name = "realWeather")
    RealWeather realWeather;

    @ApiModelProperty(value = "当天天气状况", name = "todayWeather")
    TodayWeather todayWeather;

    @ApiModelProperty(value = "未来天气状况", name = "futureWeathers")
    List<FutureWeather> futureWeathers;

    public TodayWeather getTodayWeather() {
        return todayWeather;
    }

    public void setTodayWeather(TodayWeather todayWeather) {
        this.todayWeather = todayWeather;
    }

    public List<FutureWeather> getFutureWeathers() {
        return futureWeathers;
    }

    public void setFutureWeathers(List<FutureWeather> futureWeathers) {
        this.futureWeathers = futureWeathers;
    }

    public RealWeather getRealWeather() {
        return realWeather;
    }

    public void setRealWeather(RealWeather realWeather) {
        this.realWeather = realWeather;
    }


    // 实时天气
    @ApiModel(value = "实时天气")
    public static class RealWeather {

        @ApiModelProperty(value = "温度", name = "temperature")
        public String temperature;

        @ApiModelProperty(value = "天气", name = "weather")
        public String weather;

        @ApiModelProperty(value = "风力", name = "wind")
        public String wind;

        @ApiModelProperty(value = "时间", name = "date")
        public String date;

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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }


    }

    // 未来天气
    @ApiModel(value = "未来天气")
    public static class FutureWeather {

        @ApiModelProperty(value = "温度", name = "temperature")
        public String temperature;

        @ApiModelProperty(value = "天气", name = "weather")
        public String weather;

        @ApiModelProperty(value = "风力", name = "wind")
        public String wind;

        @ApiModelProperty(value = "星期", name = "week")
        public String week;

        @ApiModelProperty(value = "时间", name = "date")
        public String date;

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

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

    // 今天天气
    @ApiModel(value = "当天天气")
    public static class TodayWeather {

        @ApiModelProperty(value = "温度", name = "temperature")
        public String temperature;

        @ApiModelProperty(value = "天气", name = "weather")
        public String weather;

        @ApiModelProperty(value = "风力", name = "wind")
        public String wind;

        @ApiModelProperty(value = "星期", name = "week")
        public String week;

        @ApiModelProperty(value = "城市", name = "city")
        public String city;

        @ApiModelProperty(value = "时间", name = "date")
        public String date;

        @ApiModelProperty(value = "穿衣指数", name = "dressingAdvice")
        public String dressingAdvice;

        @ApiModelProperty(value = "紫外线强度", name = "uvIndex")
        public String uvIndex;

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

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDressingAdvice() {
            return dressingAdvice;
        }

        public void setDressingAdvice(String dressingAdvice) {
            this.dressingAdvice = dressingAdvice;
        }

        public String getUvIndex() {
            return uvIndex;
        }

        public void setUvIndex(String uvIndex) {
            this.uvIndex = uvIndex;
        }

    }

}
