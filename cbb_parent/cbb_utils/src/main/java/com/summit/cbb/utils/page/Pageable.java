package com.summit.cbb.utils.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="分页属性类")
public class Pageable {


    @ApiModelProperty(value="总记录数")
    private Integer rowsCount;

    @ApiModelProperty(value="总页数")
    private Integer pageCount;

    @ApiModelProperty(value="当前页")
    private Integer curPage;

    @ApiModelProperty(value="每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value="当前页的记录数")
    private Integer pageRowsCount;

    public Pageable() {
        super();
    }

    public Pageable(Integer rowsCount, Integer pageCount, Integer curPage, Integer pageSize, Integer pageRowsCount) {
        super();
        this.rowsCount = rowsCount;
        this.pageCount = pageCount;
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.pageRowsCount = pageRowsCount;
    }


    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(Integer rowsCount) {
        this.rowsCount = rowsCount;
    }

    public Integer getPageRowsCount() {
        return pageRowsCount;
    }

    public void setPageRowsCount(Integer pageRowsCount) {
        this.pageRowsCount = pageRowsCount;
    }
}
