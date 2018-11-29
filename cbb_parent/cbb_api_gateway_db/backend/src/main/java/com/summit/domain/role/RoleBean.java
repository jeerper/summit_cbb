package com.summit.domain.role;

import java.io.Serializable;



public class RoleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private String note;

	public RoleBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoleBean(String code, String name, String note) {
		super();
		this.code = code;
		this.name = name;
		this.note = note;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
