package com.summit.domain.adcd;

import java.io.Serializable;

import com.summit.util.SummitTools.TreeNodeClass;

import net.sf.json.JSONObject;


public class ADCDBean  implements Serializable, TreeNodeClass<JSONObject> {
	private static final long serialVersionUID = 1L;
	
	private String adcd;
	private String adnm;
	private String padcd;
	private String level;
	public ADCDBean() {
		super();
	}
	
	public ADCDBean(String adcd, String adnm, String padcd, String level) {
		super();
		this.adcd = adcd;
		this.adnm = adnm;
		this.padcd = padcd;
		this.level = level;
	}

	public Boolean getChecked() {
		return null;
	}
	public Boolean getLeaf() {
		return null;
	}
	public JSONObject getNodeData() {
		return null;
	}
	public String getNodeId() {
		return null;
	}
	public String getNodePid() {
		return null;
	}
	public String getNodeText() {
		return null;
	}
	public Boolean getOpen() {
		return null;
	}

	public String getAdcd() {
		return adcd;
	}

	public void setAdcd(String adcd) {
		this.adcd = adcd;
	}

	public String getAdnm() {
		return adnm;
	}

	public void setAdnm(String adnm) {
		this.adnm = adnm;
	}
	
	public String getPadcd() {
		return padcd;
	}

	public void setPadcd(String padcd) {
		this.padcd = padcd;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
