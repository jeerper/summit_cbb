package com.summit.domain;

/**
 * 运算条件枚举类
 * @author 叶腾
 * @version 1.0
 */
public enum OperatorEnum {
	//相等
	EQ, 
	//模糊查询
	LIKE, 
	//大于
	GT, 
	//大于等于
	GTE, 
	//小于
	LT, 
	//小于等于
	LTE, 
	//IN查询
	IN, 
	//not_in查询
	NOT_IN,
	//不相等
	NE,
	//为空
	IS_NULL,
	//不为空
	NOT_NULL
}
