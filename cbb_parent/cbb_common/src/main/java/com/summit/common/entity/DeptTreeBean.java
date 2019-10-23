package com.summit.common.entity;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;


public class DeptTreeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键id,新增不填写，修改必填", name = "id")
    private String id;
    @ApiModelProperty(value = "编码", name = "value")
    private String value;
    @ApiModelProperty(value = "名称", name = "title")
    private String title;
    @ApiModelProperty(value = "上级编码", name = "padcd")
    private String pid;
    @ApiModelProperty(value = "下级集合", name = "children")
    private List<DeptTreeBean> children;

    public DeptTreeBean() {
        super();
    }

    public DeptTreeBean(String id, String value, String title, String pid) {
        super();
        this.id = id;
        this.value = value;
        this.title = title;
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<DeptTreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<DeptTreeBean> children) {
        this.children = children;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
