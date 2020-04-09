/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.summit.cbb.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xjtuhgd
 * @date 2019/12/09
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /**
     * 导出字段名（默认调用当前字段的“get”方法，如指定导出字段为对象，请填写“对象名.对象属性”，例：“area.name”、“office.name”）
     */
    String value() default "";

    /**
     * 导出字段标题（需要添加批注请用“**”分隔，标题**批注，仅对导出模板有效）
     */
    String title() default "";

    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     */
    int type() default 0;

    /**
     * 导出字段对齐方式（0：自动；1：靠左；2：居中；3：靠右）
     */
    int align() default 0;

    /**
     * 导出字段字段排序（升序）
     */
    int sort() default 0;

    /**
     * 如果是字典类型，请设置字典的type值
     */
    String dictType() default "";

    /**
     * 反射类型
     */
    Class<?> fieldType() default Class.class;

    /**
     * 字段归属组（根据分组导出导入）
     */
    int[] groups() default {};

    /**
     * oraLength 数据库中的长度 varchar或者char类型的时候才不是0
     */
    int maxLength() default 0;

    /**
     * minLength 最小长度
     */
    int minLength() default 0;

    /**
     * 是否可为null 默认可为null
     */
    boolean nullable() default true;

    /**
     * 最大值  数据库是数字时设置
     */
    double maxValue() default 0;

    /**
     * 最小值  数据库是数字时设置
     */
    double minValue() default -1111111;

    /**
     * 日期格式 默认"yyyy-MM-dd" 如果有变动请做相应调整
     */
    String dateFormat() default "yyyy-MM-dd";

    /**
     * 最大小数位数，默认0位，根据需要设置
     */
    int maxDecimalDigits() default 0;

    /**
     * 可用取值集合
     */
    String[] valueGroup() default {};

    /**
     * 取值是否严格按照取值集合取值，默认为false
     * 当取值不在取值集合中时，提示infoMsg，数据库存入null
     * 当设置为true时，严格按照取值集合取值，会提示errorMsg，不存库
     */
    boolean strict() default false;
}
