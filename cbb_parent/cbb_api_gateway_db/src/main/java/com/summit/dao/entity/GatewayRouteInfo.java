package com.summit.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;


@TableName("gateway_route_info")
public class GatewayRouteInfo implements Serializable {

    private static final long serialVersionUID = 7247402465606982441L;
    /**
     * 路由id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 路由名称
     */
    @TableField("name")
    private String name;

    /**
     * 路由路径
     */
    @TableField("path")
    private String path;

    /**
     * 服务id
     */
    @TableField("service_id")
    private String serviceId;

    /**
     * 转发时是否去掉前缀
     */
    @TableField("strip_prefix")
    private int stripPrefix;
    /**
     * 是否重试
     */
    @TableField("retryable")
    private int retryable;
    /**
     * 是否启用
     */
    @TableField("enabled")
    private int enabled;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(int stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public int getRetryable() {
        return retryable;
    }

    public void setRetryable(int retryable) {
        this.retryable = retryable;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
