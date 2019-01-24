package com.summit.domain.adcd;

import java.io.Serializable;

import com.summit.util.SummitTools.TreeNodeClass;

import net.sf.json.JSONObject;

public class ADCDTreeBean implements Serializable, TreeNodeClass<JSONObject> {
	private static final long serialVersionUID = 1L;
	private String adcd;
	private String padcd;
	private String adnm;
	private String level;
	private boolean open = false;
	
	public String getAdcd() {
		return adcd;
	}
	public void setAdcd(String adcd) {
		this.adcd = adcd;
	}
	public String getPadcd() {
		return padcd;
	}
	public void setPadcd(String padcd) {
		this.padcd = padcd;
	}
	public String getAdnm() {
		return adnm;
	}
	public void setAdnm(String adnm) {
		this.adnm = adnm;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public ADCDTreeBean(String adcd, String adnm, String padcd, String level) {
		super();
		this.adcd = adcd;
		this.adnm = adnm;
		this.padcd = padcd;
		this.level=level;
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
	public Boolean getOpen() {
		return this.open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	

}
