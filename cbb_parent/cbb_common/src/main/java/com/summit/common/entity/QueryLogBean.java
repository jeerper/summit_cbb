package com.summit.common.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 日志类，主要用于查询
 * @author Administrator
 *
 */
public class QueryLogBean  implements Serializable {
	
	@ApiModelProperty(value="主键id",name="id")
	private String id;
	@ApiModelProperty(value="用户名",name="usernname")
	private String usernname;
	
	@ApiModelProperty(value="操作者姓名",name="name")
	private String name;
	
	@ApiModelProperty(value="操作类型(1：新增,  2：修改,   3：删除,   4：查询 )",name="operType")
	private String operType;
	
	@ApiModelProperty(value="访问机器IP",name="callerIP")
	private String callerIP;
	
	@ApiModelProperty(value="功能模块",name="funName")
	private String funName;
	
	@ApiModelProperty(value="访问开始时间",name="stime")
	private String stime;
	
	@ApiModelProperty(value="错误信息",name="erroInfo")
	private String erroInfo;
	
	@ApiModelProperty(value="修改时间",name="updateTime")
	private String updateTime;
	
	@ApiModelProperty(value="调用是否成功标志  1：成功  0：失败",name="actionFlag")
	private String actionFlag;
	
	@ApiModelProperty(value="系统名称",name="systemName")
	private String systemName;
	
	@ApiModelProperty(value="操作描述",name="describe")
	private String describe;
	

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public String getErroInfo() {
		return erroInfo;
	}

	public void setErroInfo(String erroInfo) {
		this.erroInfo = erroInfo;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsernname() {
		return usernname;
	}

	public void setUsernname(String usernname) {
		this.usernname = usernname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCallerIP() {
		return callerIP;
	}

	public void setCallerIP(String callerIP) {
		this.callerIP = callerIP;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}
}
