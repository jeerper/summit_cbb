package com.summit.cbb.utils.page;


public class Pageable {

    /**
     * 总记录数
     *
     * @return
     */
    private Integer rowsCount;
    /**
     * 总页数
     *
     * @return
     */
    private Integer pageCount;
    /**
     * 当前页
     */
    private Integer curPage;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 当前页的总记录数
     */
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
