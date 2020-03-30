package com.summit.cbb.utils.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.summit.excel.entity.ExcelCheckResult;
import com.summit.excel.listener.EasyExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author maoyx
 * @Date 2020/3/28 22:55
 **/
@Slf4j
@Service
public class ExcelService implements IExcelService {

    @Override
    public <T> ExcelCheckResult<T> importExcel(MultipartFile file, Class<T> clazz) throws IOException {
        return importExcel(file,clazz,new ExcelCheckManager());
    }

    @Override
    public <T> ExcelCheckResult<T> importExcel(MultipartFile file, Class<T> clazz, IExcelCheckManager<T> iExcelCheckManager) throws IOException {
        EasyExcelListener easyExcelListener = new EasyExcelListener(iExcelCheckManager);
        try {
            EasyExcel.read(file.getInputStream(),clazz,easyExcelListener).sheet().doRead();
        }catch (ExcelAnalysisException e){
            log.error(e.getMessage());
        }
        return new ExcelCheckResult<T>(easyExcelListener.getSuccessBeans(),easyExcelListener.getErrorBeans(),easyExcelListener.getErrorMsgs());
    }

}
