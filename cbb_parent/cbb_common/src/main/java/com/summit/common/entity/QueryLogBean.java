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
	private String username;
	
	@ApiModelProperty(value="操作者姓名",name="name")
	private String name;
	
	@ApiModelProperty(value="操作类型(1：新增,  2：修改,   3：删除,  4：授权 ,  5：查询  )",name="operType")
	private String operType;
	
	@ApiModelProperty(value="访问机器IP",name="callerIP")
	private String callerIP;
	
	@ApiModelProperty(value="功能模块",name="funName")
	private String funName;
	
	@ApiModelProperty(value="访问开始时间",name="stime")
	private String stime;
	
	@ApiModelProperty(value="访问结束时间",name="stime")
	private String etime;
	
	@ApiModelProperty(value="访问时长",name="actiontime")
	private String actiontime;
	
	@ApiModelProperty(value="错误信息",name="erroInfo")
	private String erroInfo;
	
	@ApiModelProperty(value="修改时间",name="updateTime")
	private String updateTime;
	
	@ApiModelProperty(value="调用是否成功标志  1：成功  0：失败",name="actionFlag")
	private String actionFlag;
	
	@ApiModelProperty(value="系统名称",name="systemName")
	private String systemName;
	
	@ApiModelProperty(value="操作描述",name="operInfo")
	private String operInfo;
	

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

	public String getOperInfo() {
		return operInfo;
	}

	public void setOperInfo(String operInfo) {
		this.operInfo = operInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getActiontime() {
		return actiontime;
	}

	public void setActiontime(String actiontime) {
		this.actiontime = actiontime;
	}
	
	
}
