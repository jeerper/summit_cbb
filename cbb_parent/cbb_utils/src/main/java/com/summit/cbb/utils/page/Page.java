package com.summit.cbb.utils.page;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Page<T> extends com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> {

    private List<T> content=null;
    private Pageable pageable=null;

    public Page() {
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public Page(long current, long size) {
        this(current, size, 0);
    }

    public Page(long current, long size, long total) {
        this(current, size, total, true);
    }

    public Page(long current, long size, boolean isSearchCount) {
        this(current, size, 0, isSearchCount);
    }

    public Page(long current, long size, long total, boolean isSearchCount) {
        super(current, size, total, isSearchCount);
        pageable=new Pageable();
        pageable.setCurPage((int)current);
        pageable.setPageSize((int)size);
        pageable.setRowsCount((int)total);
    }

    public Page(List<T> content, Pageable pageable) {
        this.content = content;
        this.pageable = pageable;
    }
    @JsonIgnore
    @Override
    public List<T> getRecords() {
        return super.getRecords();
    }

    @JsonProperty
    @Override
    public Page<T> setRecords(List<T> records) {
        super.setRecords(records);
        content=records;
        if(pageable!=null){
            pageable.setPageRowsCount(content.size());
        }
        return this;
    }
    @JsonIgnore
    @Override
    public long getTotal() {
        return super.getTotal();
    }
    @JsonProperty
    @Override
    public Page<T> setTotal(long total) {
        super.setTotal(total);
        if(pageable==null){
            pageable=new Pageable();
        }
        pageable.setRowsCount((int)total);
        pageable.setPageCount((int)getPages());
        return this;
    }
    @JsonIgnore
    @Override
    public long getSize() {
        return super.getSize();
    }
    @JsonProperty
    @Override
    public Page<T> setSize(long size) {
        super.setSize(size);
        if(pageable==null){
            pageable=new Pageable();
        }
        pageable.setPageSize((int)size);
        return this;
    }
    @JsonIgnore
    @Override
    public long getCurrent() {
        return super.getCurrent();
    }
    @JsonProperty
    @Override
    public Page<T> setCurrent(long current) {
        super.setCurrent(current);
        if(pageable==null){
            pageable=new Pageable();
        }
        pageable.setCurPage((int)current);
        return this;
    }

    @JsonIgnore
    @Override
    public boolean isSearchCount() {
        return super.isSearchCount();
    }
    @JsonProperty
    @Override
    public Page<T> setSearchCount(boolean isSearchCount) {
        super.setSearchCount(isSearchCount);
        return this;
    }
    @JsonIgnore
    @Override
    public long getPages(){
        return super.getPages();
    }

    @JsonProperty
    @Override
    public Page<T> setPages(long pages){
        super.setPages(pages);
        return this;
    }

    @JsonIgnore
    @Override
    public List<OrderItem> getOrders() {
        return super.getOrders();
    }
    @JsonProperty
    @Override
    public void setOrders(List<OrderItem> orders) {
        super.setOrders(orders);
    }



    public List<T> getContent() {
        return content;
    }

    public Pageable getPageable() {
        return pageable;
    }



}
