package com.summit.common.entity;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键id(添加自动生成,修改必填)", name = "id")
    private String id;
    @ApiModelProperty(value = "上级部门id", name = "pid", required = true)
    private String pid;
    @ApiModelProperty(value = "部门编码", name = "deptCode")
    private String deptCode;
    @ApiModelProperty(value = "部门名称", name = "deptName", required = true)
    private String deptName;
    @ApiModelProperty(value = "行政区划编码", name = "adcd")
    private String adcd;
    @ApiModelProperty(value = "行政区划名称", name = "adnm", hidden = true)
    private String adnm;
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    @ApiModelProperty(value = "下级部门", name = "children", hidden = true)
    private List<DeptBean> children;

    @ApiModelProperty(value = "上级部门名称", name = "pdeptName", hidden = true)
    private String pdeptName;

    @ApiModelProperty(value = "部门负责人", name = "deptHead", required = true)
    private String deptHead;

    @ApiModelProperty(value = "部门负责人", name = "deptHead", required = true)
    private String deptHeadName;

    @ApiModelProperty(value = "机构类型(0:内部机构;1:外部机构)", name = "deptType")
    private String deptType;

    @ApiModelProperty(value = "机构类型(0:内部机构;1:外部机构)", name = "deptType")
    private String deptTypeName;



    public DeptBean(String id, String pid, String deptCode, String deptName, String adcd, String adnm, String remark, List<DeptBean> children, String pdeptName, String deptHead, String deptType) {
        super();
        this.id = id;
        this.pid = pid;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.adcd = adcd;
        this.adnm = adnm;
        this.remark = remark;
        this.deptHead = deptHead;
        this.deptType = deptType;
    }

    public DeptBean() {
        super();
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }
    public String getDeptHead() {
        return deptHead;
    }

    public void setDeptHead(String deptHead) {
        this.deptHead = deptHead;
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

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DeptBean> getChildren() {
        return children;
    }

    public void setChildren(List<DeptBean> children) {
        this.children = children;
    }

    public String getPdeptName() {
        return pdeptName;
    }

    public void setPdeptName(String pdeptName) {
        this.pdeptName = pdeptName;
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


}
