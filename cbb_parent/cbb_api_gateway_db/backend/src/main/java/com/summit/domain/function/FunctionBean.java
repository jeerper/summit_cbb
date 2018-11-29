package com.summit.domain.function;

import java.io.Serializable;

import com.summit.util.SummitTools.TreeNodeClass;

import net.sf.json.JSONObject;

public class FunctionBean implements Serializable, TreeNodeClass<JSONObject> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String pid;
	private String name;
	private Integer fdesc;
	private Integer isEnabled;
	private String furl;
	private String imgUlr;
	private String note;

	public FunctionBean() {
		super();
	}

	public FunctionBean(String id, String pid, String name, Integer fdesc,
			Integer isEnabled, String furl, String imgUlr, String note) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.fdesc = fdesc;
		this.isEnabled = isEnabled;
		this.furl = furl;
		this.imgUlr = imgUlr;
		this.note = note;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFdesc() {
		return fdesc;
	}

	public void setFdesc(Integer fdesc) {
		this.fdesc = fdesc;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	public String getImgUlr() {
		return imgUlr;
	}

	public void setImgUlr(String imgUlr) {
		this.imgUlr = imgUlr;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getChecked() {
		return null;
	}

	public Boolean getLeaf() {
		return null;
	}

	public JSONObject getNodeData() {
		JSONObject jo = new JSONObject();
		jo.put("fdesc", fdesc);
		jo.put("isEnabled", isEnabled);
		jo.put("furl", furl);
		jo.put("imgUlr", imgUlr);
		jo.put("note", note);
		return jo;
	}

	public String getNodeId() {
		return id;
	}

	public String getNodePid() {
		return pid;
	}

	public String getNodeText() {
		return name;
	}

	public Boolean getOpen() {
		return true;
	}

}
