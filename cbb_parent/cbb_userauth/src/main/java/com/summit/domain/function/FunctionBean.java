package com.summit.domain.function;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import net.sf.json.JSONObject;
/**
 * 功能基本属性
 * @author Administrator
 *
 */
public class FunctionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="主键id",name="id")
	private String id;
	@ApiModelProperty(value="父级菜单id",name="pid",required=true)
	private String pid;
	@ApiModelProperty(value="菜单名称",name="name",required=true)
	private String name;
	@ApiModelProperty(value="排序",name="fdesc",required=true)
	private Integer fdesc;
	@ApiModelProperty(hidden = true)
	private Integer isEnabled;
	@ApiModelProperty(value="路径地址",name="furl")
	private String furl;
	@ApiModelProperty(value="图片路径",name="imgUlr")
	private String imgUlr;
	@ApiModelProperty(value="备注",name="note")
	private String note;
	/**
	 * 超级功能
	 */
	@ApiModelProperty(value="超级功能",name="superfun")
	private String superfun;

	public FunctionBean() {
		super();
	}

	public FunctionBean(String id, String pid, String name, Integer fdesc,
			Integer isEnabled, String furl, String imgUlr, String note,String superfun) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.fdesc = fdesc;
		this.isEnabled = isEnabled;
		this.furl = furl;
		this.imgUlr = imgUlr;
		this.note = note;
		this.superfun=superfun;
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


	public String getSuperfun() {
		return superfun;
	}

	public void setSuperfun(String superfun) {
		this.superfun = superfun;
	}
}
