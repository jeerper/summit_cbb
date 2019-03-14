package com.summit.domain.adcd;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


public class ADCDBean  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="adcd",required=true)
	private String adcd;
	@ApiModelProperty(value="adnm",required=true)
	private String adnm;
	@ApiModelProperty(value="padcd",required=true)
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
