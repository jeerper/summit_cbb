package com.summit.cbb.utils.easyexcel;

import com.summit.cbb.utils.easyexcel.entity.ExcelCheckResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author maoyx
 * @Date 2020/3/28 21:26
 **/
public class ExcelCheckManager<T> implements IExcelCheckManager<T>{

    @Override
    public ExcelCheckResult checkImportExcel(List<T> checks) {
        return new ExcelCheckResult(checks,new ArrayList<T>(),new ArrayList<String>());
    }

}
