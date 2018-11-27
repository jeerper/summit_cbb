package com.summit.config.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//运行时保留
@Documented//生成到文档中
@Target(ElementType.METHOD)//作用范围是方法
public @interface SwitchDataSource {
  /**
   * value　使用的数据源的名称,默认为master
   * @return
   */
  String value() default "master";
}