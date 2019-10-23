package com.summit.dao;


import java.util.Date;

/**
 * @author yt
 */
@FunctionalInterface
public interface DateOperator {
    /**
     * 运算
     *
     * @param t
     * @return
     */
    public abstract boolean operator(Date t);
}
