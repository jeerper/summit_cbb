package com.summit.cbb.utils.easyexcel.validation;

import com.alibaba.excel.annotation.ExcelProperty;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * excel正则校验
 *
 * @Author maoyx
 * @Date 2020/3/28 22:13
 **/

public class EasyExcelValidation {

    private EasyExcelValidation(){}

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> List<String> validateEntity(T obj,int row) throws NoSuchFieldException {
        List<String> result = new ArrayList<>();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (set != null && !set.isEmpty()) {
            for (ConstraintViolation<T> cv : set) {
                Field declaredField = obj.getClass().getDeclaredField(cv.getPropertyPath().toString());
                ExcelProperty annotation = declaredField.getAnnotation(ExcelProperty.class);
                //记录错误信息：第i行，第j列，（表头名）+ 错误信息
                result.add("第" + row + ",第" + (annotation.index() + 1) + "列,（" + annotation.value()[0] + "）" + cv.getMessage());
            }
        }
        return result;
    }

}
