package com.summit.common.entity;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;


public class ADCDTreeBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="行政区划编码",name="value")
	private String value;
	@ApiModelProperty(value="行政区划名称",name="title")
	private String title;
	@ApiModelProperty(value="上级行政区划编码",name="padcd")
	private String padcd;
	@ApiModelProperty(value="级别",name="level")
	private String level;
	@ApiModelProperty(value="下级行政区划集合",name="children")
	private List<ADCDTreeBean> children;
	
	public ADCDTreeBean() {
		super();
	}
	
	public ADCDTreeBean(String value, String title, String padcd, String level) {
		super();
		this.value = value;
		this.title = title;
		this.padcd = padcd;
		this.level = level;
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

	
	public List<ADCDTreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<ADCDTreeBean> children) {
		this.children = children;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
