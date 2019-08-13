package com.summit.common.entity;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class DictionaryBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 15451645L;
	@ApiModelProperty(value="编码",name="code,新增不填写，修改必填",required=true)
	private String code;
	@ApiModelProperty(value="上级code",name="pcode",required=true)
	private String pcode;
	@ApiModelProperty(value="名称",name="name",required=true)
	private String name;
	@ApiModelProperty(value="标识值，引用表存此值",name="ckey",required=true)
	private String ckey;
	@ApiModelProperty(value="备注",name="note")
	private String note;
	@ApiModelProperty(value="下级信息",name="children",hidden = true)
	private List<DictionaryBean> children;
	
	
	
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
	

	public List<DictionaryBean> getChildren() {
		return children;
	}

	public void setChildren(List<DictionaryBean> children) {
		this.children = children;
	}

	@Override
	public DictionaryBean clone() {
		return new DictionaryBean(this.code, this.pcode, this.name, this.ckey,
				this.note);
	}

	
	
	
}
