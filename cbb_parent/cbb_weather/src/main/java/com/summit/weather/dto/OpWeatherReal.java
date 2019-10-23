package com.summit.weather.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hyn  
 * @version V1.0  
 * @Title: OpWeatherReal.java
 * @Package com.summit.homs.dto.weather
 * @Description: TODO
 * @date 2018年12月3日 下午8:08:15
 */
@Entity
@Table(name = "op_weather_r")
@EntityListeners(AuditingEntityListener.class)
@ApiModel(value = "天气历史表")
public class OpWeatherReal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") // 这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    @ApiModelProperty(value = "id", name = "id", hidden = true, required = false)
    @Column(columnDefinition = "varchar(36)   comment 'id'")
    private String id;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", name = "moditime", example = "2018-08-27 00:00:00", required = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp on update current_timestamp comment'更新时间'")
    @UpdateTimestamp
    private Date moditime;


    /**
     * 地址
     */
    @ApiModelProperty(value = "地址", name = "address")
    @Column(columnDefinition = "varchar(20) not null default '' comment '地址'")
    private String address;

    @ApiModelProperty(value = "温度", name = "temperature")
    @Column(columnDefinition = "varchar(10) not null default '' comment '温度'")
    public String temperature;

    @ApiModelProperty(value = "天气", name = "weather")
    @Column(columnDefinition = "varchar(10) not null default '' comment '天气'")
    public String weather;

    @ApiModelProperty(value = "风力", name = "wind")
    @Column(columnDefinition = "varchar(10) not null default '' comment '风力'")
    public String wind;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getModitime() {
        return moditime;
    }

    public void setModitime(Date moditime) {
        this.moditime = moditime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
