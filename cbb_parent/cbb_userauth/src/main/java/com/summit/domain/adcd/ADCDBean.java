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
		padcd = padcd;
		level = level;
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
		// TODO Auto-generated method stub
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
