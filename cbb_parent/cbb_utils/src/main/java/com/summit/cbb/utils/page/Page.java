package com.summit.cbb.utils.page;

import java.util.List;


public class Page<T> {

    private List<T> content;
    private Pageable pageable;

    public Page() {
        super();
    }

    public Page(List<T> content, Pageable pageable) {
        super();
        this.content = content;
        this.pageable = pageable;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

}
