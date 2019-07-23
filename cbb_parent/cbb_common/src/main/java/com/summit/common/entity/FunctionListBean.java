package com.summit.common.entity;

import java.io.Serializable;
import java.util.List;

import com.summit.common.entity.FunctionBean;

import io.swagger.annotations.ApiModelProperty;


public class FunctionListBean implements Serializable{

	@ApiModelProperty(value="用户功能菜单",name="functionList")
	private List<FunctionBean> functionList;
	
	@ApiModelProperty(value="角色功能id",name="functionIdList")
	private List<String> functionIdList;

	public List<FunctionBean> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<FunctionBean> functionList) {
		this.functionList = functionList;
	}

	public List<String> getFunctionIdList() {
		return functionIdList;
	}

	public void setFunctionIdList(List<String> functionIdList) {
		this.functionIdList = functionIdList;
	}
	
}
