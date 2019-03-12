package com.summit.domain.dictionary;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class DictionaryBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 15451645L;
	@ApiModelProperty(value="code",required=true)
	private String code;
	private String pcode;
	@ApiModelProperty(value="name",required=true)
	private String name;
	private String ckey;
	private String note;
	
	
	public DictionaryBean() {
		super();
	}

	public DictionaryBean(String code, String pcode, String name, String ckey,
			String note) {
		super();
		this.code = code;
		this.pcode = pcode;
		this.name = name;
		this.ckey = ckey;
		this.note = note;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public DictionaryBean clone() {
		return new DictionaryBean(this.code, this.pcode, this.name, this.ckey,
				this.note);
	}

	
	public String getId() {
		// TODO Auto-generated method stub
		return code;
	}

	
	public String getpId() {
		// TODO Auto-generated method stub
		return pcode;
	}
	
	
}
