package com.summit.domain.dept;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;


public class DeptBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	@ApiModelProperty(value="pid",required=true)
	private String pid;
	@ApiModelProperty(value="deptCode",required=true)
	private String deptCode;
	@ApiModelProperty(value="deptName",required=true)
	private String deptName;
	private String remark;
	private List<DeptBean> children;
	public DeptBean() {
		super();
	}
	
	public DeptBean(String id, String pid, String deptCode, String deptName,String remark) {
		super();
		this.id = id;
		this.pid = pid;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.remark = remark;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<DeptBean> getChildren() {
		return children;
	}

	public void setChildren(List<DeptBean> children) {
		this.children = children;
	}
	
}
