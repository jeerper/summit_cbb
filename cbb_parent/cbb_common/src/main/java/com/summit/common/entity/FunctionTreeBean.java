package com.summit.common.entity;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;


public class FunctionTreeBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="编码",name="value")
	private String value;
	@ApiModelProperty(value="名称",name="title")
	private String title;
	@ApiModelProperty(value="key",name="key")
	private String key;
	@ApiModelProperty(value="下级集合",name="children")
	private List<FunctionTreeBean> children;
	
	public FunctionTreeBean() {
		super();
	}
	
	public FunctionTreeBean(String value, String title, String key) {
		super();
		this.value = value;
		this.title = title;
		this.key = key;
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
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<FunctionTreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<FunctionTreeBean> children) {
		this.children = children;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
