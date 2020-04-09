package com.summit.cbb.utils.constants;


import lombok.Data;

import java.util.List;

/**
 * 过程数据sheet页信息
 * 
 * @author xjtuhgd
 * @date 2020/03/05
 */
@Data
public class ExportSheetVo<T> {

    /**
     * 数据列表
     */
    private List<T> dataList;
    /**
     * 数据类
     */
    private Class<T> c;
    /**
     * sheet页标题
     */
    private String title;
    /**
     * 备注
     */
    private String note;
    /**
     * sheet页名称
     */
    private String sheetName;

    public ExportSheetVo(List<T> dataList, Class<T> c, String title, String note, String sheetName) {
        super();
        this.dataList = dataList;
        this.c = c;
        this.title = title;
        this.note = note;
        this.sheetName = sheetName;
    }
}
