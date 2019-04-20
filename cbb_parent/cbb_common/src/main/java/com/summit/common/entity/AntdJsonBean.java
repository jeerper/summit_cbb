package com.summit.common.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


public class AntdJsonBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="唯一标识",name="key")
	private String key;
	@ApiModelProperty(value="名称",name="title")
	private String title;
	@ApiModelProperty(value="编号",name="value")
	private String value;


	public AntdJsonBean() {
		super();
	}

	public AntdJsonBean(String key, String title, String value) {
		super();
		this.key = key;
		this.title = title;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
