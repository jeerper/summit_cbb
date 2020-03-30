package com.summit.cbb.utils.easyexcel;


import com.summit.excel.entity.ExcelCheckResult;

import java.util.List;

/**
 * excel业务校验接口
 *
 * @Author maoyuxuan
 * @Date 2020/3/28 22:10
 **/
public interface IExcelCheckManager<T> {

    ExcelCheckResult checkImportExcel(List<T> checks);

}
