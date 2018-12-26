package com.summit.domain.dictionary;

import com.summit.util.SummitTools.ZTreeNodeClass;

import java.io.Serializable;

public class DictionaryBean implements Serializable, ZTreeNodeClass<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 15451645L;
	private String code;
	private String pcode;
	private String name;
	private String ckey;
	private String note;
	private boolean open = false;
	
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

	public Boolean getChecked() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return code;
	}

	public Boolean getNocheck() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getNodeData() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getOpen() {
		return this.open;
	}

	public String getpId() {
		// TODO Auto-generated method stub
		return pcode;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
}
