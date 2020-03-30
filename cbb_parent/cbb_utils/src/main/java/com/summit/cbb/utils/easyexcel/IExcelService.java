package com.summit.cbb.utils.easyexcel;

import com.summit.excel.entity.ExcelCheckResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * excel服务
 *
 * @Author maoyx
 * @Date 2020/3/28 22:54
 **/
public interface IExcelService {

    /**
     * excel导入数据解析
     * @param file
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> ExcelCheckResult<T> importExcel(MultipartFile file, Class<T> clazz) throws IOException;

    /**
     * excel导入数据解析2 （使用业务校验）
     * @param file
     * @param clazz
     * @param iExcelCheckManager    业务校验类
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> ExcelCheckResult<T> importExcel(MultipartFile file, Class<T> clazz, IExcelCheckManager<T> iExcelCheckManager) throws IOException;



}
