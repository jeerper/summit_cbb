package com.summit.cbb.utils.easyexcel.listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.summit.excel.IExcelCheckManager;
import com.summit.excel.entity.ExcelCheckResult;
import com.summit.excel.validation.EasyExcelValidation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * easyExcel通用监听类
 *
 * @Author maoyx
 * @Date 2020/3/28 22:01
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class EasyExcelListener<T> extends AnalysisEventListener<T> {

    //成功结果集
    private List<T> successBeans = new ArrayList<>();

    //失败结果集
    private List<T> errorBeans = new ArrayList<>();

    //失败原因记录
    private List<String> errorMsgs = new ArrayList<>();

    //业务校验接口
    private IExcelCheckManager<T> iExcelCheckManager;

    //正则校验成功的临时集合
    private List<T> list = new ArrayList<>();

    public EasyExcelListener(IExcelCheckManager<T> iExcelCheckManager){
        this.iExcelCheckManager = iExcelCheckManager;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        int currentRow = context.readRowHolder().getRowIndex() + 1;//当前行 : 逻辑当前行 + 1
        List<String> errMsgs = new ArrayList<>();
        try {
            //根据excel数据实体中的javax.validation + 正则表达式来校验excel数据
            errMsgs = EasyExcelValidation.validateEntity(data, currentRow);
        } catch (NoSuchFieldException e) {
            errMsgs.add("第" + currentRow + "行解析数据出错");
            e.printStackTrace();
        }

        if (errMsgs.size() == 0){
            list.add(data);
        }else {
            errorBeans.add(data);
            errorMsgs.addAll(errMsgs);
        }


        //每1000条临时数据处理一次
        if (list.size() > 1000){
            //业务校验
            businessCheck();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //对剩余正则校验成功的数据进行业务校验
        businessCheck();

    }

    //业务校验
    private void businessCheck(){
        ExcelCheckResult result = iExcelCheckManager.checkImportExcel(list);
        successBeans.addAll(result.getSuccessBeans());
        errorBeans.addAll(result.getErrorBeans());
        errorMsgs.addAll(result.getErrorMsgs());
        list.clear();
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        Class clazz = context.readWorkbookHolder().getClazz();
        if (clazz != null){
            try {
                List<String> headerErrorMsg = new ArrayList<>();
                Map<Integer, String> indexNameMap = getIndexNameMap(clazz);
                Set<Integer> keySet = indexNameMap.keySet();
                if (headMap.size() != indexNameMap.size()){
                    headerErrorMsg.add("解析excel表头出错，请检查表头信息是否齐全，标准表头数量为" + indexNameMap.size() + "列");
                }
                for (Integer key : keySet) {
                    if (StringUtils.isEmpty(headMap.get(key))){
                        headerErrorMsg.add("解析excel表头出错，请检查第"+key+"列表头信息");
                    }
                    if (!headMap.get(key).equals(indexNameMap.get(key))){
                        headerErrorMsg.add("解析excel表头出错，请检查第"+key+"列表头信息（"+headMap.get(key)+"）是否正确");
                    }
                }
                if (headerErrorMsg.size() > 0){
                    errorMsgs.addAll(headerErrorMsg);
                    throw new ExcelAnalysisException("excel表头解析异常");
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }


    //获取注解里ExcelProperty的value，用作校验excel表头一致性
    @SuppressWarnings("rawtypes")
    public Map<Integer,String> getIndexNameMap(Class clazz) throws NoSuchFieldException {
        Map<Integer,String> result = new HashMap<>();
        Field[] fields = FieldUtils.getAllFields(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null){//这里需要处理，需要进一步判断index和value不能为默认值，才可取
                result.put(annotation.index(),annotation.value()[0]);
            }
        }
        return result;
    }

}
