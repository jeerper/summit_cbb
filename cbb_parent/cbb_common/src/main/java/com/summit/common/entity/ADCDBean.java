package com.summit.common.entity;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;


public class ADCDBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "行政区划编码,新增，修改必填", name = "adcd", required = true)
    private String adcd;
    @ApiModelProperty(value = "行政区划名称", name = "adnm", required = true)
    private String adnm;
    @ApiModelProperty(value = "级别", name = "level")
    private String level;

    @ApiModelProperty(value = "上级行政区划编码", name = "padcd", required = true)
    private String padcd;
    @ApiModelProperty(value = "上级行政区划名称", name = "padnm", hidden = true)
    private String padnm;

    @ApiModelProperty(value = "下级行政区划集合", name = "children", hidden = true)
    private List<ADCDBean> children;

    @ApiModelProperty(value = "是否有子集", name = "hasChild", hidden = true)
    private boolean hasChild;

    public ADCDBean() {
        super();
    }

    public ADCDBean(String adcd, String adnm, String padcd, String level) {
        super();
        this.adcd = adcd;
        this.adnm = adnm;
        this.padcd = padcd;
        this.level = level;
    }

    public String getAdcd() {
        return adcd;
    }

    public void setAdcd(String adcd) {
        this.adcd = adcd;
    }

    public String getAdnm() {
        return adnm;
    }

    public void setAdnm(String adnm) {
        this.adnm = adnm;
    }

    public String getPadcd() {
        return padcd;
    }

    public void setPadcd(String padcd) {
        this.padcd = padcd;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public List<ADCDBean> getChildren() {
        return children;
    }

    public void setChildren(List<ADCDBean> children) {
        this.children = children;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getPadnm() {
        return padnm;
    }

    public void setPadnm(String padnm) {
        this.padnm = padnm;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }
}
