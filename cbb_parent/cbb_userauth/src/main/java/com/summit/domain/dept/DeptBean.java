package com.summit.domain.dept;

import java.io.Serializable;

import com.summit.util.SummitTools.TreeNodeClass;

import net.sf.json.JSONObject;


public class DeptBean  implements Serializable, TreeNodeClass<JSONObject> {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String pid;
	private String deptCode;
	private String deptName;
	private String remark;
	private boolean open = false;
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

	public Boolean getChecked() {
		// TODO Auto-generated method stub
		return null;
	}
	public Boolean getLeaf() {
		// TODO Auto-generated method stub
		return null;
	}
	public JSONObject getNodeData() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getNodeId() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getNodePid() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getNodeText() {
		// TODO Auto-generated method stub
		return null;
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

	public Boolean getOpen() {
		return this.open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
