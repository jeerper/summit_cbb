package com.summit.common.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 日志类，主要用于新增和修改
 * @author Administrator
 *
 */
public class LogBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="主键id(添加自动生成,修改必填)",name="id")
	private String id;
	
	@ApiModelProperty(value="功能模块",name="funName",required=true)
	private String funName;
	
	@ApiModelProperty(value="操作类型(1：新增,  2：修改,   3：删除,  4：授权 ,  5：查询  )",name="operType")
	private String operType;
	
	@ApiModelProperty(value="错误信息",name="erroInfo")
	private String erroInfo;
	
	@ApiModelProperty(value="调用是否成功标志  1：成功  0：失败",name="actionFlag")
	private String actionFlag;
	
	@ApiModelProperty(value="系统名称",name="systemName")
	private String systemName;
	
	@ApiModelProperty(value="操作描述",name="describe")
	private String describe;

	
	public LogBean() {
		super();
	}
	/**
	 * 新增
	 * @param funName
	 * @param systemName
	 * @param describe
	 * @param operType
	 */
	public LogBean(String funName,String systemName,String describe,String operType) {
		super();
		this.funName = funName;
		this.systemName = systemName;
		this.describe = describe;
		this.systemName = systemName;
		this.operType=operType;
	}
	
	/**
	 * 修改
	 * @param id
	 * @param funName
	 * @param systemName
	 * @param describe
	 * @param erroInfo
	 * @param actionFlag
	 */
	public LogBean(String id, String funName,String systemName,String describe, String erroInfo, String actionFlag,String operType) {
		super();
		this.id = id;
		this.funName = funName;
		this.systemName = systemName;
		this.describe = describe;
		this.erroInfo = erroInfo;
		this.systemName = systemName;
		this.actionFlag = actionFlag;
		this.operType=operType;
	}
	
	
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
